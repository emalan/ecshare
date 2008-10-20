package com.madalla.service.cms.jcr;

import com.madalla.service.cms.IRepositoryData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

public class ContentHelper extends AbstractContentHelper {
	
	private static final Log log = LogFactory.getLog(ContentHelper.class);
	private static final String DEFAULT_CONTENT = "Content";
	
	// Repository Values
    static final String EC_NODE_PAGES = NS + "pages";
    static final String EC_NODE_CONTENT = NS + "content";
    
	private static ContentHelper instance;
	public static ContentHelper getInstance(){
		return instance;
	}

	public ContentHelper(String site, JcrTemplate template ){
		this.site = site;
		this.template = template;
		instance = this;
	}
	
    public static boolean isContentNode(final String path){
    	String[] pathArray = path.split("/");
        return EC_NODE_CONTENT.equals(pathArray[pathArray.length - 2]);
    	}
    
    public static boolean isContentPasteNode(final String path){
    	String[] pathArray = path.split("/");
        return EC_NODE_CONTENT.equals(pathArray[pathArray.length - 1]);
    }

    //TODO use generic Save method
    public String save(final Content content){
    	log.info("save - "+content);
        return (String) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
            	log.debug("processEntry - " + this);
                Node node ;
                if (StringUtils.isEmpty(content.getId())){
                	Node siteNode = getSiteNode(session);
                	Node parent = getCreateNode(EC_NODE_PAGES, siteNode);
                	Node groupNode = getCreateNode(NS+content.getGroup(), parent);
               		Node contentNode = getCreateNode(EC_NODE_CONTENT, groupNode);

               		node = getCreateNode(NS+content.getName(), contentNode);
                } else {
                    log.debug("processEntry - retrieving node by path. path="+content.getId());
                    node = (Node) session.getItem(content.getId());
                }
                node.setProperty(EC_PROP_CONTENT, content.getText());
                session.save();
                return node.getPath();

            }
        });
    }
    
    public String getData(final String group, final String name) {
    	return (String) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node siteNode = getSiteNode(session);
            	Node parent = getCreateNode(EC_NODE_PAGES, siteNode);
            	Node groupNode = getCreateNode(NS+group, parent);
           		Node contentNode = getCreateNode(EC_NODE_CONTENT, groupNode);
           		Node node = getCreateNode(NS+ name, contentNode);
           		if (node.isNew()){
           			node.setProperty(EC_PROP_CONTENT, DEFAULT_CONTENT);
           			session.save();
           			return DEFAULT_CONTENT;
           		} else {
           			return node.getProperty(EC_PROP_CONTENT).getString();
           		}
			}
    	});
    }
    
	@Override
	Node getParentNode(Node node) throws RepositoryException {
		return null;
	}

	@Override
	void setPropertyValues(Node node, IRepositoryData data)
			throws RepositoryException {
	}

}
