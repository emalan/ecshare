package com.madalla.webapp;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

public class MenuListView extends ListView<IMenuItem>{
	private static final long serialVersionUID = -8310833021413122278L;

	private String menuLinkId;

	public MenuListView(final String id, final String menuLinkId, final IModel<List<? extends IMenuItem>> menuListModel) {
		super(id, menuListModel);
		this.menuLinkId = menuLinkId;
	}

	@Override
	protected void populateItem(final ListItem<IMenuItem> item) {
		IMenuItem menu = item.getModelObject();
		item.add(new AppPageLink(menuLinkId, menu.getItemClass(), menu.getItemName()));
	}

}
