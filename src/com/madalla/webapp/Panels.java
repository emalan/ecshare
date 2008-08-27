package com.madalla.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.blog.BlogHomePanel;
import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.email.EmailFormPanel;

/**
 * Utlility static instantiation methods to create Wicket Panels
 * for your Pages. Provided to simplify the API by providing one
 * place for getting Panels.
 * 
 * NOTE: Your Wicket Application needs to extend the {@link CmsBlogApplication} or 
 * the {@link CmsBlogEmailApplication} before you can use these Panels
 * 
 * @author Eugene Malan
 *
 */
public class Panels {
	
	private Panels(){}
	
	/**
	 * @param id - wicket id
	 * @param node - Content Parent Node, normally Page Name
	 * @param returnPage - used to return back from editing Content
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.cms.ContentPanel}
	 */
	public static Panel contentPanel(String id, String node, Class<? extends Page> returnPage){
		return new ContentPanel(id, node, returnPage);
	}
	
	
	/**
	 * @param id - wicket id
	 * @param blog - Name of this Blog
	 * @param returnPage - used to return from blog admin Page
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.blog.BlogHomePanel}
	 */
	public static Panel blogPanel(String id, String blog, Class<? extends Page> returnPage){
		return new BlogHomePanel(id, blog, returnPage);
	}
	
	/**
	 * @param id - wicket id
	 * @param subject
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.email.EmailFormPanel}
	 */
	public static Panel emailPanel(String id, String subject){
		return new EmailFormPanel(id, subject);
	}
	

}
