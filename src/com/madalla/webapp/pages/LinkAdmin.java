package com.madalla.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.Link;

import com.madalla.webapp.AdminPage;

/**
 * Link to Admin Page
 * 
 * @author Eugene Malan
 *
 */
public class LinkAdmin extends Link<Object>{

	private static final long serialVersionUID = 1L;
	private final Class<? extends AdminPage> adminPageClass;

	/**
	 * @param id - wicket id
	 * @param adminPageClass - The Admin Page Class 
	 */
	public LinkAdmin(String id, Class<? extends AdminPage> adminPageClass){
		super(id);
		this.adminPageClass = adminPageClass;
		
	}
	@Override
	public void onClick() {
		AdminPage adminPage = constructAdminPage(adminPageClass);
		adminPage.setReturnPage(getReturnPageClass());
		setResponsePage(adminPage);
		
	}
	
	protected Class<? extends Page> getReturnPageClass(){
		Page page = getPage();
		if (page instanceof AdminPage){
			return ((AdminPage)page).getReturnPage();
		} else {
			return page.getPageClass();
		}
	}
	
	protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz){
		return (AdminPage) getSession().getPageFactory().newPage(clazz);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("rel", "nofollow");
		super.onComponentTag(tag);
	}
	

}
