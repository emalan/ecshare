package com.madalla.webapp;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class MenuListView extends ListView<IMenuItem>{
	private static final long serialVersionUID = -8310833021413122278L;
	
	private String menuLinkId;
	
	public MenuListView(final String id, final String menuLinkId, List<IMenuItem> items) {
		super(id, items);
		this.menuLinkId = menuLinkId;
	}

	@Override
	protected void populateItem(ListItem<IMenuItem> item) {
		IMenuItem menu = item.getModelObject();
		item.add(new AppPageLink(menuLinkId, menu.getItemClass(), menu.getItemName()));
	}		
	
	@Override
	protected void onBeforeRender(){
		super.onBeforeRender();
	}
	
	
	
}
