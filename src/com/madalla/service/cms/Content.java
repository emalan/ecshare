package com.madalla.service.cms;

import java.io.Serializable;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

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
public class Content  implements IRepositoryData, Serializable {
	private static final long serialVersionUID = 1228074714351585867L;
	private static final Log log = LogFactory.getLog(Content.class);

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
    
    public String save(){
    	return ContentHelper.getInstance().save(this);
    }

    //this is the get method
    public String processEntry(Session session, IRepositoryService service) throws RepositoryException{
    	return null;
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
