package com.madalla.webapp;

import static com.madalla.webapp.blog.BlogParameters.BLOG_ENTRY_ID;
import static com.madalla.webapp.blog.BlogParameters.BLOG_NAME;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_ID;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_NODE;
import static com.madalla.webapp.images.admin.AlbumParams.ALBUM;
import static com.madalla.webapp.images.admin.AlbumParams.RETURN_PAGE;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.bo.page.ResourceType;
import com.madalla.webapp.blog.BlogHomePanel;
import com.madalla.webapp.blog.admin.BlogEntryPanel;
import com.madalla.webapp.cms.ContentEntryPanel;
import com.madalla.webapp.cms.ContentLinkPanel;
import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.cms.InlineContentPanel;
import com.madalla.webapp.cms.admin.ContentAdminPanel;
import com.madalla.webapp.email.EmailFormPanel;
import com.madalla.webapp.images.AlbumPanel;
import com.madalla.webapp.images.admin.AlbumAdminPanel;
import com.madalla.webapp.user.UserAdminPanel;
import com.madalla.webapp.user.UserLoginPanel;
import com.madalla.webapp.user.UserProfilePanel;

/**
 * Utility static instantiation methods to create Wicket Panels
 * for your Pages. Provided to simplify the API by providing one
 * place for getting Panels.
 * 
 * NOTE: Your Wicket Application needs to extend {@link CmsApplication} before you can use these Panels
 * 
 * @author Eugene Malan
 *
 */
public class Panels {
	
	private Panels(){}
	
	public static Panel contentLinkPanelPdf(String id, String node){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new ContentLinkPanel(id, node, ResourceType.TYPE_PDF);
	}
	
	public static Panel contentLinkPanelDoc(String id, String node){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new ContentLinkPanel(id, node, ResourceType.TYPE_DOC);
	}
	
	public static Panel contentLinkPanelOdt(String id, String node){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new ContentLinkPanel(id, node, ResourceType.TYPE_ODT);
	}

	/**
	 * @param id - wicket id
	 * @param node - Content Parent Node, normally Page Name
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.cms.InlineContentPanel}
	 */
	public static Panel contentInlinePanel(String id, String node){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new InlineContentPanel(id, node);
	}
	
	/**
	 * @param id - wicket id
	 * @param node - Content Parent Node, normally Page Name
	 * @param returnPage - used to return back from editing Content
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.cms.ContentPanel}
	 */
	public static Panel contentPanel(String id, String node, Class<? extends Page> returnPage){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)|| returnPage == null){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new ContentPanel(id, node, returnPage);
	}
	
	public static Panel contentEntryPanel(String id, PageParameters parameters){
		if (StringUtils.isEmpty(id) || parameters == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		} 
        String nodeName = getPageParameter(CONTENT_NODE, parameters, "ContentEntryPanel");
        String contentId = getPageParameter(CONTENT_ID, parameters,"ContentEntryPanel");
        Class<? extends Page> returnPage = getReturnPage(parameters, "ContentEntryPanel");

		return new ContentEntryPanel(id, nodeName, contentId, returnPage);
	}
	
	public static Panel contentAdminPanelForSite(String id, PageParameters parameters){
		if (StringUtils.isEmpty(id) || parameters == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		}
		return ContentAdminPanel.newInstance(id, getReturnPage(parameters, "ContentEntryPanel"));
	}
	
	public static Panel contentAdminPanelForAdmin(String id, PageParameters parameters){
		if (StringUtils.isEmpty(id) || parameters == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		}
		return ContentAdminPanel.newAdminInstance(id, getReturnPage(parameters, "ContentEntryPanel"));
	}

	/**
	 * @param id - wicket id
	 * @param blog - Name of this Blog
	 * @param returnPage - used to return from blog admin Page
	 * @param parameters - page parameters
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.blog.BlogHomePanel}
	 */
	public static Panel blogPanel(String id, String blog, Class<? extends Page> returnPage, PageParameters parameters) {
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(blog)|| returnPage == null || parameters == null){
			error("BlogHomePanel - All parameters need to be supplied.");
		}
		String blogEntryId = parameters.getString(BLOG_ENTRY_ID);
   		return new BlogHomePanel(id, blog, blogEntryId, returnPage);
		
	}
	
	/**
	 * @param id - wicket id
	 * @param parameters - page parameters including Blog name and return page
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.blog.admin.BlogEntryPanel}
	 */
	public static Panel blogEntryPanel(String id, PageParameters parameters){
		if (StringUtils.isEmpty(id) || parameters == null){
			error("BlogHomePanel - All parameters need to be supplied.");
		}
		String blogName = getPageParameter(BLOG_NAME, parameters, "BlogEntryPanel");
		String blogEntryId = parameters.getString(BLOG_ENTRY_ID);
		if (StringUtils.isEmpty(blogEntryId)){
			return new BlogEntryPanel(id, blogName, getReturnPage(parameters, "BlogEntryPanel"));
		} else {
			return new BlogEntryPanel(id, blogName, blogEntryId, getReturnPage(parameters, "BlogEntryPanel"));
		}
	}
	
	/**
	 * @param id - wicket id
	 * @param subject - this will end up as subject in the email
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.email.EmailFormPanel}
	 */
	public static Panel emailPanel(String id, String subject){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(subject)){
			error("EmailFormPanel - All parameters need to be supplied.");
		}
		return new EmailFormPanel(id, subject);
	}
	
	/**
	 * @param id - wicket id
	 * @param parameters - pageParameters - passed on to Panel
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.images.admin.AlbumAdminPanel}
	 */
	public static Panel albumAdminPanel(String id, PageParameters parameters){
		if (StringUtils.isEmpty(id) || parameters == null){
			error("AlbumAdminPanel - All parameters need to be supplied.");
		}
		String album = getPageParameter(ALBUM, parameters,"AlbumAdminPanel");
		return new AlbumAdminPanel(id, album, getReturnPage(parameters, "AlbumAdminPanel"));
	}
	
	/**
	 * @param id - wicket id
	 * @param album
	 * @param returnPage - used to create return link
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.images.AlbumPanel}
	 */
	public static Panel albumPanel(String id, String album, Class<? extends Page> returnPage){
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(album) || returnPage == null){
			error("AlbumPanel - All parameters need to be supplied.");
		}
		return new AlbumPanel(id, album, returnPage);
	}
	
	/**
	 * User Login Panel - with login and lost password functionality
	 * @param id - wicket id
	 * @param parameters - including return Page
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.user.UserLoginPanel}
	 */	
	public static Panel userLoginPanel(String id, PageParameters parameters) {
		if (StringUtils.isEmpty(id) || parameters == null){
			error("UserLoginPanel - All parameters need to be supplied.");
		}
		return new UserLoginPanel(id, getReturnPage(parameters, "UserLoginPanel"));
	}

	/**
	 * User Profile Panel - change user info, reset password
	 * @param id - wicket id
	 * @param parameters - including return Page
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.user.UserProfilePanel}
	 */	
	public static Panel userProfilePanel(String id, PageParameters parameters) {
		if (StringUtils.isEmpty(id) || parameters == null){
			error("UserProfilePanel - All parameters need to be supplied.");
		}
		return new UserProfilePanel(id, getReturnPage(parameters, "UserProfilePanel"));
	}
	
	/**
	 * User Admin Panel - create new user, edit existing user, reset password, send welcome email
	 * @param id - wicket id
	 * @param parameters - including return Page
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.user.UserAdminPanel}
	 */	
	public static Panel userAdminPanel(String id, PageParameters parameters) {
		if (StringUtils.isEmpty(id) || parameters == null){
			error("UserAdminPanel - All parameters need to be supplied.");
		}
		return new UserAdminPanel(id, getReturnPage(parameters, "UserAdminPanel"));
	}
	
	/*  Utility methods */
	private static void error(String message){
		throw new WicketRuntimeException(message);
	}
	private static void error(String message, Exception e){
		throw new WicketRuntimeException(message, e);
	}
	@SuppressWarnings("unchecked")
	private static Class<? extends Page> getReturnPage(PageParameters parameters, String panel){
		String pageString = getPageParameter(RETURN_PAGE, parameters, panel);
		Class returnPage = null;
		try {
            returnPage = Class.forName(pageString);
            returnPage.asSubclass(Page.class);
        } catch (ClassNotFoundException e) {
        	error(panel + " - ClassNotFoundException while getting return Class from PageParameters.", e);
        } catch (ClassCastException e){
        	error(panel + " - The Class Type of the PageParameter '"+RETURN_PAGE+"' needs to extend the Page class.");
        }
        return returnPage;
	}
	private static String getPageParameter(String paramName, PageParameters parameters, String panel){
		String paramValue = parameters.getString(paramName);
		if (StringUtils.isEmpty(paramValue)){
			error(panel + " - A pageParameter with value '"+ paramName+"' needs to supplied.");
		}
		return paramValue;
	}

}
