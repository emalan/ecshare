package com.madalla.wicket;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class LabelPagingNavigator extends AjaxPagingNavigator{

	private static final long serialVersionUID = 1L;
	
	public LabelPagingNavigator(String id, IPageable pageable) {
		super(id, pageable);
		
	}
	
	
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.append("class", "navigator"," ");
		super.onComponentTag(tag);
	}



	@Override
	protected Link<?> newPagingNavigationIncrementLink(final String id, IPageable pageable, int increment) {
		
		return new AjaxPagingNavigationIncrementLink(id, pageable, increment){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				if (id.equals("prev")){
					replaceComponentTagBody(markupStream, openTag, getString("navigator.prev"));
				} else if (id.equals("next")){
					replaceComponentTagBody(markupStream, openTag, getString("navigator.next"));
				}
			}
			
		};
	}

	@Override
	protected Link<?> newPagingNavigationLink(final String id, IPageable pageable, int pageNumber) {
		
		return new AjaxPagingNavigationLink(id, pageable, pageNumber){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				if (id.equals("first")){
					replaceComponentTagBody(markupStream, openTag, getString("navigator.first"));
				} else if (id.equals("last")){
					replaceComponentTagBody(markupStream, openTag, getString("navigator.last"));
				}
				
			}
		};

	}

}
