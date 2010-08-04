package com.madalla.cms.service.jcr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.cms.jcr.JcrUtils;
import com.madalla.cms.jcr.NodeDisplay;
import com.madalla.cms.jcr.model.JcrNodeModel;
import com.madalla.cms.jcr.model.tree.JcrTreeModel;
import com.madalla.cms.jcr.model.tree.JcrTreeNode;
import com.madalla.service.BackupFile;
import com.madalla.service.IRepositoryAdminService;

public class RepositoryAdminService extends AbstractJcrRepositoryService implements IRepositoryAdminService {
	
    private static final String APP = "applications";
    private JcrTemplate template;
    private String repositoryHome;
    private final Log log = LogFactory.getLog(this.getClass());
    
    static final String EC_NODE_BACKUP = NS + "backup";
    
    public NodeDisplay getNodeDisplay(final String path){
    	return (NodeDisplay) template.execute(new JcrCallback(){
       		
    		public Object doInJcr(Session session) throws IOException, RepositoryException{
				Item item = session.getItem(path);
				return new NodeDisplay(item);
    		}
    		
    	});
    }
    
    public void deleteNode(String path){
    	JcrUtils.deleteNode(template, path);
    }
    
    public void pasteNode(final String srcPath, final String destPath){
    	template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException, RepositoryException {
				session.move(srcPath, destPath);
				session.save();
				return null;
			}
    		
    	});
    }
    
    public DefaultTreeModel getRepositoryContent(){
        return (DefaultTreeModel) template.execute(new JcrCallback(){
            
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
    
    public DefaultTreeModel getSiteContent(){
        return (DefaultTreeModel) template.execute(new JcrCallback(){
            
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
				File backupFile = BackupFile.getBackupFile(APP, repositoryHome);
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
				File backupFile = BackupFile.getBackupFile(getSite(), repositoryHome);
				FileOutputStream out = new FileOutputStream(backupFile);
                session.exportDocumentView(node.getPath(), out, true, false);
                out.close();
				return backupFile.getName();
			}
    	});
    	return file;
    }
    
    public List<BackupFile> getApplicationBackupFileList(){
    	return BackupFile.getFileList(APP, repositoryHome);
    }
    
    public List<BackupFile> getBackupFileList() {
    	return BackupFile.getFileList(getSite(),repositoryHome);
    }
    
    //TODO refactor the 2 restore methods
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
    
	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public void setRepositoryHome(String repositoryHome) {
		this.repositoryHome = repositoryHome;
	}

}