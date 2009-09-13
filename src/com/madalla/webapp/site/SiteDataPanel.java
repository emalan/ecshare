package com.madalla.webapp.site;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.CssProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.madalla.bo.email.EmailEntryData;
import com.madalla.webapp.panel.CmsPanel;

public class SiteDataPanel extends CmsPanel {

	private static final long serialVersionUID = 1L;
	
	private class SortableEmailEntryProvider extends SortableDataProvider<EmailEntryData> {

		private static final long serialVersionUID = 1L;
		//private List<EmailEntryData> list;
		
		SortableEmailEntryProvider(){
			setSort("id", true);
		}

		public Iterator<? extends EmailEntryData> iterator(int first, int count) {
			List<EmailEntryData> list = getRepositoryService().getEmailEntries();
			if ("senderName".equals(getSort().getProperty())){
				Collections.sort(list, getNameComparator());
			} else {
				Collections.sort(list);
			}
			if (!getSort().isAscending()){
				Collections.reverse(list);
			}
			return list.listIterator(first);
		}
		
		private Comparator<EmailEntryData> getNameComparator(){
			return new Comparator<EmailEntryData>(){

				public int compare(EmailEntryData o1, EmailEntryData o2) {
					return o1.getSenderName().compareTo(o2.getSenderName());
				}
				
			};
		}

		public IModel<EmailEntryData> model(EmailEntryData object) {
			return new LoadableDetachableEmailEntryModel((EmailEntryData)object);
		}

		public int size() {
			return getRepositoryService().getEmailEntries().size();
		}
		
	}
	
	private class LoadableDetachableEmailEntryModel extends LoadableDetachableModel<EmailEntryData>{

		private static final long serialVersionUID = 1L;
		private String id;
		
		public LoadableDetachableEmailEntryModel(EmailEntryData emailEntry) {
			this(emailEntry.getId());
		}
		
		public LoadableDetachableEmailEntryModel(String id){
			if (StringUtils.isEmpty(id)){
				throw new IllegalArgumentException();
			}
			this.id = id;
		}

		@Override
		protected EmailEntryData load() {
			return getRepositoryService().getEmailEntry(id);
		}
		
	    public int hashCode()	    {
	        return id.hashCode();
	    }

	    public boolean equals(final Object obj) {
	        if (obj == this) {
	            return true;
	        } else if (obj == null) {
	            return false;
	        } else if (obj instanceof LoadableDetachableEmailEntryModel) {
	        	LoadableDetachableEmailEntryModel other = (LoadableDetachableEmailEntryModel)obj;
	            return other.id == this.id;
	        }
	        return false;
	    }
		
	}
	
	public SiteDataPanel(String id) {
		super(id);
		//List<EmailEntryData> list = getRepositoryService().getEmailEntries();
		//log.debug("Retrieve email data."+ list.size() + "entries");
		SortableEmailEntryProvider provider = new SortableEmailEntryProvider();
		final DataView<EmailEntryData> dataView = new DataView<EmailEntryData>("sorting", provider){

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<EmailEntryData> item) {
				//item.add(new Label("emailId", item.getId()));
				EmailEntryData emailEntry = item.getModelObject();
				item.add(new Label("date", emailEntry.getDateTime().toString("yyyy-MM-dd")));
				item.add(new Label("time", emailEntry.getDateTime().toString("HH:mm:ss")));
				item.add(new Label("name", emailEntry.getSenderName()));
				item.add(new Label("email", emailEntry.getSenderEmailAddress()));
				item.add(new Label("comment", emailEntry.getSenderComment()));
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<Object>() {
					private static final long serialVersionUID = 1L;

					public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
				
			}
			
		};
		
		dataView.setItemsPerPage(10);
		
		add(new OrderByBorder("orderByDateTime", "id", provider, NumericCssProvider.instance) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByName", "senderName", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });
		
		add(dataView);
		
		add(new PagingNavigator("navigator", dataView));
	}
	
	/**
	 * Default implementation of ICssProvider
	 * 
	 * @author Igor Vaynberg ( ivaynberg )
	 */
	public static class NumericCssProvider extends CssProvider
	{
		private static final long serialVersionUID = 1L;

		private static NumericCssProvider instance = new NumericCssProvider();

		private NumericCssProvider()
		{
			super("wicket_orderUp_n", "wicket_orderDown_n", "wicket_orderNone");
		}

		/**
		 * @return singleton instance
		 */
		public static NumericCssProvider getInstance()
		{
			return instance;
		}
	}


}
