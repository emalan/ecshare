package com.madalla.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.blog.BlogHomePanel;
import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.email.EmailFormPanel;

public class Panels {
	
	private Panels(){}
	
	/**
	 * Note: your application needs to extend the CmsBlogApplication
	 * @param id - wicket id
	 * @param node - Content Parent Node, normally Page Name
	 * @param returnPage - used to return back from editing Content
	 * @param contentEditPage - used to link to the Content Edit Page
	 * @param contentAdmin - used to link to the Content Admin Page
	 * @return
	 */
	public static Panel contentPanel(String id, String node, Class<? extends Page> returnPage){
		return new ContentPanel(id, node, returnPage);
	}
	
	
	/**
	 * Note: your application needs to extend the CmsBlogApplication
	 * @param id - wicket id
	 * @param blog - Name of this Blog
	 * @param returnPage - used to return from blog admin Page
	 * @return
	 */
	public static Panel blogPanel(String id, String blog, Class<? extends Page> returnPage){
		return new BlogHomePanel(id, blog, returnPage);
	}
	
	/**
	 * Note: your application needs to extend the CmsBlogEmailApplication
	 * @param id - wicket id
	 * @param subject
	 * @param emailSender
	 * @return
	 */
	public static Panel emailPanel(String id, String subject){
		return new EmailFormPanel(id, subject);
	}
	

}
