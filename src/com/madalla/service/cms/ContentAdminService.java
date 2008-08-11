package com.madalla.service.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.model.tree.JcrTreeModel;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

public class ContentAdminService extends AbstractContentService implements IContentAdminService {
	
    private static final long serialVersionUID = 1L;
    private static final String FILE_SUFFIX = "-backup.xml";
    private static final String APP = "applications";
    private JcrTemplate template;
    private String repositoryHome;
    private final Log log = LogFactory.getLog(this.getClass());
    
    

    public TreeModel getRepositoryContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = getApplicationNode(session);
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
                JcrNodeModel nodeModel = new JcrNodeModel(siteNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(siteNode);
                return jcrTreeModel;
            }
        });
    }
    
    public void backupContentRoot(){
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getApplicationNode(session);
				OutputStream out = getBackupFile(APP);
                session.exportDocumentView(node.getPath(), out, true, false);
                out.close();
				return null;
			}
    	});
    }
    
    public void backupContentSite(){
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getCreateSiteNode(session);
				OutputStream out = getBackupFile(site);
                session.exportDocumentView(node.getPath(), out, true, false);
				return null;
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
				Node backupParent = getCreateBackupNode(session);
				session.save();
				
				//remove old backup if exists
				if (backupParent.hasNode(NS+site)){
					Node oldBackup = backupParent.getNode(NS+site);
					log.debug("restoreContentSite - removing an old backup.");
					oldBackup.remove();
					session.save();
				}
				//move site out to backup
				String srcPath = appnode.getPath();
				String destPath = backupParent.getPath()+"/"+appnode.getName();
				log.debug("Moving site from "+srcPath + " to "+ destPath);
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
					log.debug("restoreContentSite - removing an old backup.");
					oldBackup.remove();
					session.save();
				}
				//move site out to backup
				String srcPath = siteNode.getPath();
				String destPath = backupParent.getPath()+"/"+siteNode.getName();
				log.debug("Moving site from "+srcPath + " to "+ destPath);
				session.move(srcPath, destPath);
				session.save();
				
				session.importXML(importPath, in, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                session.save();
				return null;
			}
    	});
    }

	private OutputStream getBackupFile(String fileName) throws IOException {
		
        //Get repository home directory and create File
		File repositoryHomeDir = getRepositoryHomeDir();
        File backupFile = new File(repositoryHomeDir,fileName+FILE_SUFFIX);
        if (backupFile.exists()){
        	log.debug("Backup file exists, we should move old one.");
			//TODO Use jakarta Commons FileUtils to move old backup
			//FileUtils
		}
        log.debug("Backup file name. fileName="+backupFile);
        FileOutputStream fileOut = new FileOutputStream(backupFile);
		return fileOut;
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
