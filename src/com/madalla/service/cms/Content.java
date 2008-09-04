package com.madalla.service.cms;

import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Content Text data stored in JCR Content Repository
 * <p>
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. This class is responsible
 * for storing itself in the Repository and fetching itself. 
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 *                       ------------------------------------------------------------
 *                      |                         |                                  |
 *                    ec:pages                 ec:blogs                           ec:images
 *              --------|----------              
 *             |        |          |             
 *        [ec:page1] [ec:page2] [ec:page3]
 *             |        |          |      
 *                   ec:content           
 *       ---------------|-----------      
 *      |               |           |
 * [ec:para1]   [ec:para2]    [ec:block1]
 * 
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class Content extends AbstractContentData implements Serializable {
	private static final long serialVersionUID = 1228074714351585867L;
	private static final Log log = LogFactory.getLog(Content.class);

	// Repository Values
    static final String EC_NODE_PAGES = NS + "pages";
    static final String EC_NODE_CONTENT = NS + "content";


	private final String pageName;
	private String name;
    private final String id;
    private String text;
    
    /**
     * @param pageName
     * @param contentId
     */
    public Content(final String pageName, final String contentName){
    	this.id = "";
    	this.pageName = pageName;
    	this.name = contentName;
    }
    public Content(final String id, final String pageName, final String contentName){
    	this.id = id;
    	this.pageName = pageName;
    	this.name = contentName;
    }
    
    public String processEntry(Session session, IRepositoryService service) throws RepositoryException{
    	log.debug("processEntry - " + this);
        Node node ;
        if (StringUtils.isEmpty(id)){
        	Node siteNode = service.getSiteNode(session);
        	Node parent = service.getCreateNode(EC_NODE_PAGES, siteNode);
        	Node groupNode = service.getCreateNode(NS+getGroup(), parent);
       		Node contentNode = service.getCreateNode(EC_NODE_CONTENT, groupNode);

       		node = service.getCreateNode(NS+getName(), contentNode);
        } else {
            log.debug("processEntry - retrieving node by path. path="+getId());
            node = (Node) session.getItem(getId());
        }
        setText(node.getProperty(EC_PROP_CONTENT).getString());
        session.save();
        return node.getPath();
    }
    
    public String getPageName() {
        return pageName;
    }
    public String getId() {
        return id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
	public String getGroup() {
		return pageName;
	}
	public String getName() {
		return name;
	}
    
    
    
}
