package com.madalla.service.cms.jcr;

import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.model.tree.JcrTreeModel;
import com.madalla.util.jcr.model.tree.JcrTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RepositoryAdminService extends AbstractRepositoryService implements IRepositoryAdminService {
	
    private static final long serialVersionUID = 1L;
    private static final String FILE_SUFFIX = ".xml";
    private static final String APP = "applications";
    private JcrTemplate template;
    private String repositoryHome;
    private final Log log = LogFactory.getLog(this.getClass());
    
    static final String EC_NODE_BACKUP = NS + "backup";
    
    //TODO allow switching between different workspaces
    public String[] getAvailableWorkspaces(){
    	return (String[]) template.execute(new JcrCallback(){
    		
    		public Object doInJcr(Session session) throws IOException, RepositoryException{
 				JackrabbitWorkspace workpace = (JackrabbitWorkspace)session.getWorkspace();
				return workpace.getAccessibleWorkspaceNames();
    		}
    	});
    }
    
    //TODO create new workspaces from admin console
    public void createNewWorkspace(final String workspaceName){
    	template.execute(new JcrCallback(){
    		
    		public Object doInJcr(Session session) throws IOException, RepositoryException{
				JackrabbitWorkspace workpace = (JackrabbitWorkspace)session.getWorkspace();
				workpace.createWorkspace(workspaceName);
    			return null;
    		}
    	});
    }

    public TreeModel getRepositoryContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = getApplicationNode(session);
                session.save();
                JcrNodeModel nodeModel = new JcrNodeModel(rootNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootNode);
                return jcrTreeModel;
            }
        });
    }
    
    public TreeModel getSiteContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node siteNode = getSiteNode(session);
                session.save();
                JcrNodeModel nodeModel = new JcrNodeModel(siteNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(siteNode);
                return jcrTreeModel;
            }
        });
    }
    
    public String backupContentRoot(){
    	log.info("backupContentRoot - Backing up Content Repository...");
    	String file = (String) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getApplicationNode(session);
                session.save();
				File backupFile = getBackupFile(APP);
				FileOutputStream out = new FileOutputStream(backupFile);
                session.exportDocumentView(node.getPath(), out, true, false);
                out.close();
				return backupFile.getName();
			}
    	});
    	return file;
    }
    
    public String backupContentSite(){
    	log.info("backupContentSite - Backing up Content Repository for Site. site=" + getSite());
    	String file = (String) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getSiteNode(session);
                session.save();
				File backupFile = getBackupFile(getSite());
				FileOutputStream out = new FileOutputStream(backupFile);
                session.exportDocumentView(node.getPath(), out, true, false);
                out.close();
				return backupFile.getName();
			}
    	});
    	return file;
    }
    
    public List<File> getApplicationBackupFileList(){
    	return getFileList(APP);
    }
    
    public List<File> getBackupFileList() {
    	return getFileList(getSite());
    }
    
    private List<File> getFileList(final String fileStart){
    	File repositoryHomeDir;
		try {
			repositoryHomeDir = getRepositoryHomeDir();
		} catch (IOException e) {
			log.error("Unable to get Repository Home Directory.", e);
			return null;
		}
    	File[] files = repositoryHomeDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(name.endsWith(FILE_SUFFIX)){
                    if (name.startsWith(fileStart)){
                        return true;
                    }
				}
				return false;
			}
    	});
        log.debug("getBackupFileList - Number of backup files found="+files.length);
    	return Arrays.asList(files);
    }
    
    //TODO refactore the 2 restore methods
    public void restoreContentApplication(final File backupFile) {
        log.info("restoreContentApplication - Importing data to repository from file. file ="+backupFile.getPath());
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				InputStream in = new FileInputStream(backupFile);
				
				Node appnode = getApplicationNode(session);
				String importPath = appnode.getParent().getPath();
				Node backupParent = getCreateNode(EC_NODE_BACKUP, session.getRootNode());
				session.save();
				
				//remove old backup if exists
				if (backupParent.hasNode(EC_NODE_APP)){
					Node oldBackup = backupParent.getNode(EC_NODE_APP);
					log.info("restoreContentSite - removing an old backup.");
					oldBackup.remove();
					session.save();
				}
				//move site out to backup
				String srcPath = appnode.getPath();
				String destPath = backupParent.getPath()+"/"+appnode.getName();
				log.info("Moving site from "+srcPath + " to "+ destPath);
				session.move(srcPath, destPath);
				session.save();
				
				session.importXML(importPath, in, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                session.save();
				return null;
			}
    	});
    }

    public void restoreContentSite(final File backupFile) {
        log.info("restoreContentSite - Importing data to repository from file. file ="+backupFile.getPath());
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				InputStream in = new FileInputStream(backupFile);
				
				Node siteNode = getSiteNode(session);
				String importPath = siteNode.getParent().getPath();
				Node backupParent = getCreateBackupNode(session);
				session.save();
				
				//remove old backup if exists
				if (backupParent.hasNode(NS+getSite())){
					Node oldBackup = backupParent.getNode(NS+getSite());
					log.info("restoreContentSite - removing an old backup.");
					oldBackup.remove();
					session.save();
				}
				//move site out to backup
				String srcPath = siteNode.getPath();
				String destPath = backupParent.getPath()+"/"+siteNode.getName();
				log.info("Moving site from "+srcPath + " to "+ destPath);
				session.move(srcPath, destPath);
				session.save();
				
				session.importXML(importPath, in, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                session.save();
				return null;
			}
    	});
    }
    
    public Boolean isRollbackSiteAvailable(){
		return isRollBackAvailable(false);
    }
    
    public Boolean isRollbackApplicationAvailable(){
		return isRollBackAvailable(true);
    }
    
    private Boolean isRollBackAvailable(final Boolean application){
    	//if there is a backup available then rollback is available
    	Boolean available = (Boolean) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				if (application){
					Node backupParent = getCreateNode(EC_NODE_BACKUP, session.getRootNode());
					session.save();
                    return backupParent.hasNode(EC_NODE_APP);
				} else {
					Node backupParent = getCreateBackupNode(session);
					session.save();
                    return backupParent.hasNode(NS + getSite());
				}
			}
    		
    	});
    	return available;
    }

    public void rollbackApplicationRestore() {
		rollback(true);
	}
    
    public void rollbackSiteRestore(){
    	rollback(false);
    }
    
    private Node getCreateBackupNode(Session session) throws RepositoryException{
    	Node appNode = getCreateNode(EC_NODE_APP, session.getRootNode());
    	return getCreateNode(EC_NODE_BACKUP, appNode);
    }

    
    private void rollback(final Boolean application){
    	log.info("rollback - Attempting to rollback restore. Site ="+getSite());
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				String backupName;
				Node backupParent;
				Node destinationNode;
				if(application){
					backupName = EC_NODE_APP;
					backupParent = getCreateNode(EC_NODE_BACKUP, session.getRootNode());
					destinationNode = getApplicationNode(session);
				} else {
					backupName = NS+getSite();
					backupParent = getCreateBackupNode(session);
					destinationNode = getSiteNode(session);
				}
				session.save();

				//check for backup 
				if (backupParent.hasNode(backupName)){
					Node backup = backupParent.getNode(backupName);
					log.info("rollbackSiteRestore - found backup.");
					
					//remove destination
					String destPath = destinationNode.getPath();
					destinationNode.remove();
					session.save();
					
					//move backup to site
					String srcPath = backup.getPath();
					log.info("rollbackSiteRestore - Moving site from "+srcPath + " to "+ destPath);
					session.move(srcPath, destPath);
					session.save();
					log.info("rollbackSiteRestore - rollback success.");
				} else {
					log.info("rollbackSiteRestore - No backup found - rollback failed.");
				}
				return null;
			}
    	});

    }

	private File getBackupFile(String fileName) throws IOException {
		
        //Get repository home directory and create File
		File repositoryHomeDir = getRepositoryHomeDir();
        String dateTimeString = ISODateTimeFormat.basicDateTime().print(new LocalDateTime());

		File backupFile = new File(repositoryHomeDir,fileName+"-backup-"+dateTimeString+FILE_SUFFIX);
        if (backupFile.exists()){
        	log.error("Backup file exists. Should not happen.");
		}
        log.debug("Backup file name. fileName="+backupFile);
        
		return backupFile;
	}
	
	private File getRepositoryHomeDir() throws IOException{
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(repositoryHome);
        return resource.getFile();
	}
	
	public String createOriginalImage(String imageName, InputStream fullImage) {
		return ImageDataHelper.getInstance().saveOriginalImage(imageName, fullImage);
	}


	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public void setRepositoryHome(String repositoryHome) {
		this.repositoryHome = repositoryHome;
	}

}
