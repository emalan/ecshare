package com.madalla.service.cms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
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

public class ContentAdminService implements IContentData, IContentAdminService {
	
    private static final long serialVersionUID = 1L;
    private static final String FILE_SUFFIX = "-backup.xml";
    private JcrTemplate template;
    private String site ;
    private String repositoryHome;
    private final Log log = LogFactory.getLog(this.getClass());
    

    public TreeModel getSiteContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootContentNode = session.getRootNode().getNode(EC_NODE_APP);
                Node siteNode = rootContentNode.getNode(EC_NODE_SITE);

                //TODO change to using site Node. Site Nood must act like root.
                JcrNodeModel nodeModel = new JcrNodeModel(rootContentNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootContentNode);
                return jcrTreeModel;
            }
        });
    }
    
    public void backupContentRoot(){
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getRootNode(session);
				OutputStream out = getBackupFile("root");
                session.exportDocumentView(node.getPath(), out, true, false);
				return null;
			}
    	});
    }
    
    public void backupContentApps(){
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getSiteAppNode(session);
				OutputStream out = getBackupFile("apps");
                session.exportDocumentView(node.getPath(), out, true, false);
				return null;
			}
    	});
    }
    
    public void restoreFromFile(final InputStream in){
        template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node node = getSiteNode(session);
                session.importXML(node.getPath(), in, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
                return null;
            }
        });
    }
    
    public void backupContentSite(){
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = getSiteNode(session);
				OutputStream out = getBackupFile(site);
                session.exportDocumentView(node.getPath(), out, true, false);
				return null;
			}
    	});
    }

	private OutputStream getBackupFile(String fileName) throws IOException {
		
        //Get repository home directory and create File
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(repositoryHome);
        File repositoryHomeDir = resource.getFile();
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

	private Node getRootNode(Session session) throws RepositoryException {
		return session.getRootNode();
	}
	
	private Node getSiteAppNode(Session session) throws PathNotFoundException, RepositoryException{
		return session.getRootNode().getNode(EC_NODE_APP);
	}
	
	private Node getSiteNode(Session session) throws PathNotFoundException, RepositoryException{
		Node appNode = session.getRootNode().getNode(EC_NODE_APP);
		Node rt = null;
		for (NodeIterator iter = appNode.getNodes(EC_NODE_SITE); iter.hasNext();){
			Node test = iter.nextNode();
			if (test.hasProperties() && test.hasProperty(EC_PROP_TITLE)){
                String testTitle = test.getProperty(EC_PROP_TITLE).getString();
                if (testTitle.equals(site)){
                    rt = test;
                    break;
                }
            }
		}
		return rt;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public void setRepositoryHome(String repositoryHome) {
		this.repositoryHome = repositoryHome;
	}
    
}
