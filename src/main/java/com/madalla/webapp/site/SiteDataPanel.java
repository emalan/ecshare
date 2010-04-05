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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.bo.email.EmailEntryData;
import com.madalla.bo.log.LogData;
import com.madalla.webapp.panel.CmsPanel;

public class SiteDataPanel extends CmsPanel {

	private static final long serialVersionUID = 1L;
	
	private final DateTimeZone dateTimeZone;
	
	private class SortableEmailEntryProvider extends SortableDataProvider<EmailEntryData> {

		private static final long serialVersionUID = 1L;
		
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
	
	private class SortableLogProvider extends SortableDataProvider<LogData> {

		private static final long serialVersionUID = 1L;
		
		SortableLogProvider(){
			setSort("id", true);
		}

		public Iterator<? extends LogData> iterator(int first, int count) {
			List<LogData> list = getRepositoryService().getTransactionLogEntries();
			if ("user".equals(getSort().getProperty())){
				Collections.sort(list, getUserComparator());
			} else {
				Collections.sort(list);
			}
			if (!getSort().isAscending()){
				Collections.reverse(list);
			}
			return list.listIterator(first);
		}
		
		private Comparator<LogData> getUserComparator(){
			return new Comparator<LogData>(){

				public int compare(LogData o1, LogData o2) {
					return o1.getUser().compareTo(o2.getUser());
				}
				
			};
		}

		public int size() {
			return getRepositoryService().getTransactionLogEntries().size();
		}

		public IModel<LogData> model(LogData object) {
			return new LoadableDetachableLogModel(object);
		}
		
	}
	
	private class LoadableDetachableLogModel extends LoadableDetachableModel<LogData>{

		private static final long serialVersionUID = 1L;
		private String id;
		
		public LoadableDetachableLogModel(LogData entry) {
			this(entry.getId());
		}
		
		public LoadableDetachableLogModel(String id){
			if (StringUtils.isEmpty(id)){
				throw new IllegalArgumentException();
			}
			this.id = id;
		}

		@Override
		protected LogData load() {
			return getRepositoryService().getTransactionLog(id);
		}
		
	    public int hashCode()	    {
	        return id.hashCode();
	    }

	    public boolean equals(final Object obj) {
	        if (obj == this) {
	            return true;
	        } else if (obj == null) {
	            return false;
	        } else if (obj instanceof LoadableDetachableLogModel) {
	        	LoadableDetachableLogModel other = (LoadableDetachableLogModel)obj;
	            return other.id == this.id;
	        }
	        return false;
	    }
		
	}
	
	public SiteDataPanel(String id) {
		super(id);
		
		dateTimeZone = getRepositoryService().getDateTimeZone();
		
		SortableEmailEntryProvider provider = new SortableEmailEntryProvider();
		final DataView<EmailEntryData> dataView = new DataView<EmailEntryData>("emailSorting", provider){

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<EmailEntryData> item) {
				//item.add(new Label("emailId", item.getId()));
				EmailEntryData emailEntry = item.getModelObject();
				DateTime dateTime = emailEntry.getDateTime().toDateTime(dateTimeZone);
				item.add(new Label("date", dateTime.toString("yyyy-MM-dd")));
				item.add(new Label("time", dateTime.toString("HH:mm:ss")));
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
		
		add(new PagingNavigator("emailNavigator", dataView));
		
		//////////////////////
		// Transaction logs
		//////////////////////
		SortableLogProvider logProvider = new SortableLogProvider();
		final DataView<LogData> logView = new DataView<LogData>("logSorting", logProvider){

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<LogData> item) {
				LogData data = item.getModelObject();
				DateTime dateTime = data.getDateTime().toDateTime(dateTimeZone);
				item.add(new Label("date", dateTime.toString("yyyy-MM-dd")));
				item.add(new Label("time", dateTime.toString("HH:mm:ss")));
				item.add(new Label("user", data.getUser()));
				item.add(new Label("type", data.getType()));
				item.add(new Label("cmsId", data.getCmsId()));
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<Object>() {
					private static final long serialVersionUID = 1L;

					public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
				
			}
			
		};
		
		add(new OrderByBorder("orderByDateTime1", "id", logProvider, NumericCssProvider.instance) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
                logView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByUser", "user", logProvider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
                logView.setCurrentPage(0);
            }
        });
		
		logView.setItemsPerPage(10);
		
		add(logView);
		add(new PagingNavigator("logNavigator", logView));
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
