package com.madalla.service.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.model.tree.JcrTreeModel;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

public class ContentAdminService extends AbstractContentService implements IContentAdminService {
	
    private static final long serialVersionUID = 1L;
    private static final String FILE_SUFFIX = ".xml";
    private static final String APP = "applications";
    private JcrTemplate template;
    private String repositoryHome;
    private final Log log = LogFactory.getLog(this.getClass());
    
    

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
                Node siteNode = getCreateSiteNode(session);
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
    
    public void backupContentSite(){
    	String file = (String) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getCreateSiteNode(session);
                session.save();
				File backupFile = getBackupFile(site);
				FileOutputStream out = new FileOutputStream(backupFile);
                session.exportDocumentView(node.getPath(), out, true, false);
                out.close();
				return backupFile.getName();
			}
    	});
    }
    
    public File[] getApplicationBackupFileList(){
    	return getFileList(APP);
    }
    
    public File[] getBackupFileList() {
    	return getFileList(site);
    }
    
    private File[] getFileList(final String fileStart){
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
    	return files;
    }
    
    public void restoreContentApplication(final File backupFile) {
        log.info("Importing data to repository from file. file ="+backupFile.getPath());
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
        log.info("Importing data to repository from file. file ="+backupFile.getPath());
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				InputStream in = new FileInputStream(backupFile);
				
				Node siteNode = getCreateSiteNode(session);
				String importPath = siteNode.getParent().getPath();
				Node backupParent = getCreateBackupNode(session);
				session.save();
				
				//remove old backup if exists
				if (backupParent.hasNode(NS+site)){
					Node oldBackup = backupParent.getNode(NS+site);
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
    
    public void rollbackSiteRestore(){
    	log.info("Attempting to rollback restore. Site ="+site);
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				Node siteNode = getCreateSiteNode(session);
				Node backupParent = getCreateBackupNode(session);
				session.save();

				//check for backup 
				if (backupParent.hasNode(NS+site)){
					Node backup = backupParent.getNode(NS+site);
					log.info("rollbackSiteRestore - found backup.");
					
					//remove site
					String destPath = siteNode.getPath();
					siteNode.remove();
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

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public void setRepositoryHome(String repositoryHome) {
		this.repositoryHome = repositoryHome;
	}
    
}
