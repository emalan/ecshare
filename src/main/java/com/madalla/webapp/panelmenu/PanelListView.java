package com.madalla.webapp.panelmenu;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class PanelListView extends ListView<PanelMenuItem>{
	private static final long serialVersionUID = -8310833021413122278L;
	
	final private String menuLinkId;
	final private String panelId;
	
	public PanelListView(final String id, final String panelId, final String menuLinkId, List<PanelMenuItem> items) {
		super(id, items);
		this.menuLinkId = menuLinkId;
		this.panelId = panelId;
	}

	@Override
	protected void populateItem(ListItem<PanelMenuItem> item) {
		final PanelMenuItem menu = item.getModelObject();
		item.add(new PanelLink(menuLinkId, panelId, menu.c, menu.key, menu.titleKey));
	}
}
