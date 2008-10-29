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

import com.madalla.webapp.blog.BlogHomePanel;
import com.madalla.webapp.blog.admin.BlogEntryPanel;
import com.madalla.webapp.cms.ContentEntryPanel;
import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.cms.admin.ContentAdminPanel;
import com.madalla.webapp.email.EmailFormPanel;
import com.madalla.webapp.images.AlbumPanel;
import com.madalla.webapp.images.admin.AlbumAdminPanel;

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
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(node)|| returnPage == null){
			error("ContentPanel - All parameters need to be supplied.");
		}
		return new ContentPanel(id, node, returnPage);
	}
	
	public static Panel contentEntryPanel(String id, PageParameters params){
		if (StringUtils.isEmpty(id) || params == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		} 
        String nodeName = getPageParameter(CONTENT_NODE, params, "ContentEntryPanel");
        String contentId = getPageParameter(CONTENT_ID, params,"ContentEntryPanel");
        Class<? extends Page> returnPage = getReturnPage(params, "ContentEntryPanel");

		return new ContentEntryPanel(id, nodeName, contentId, returnPage);
	}
	
	public static Panel contentAdminPanelForSite(String id, PageParameters params){
		if (StringUtils.isEmpty(id) || params == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		}
		return ContentAdminPanel.newInstance(id, getReturnPage(params, "ContentEntryPanel"));
	}
	
	public static Panel contentAdminPanelForAdmin(String id, PageParameters params){
		if (StringUtils.isEmpty(id) || params == null){
			error("ContentEntryPanel - All parameters need to be supplied.");
		}
		return ContentAdminPanel.newAdminInstance(id, getReturnPage(params, "ContentEntryPanel"));
	}

	/**
	 * @param id - wicket id
	 * @param blog - Name of this Blog
	 * @param returnPage - used to return from blog admin Page
	 * @param params - page parameters
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.blog.BlogHomePanel}
	 */
	public static Panel blogPanel(String id, String blog, Class<? extends Page> returnPage, PageParameters params) {
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(blog)|| returnPage == null || params == null){
			error("BlogHomePanel - All parameters need to be supplied.");
		}
		String blogEntryId = getPageParameter(BLOG_ENTRY_ID, params, "BlogHomePanel");
   		return new BlogHomePanel(id, blog, blogEntryId, returnPage);
		
	}
	
	/**
	 * @param id
	 * @param params - page parameters
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.blog.admin.BlogEntryPanel}
	 */
	public static Panel blogEntryPanel(String id, PageParameters params){
		if (StringUtils.isEmpty(id) || params == null){
			error("BlogHomePanel - All parameters need to be supplied.");
		}
		String blogName = getPageParameter(BLOG_NAME, params, "BlogEntryPanel");
		String blogEntryId = params.getString(BLOG_ENTRY_ID);
		if (StringUtils.isEmpty(blogEntryId)){
			return new BlogEntryPanel(id, blogName, getReturnPage(params, "BlogEntryPanel"));
		} else {
			return new BlogEntryPanel(id, blogName, blogEntryId, getReturnPage(params, "BlogEntryPanel"));
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
	 * @param params - pageParameters - passed on to Panel
	 * @return Instantiated Panel of Type {@link com.madalla.webapp.images.admin.AlbumAdminPanel}
	 */
	public static Panel albumAdminPanel(String id, PageParameters params){
		if (StringUtils.isEmpty(id) || params == null){
			error("AlbumAdminPanel - All parameters need to be supplied.");
		}
		String album = getPageParameter(ALBUM, params,"AlbumAdminPanel");
		return new AlbumAdminPanel(id, album, getReturnPage(params, "AlbumAdminPanel"));
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
	

	/*  Utility methods */
	private static void error(String message){
		throw new WicketRuntimeException(message);
	}
	private static void error(String message, Exception e){
		throw new WicketRuntimeException(message, e);
	}
	@SuppressWarnings("unchecked")
	private static Class<? extends Page> getReturnPage(PageParameters params, String panel){
		String pageString = getPageParameter(RETURN_PAGE, params, panel);
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
	private static String getPageParameter(String paramName, PageParameters params, String panel){
		String paramValue = params.getString(paramName);
		if (StringUtils.isEmpty(paramValue)){
			error(panel + " - A pageParameter with value '"+ paramName+"' needs to supplied.");
		}
		return paramValue;
	}
	
	
	

}
