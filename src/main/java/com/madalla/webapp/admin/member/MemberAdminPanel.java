package com.madalla.webapp.admin.member;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.bo.member.MemberData;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;

public class MemberAdminPanel extends CmsPanel {
	private static final long serialVersionUID = 1L;

	private final DateTimeZone dateTimeZone;
	
	private class SortableMemberProvider extends SortableDataProvider<MemberData>{
		
		private static final long serialVersionUID = 1L;
		
		public SortableMemberProvider() {
			setSort("id", true);
		}

		public Iterator<? extends MemberData> iterator(int first, int count) {
			List<? extends MemberData> list = getRepositoryService().getMemberEntries();
			if("firstName".equals(getSort().getProperty())){
				Collections.sort(list, getFirstNameComparator());
			} else if ("loginId".equals(getSort().getProperty())){
				Collections.sort(list, getMemberIdComparator());
			} else if ("companyName".equals(getSort().getProperty())){
				Collections.sort(list, getCompanyNameComparator());
			} else if ("lastName".equals(getSort().getProperty())){
				Collections.sort(list, getLastNameComparator());
			} else {
				Collections.sort(list);
			}
			if (!getSort().isAscending()){
				Collections.reverse(list);
			}
			return list.listIterator(first);
		}
		
		private Comparator<MemberData> getFirstNameComparator(){
			return new Comparator<MemberData>() {

				public int compare(MemberData o1, MemberData o2) {
					return o1.getFirstName().compareTo(o2.getFirstName());
				}
			};
		}
		
		private Comparator<MemberData> getMemberIdComparator(){
			return new Comparator<MemberData>() {

				public int compare(MemberData o1, MemberData o2) {
					return o1.getMemberId().compareTo(o2.getMemberId());
				}
			};
		}
		
		private Comparator<MemberData> getCompanyNameComparator(){
			return new Comparator<MemberData>() {

				public int compare(MemberData o1, MemberData o2) {
					return o1.getCompanyName().compareTo(o2.getCompanyName());
				}
			};
		}
		
		private Comparator<MemberData> getLastNameComparator(){
			return new Comparator<MemberData>() {

				public int compare(MemberData o1, MemberData o2) {
					return o1.getLastName().compareTo(o2.getLastName());
				}
			};
		}

		public int size() {
			return getRepositoryService().getMemberEntries().size();
		}

		public IModel<MemberData> model(MemberData object) {
			return new LoadableDetachableMemberModel(object);
		}
	}
	
	private class LoadableDetachableMemberModel extends LoadableDetachableModel<MemberData>{
		private static final long serialVersionUID = 1L;
		private String id;
		
		public LoadableDetachableMemberModel(MemberData member){
			this(member.getId());
		}
		
		public LoadableDetachableMemberModel(String id){
			if (StringUtils.isEmpty(id)){
				throw new IllegalArgumentException();
			}
			this.id = id;
		}
		
		@Override
		protected MemberData load() {
			return getRepositoryService().getMemberById(id);
		}
		
	    public int hashCode()	    {
	        return id.hashCode();
	    }

	    public boolean equals(final Object obj) {
	        if (obj == this) {
	            return true;
	        } else if (obj == null) {
	            return false;
	        } else if (obj instanceof LoadableDetachableMemberModel) {
	        	LoadableDetachableMemberModel other = (LoadableDetachableMemberModel)obj;
	            return other.id == this.id;
	        }
	        return false;
	    }
		
		
	}
	
	public MemberAdminPanel(String id) {
		super(id);
		
		add(Css.CSS_ICON);
		
		
		dateTimeZone = getRepositoryService().getDateTimeZone();
		
		final MarkupContainer container ;
		add(container = new WebMarkupContainer("memberContainer"));
		container.setOutputMarkupId(true);
		
		final SortableMemberProvider provider = new SortableMemberProvider();
		final DataView<MemberData> dataView = new DataView<MemberData>("memberSorting", provider){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<MemberData> item) {
				final MemberData entry = item.getModelObject();
				item.add(new Label("id", entry.getId()));
				item.add(new Label("loginId", entry.getMemberId()));
				item.add(new Label("firstName", entry.getFirstName()));
				item.add(new Label("lastName", entry.getLastName()));
				item.add(new Label("companyName", entry.getCompanyName()));
				DateTime dateTime = entry.getSignupDate().toDateTime(dateTimeZone);
				item.add(new Label("date", dateTime.toString("yyyy-MM-dd")));
				item.add(new Label("auth", entry.isAuthorized()? "Yes":"No"));
				item.add(new IndicatingAjaxLink<String>("delete", new Model<String>("")){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						getRepositoryService().deleteMember(entry);
						target.addComponent(container);
					}
					
				});
				item.add(new Link<Void>("memberEdit"){

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						getPage().replace(new MemberProfilePanel("adminPanel", entry));
						
					}
					
				});
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<Object>() {
					private static final long serialVersionUID = 1L;

					public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
				
			}

		};
		
		container.add(dataView);
		
		dataView.setItemsPerPage(10);
		
		container.add(new OrderByBorder("orderById", "id", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
				dataView.setCurrentPage(0);
            }
        });
		
		container.add(new OrderByBorder("orderByLoginId", "loginId", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
				dataView.setCurrentPage(0);
            }
        });
		
		container.add(new OrderByBorder("orderByCompanyName", "companyName", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
				dataView.setCurrentPage(0);
            }
        });
		
		container.add(new OrderByBorder("orderByFirstName", "firstName", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
				dataView.setCurrentPage(0);
            }
        });

		container.add(new OrderByBorder("orderByLastName", "lastName", provider) {
			private static final long serialVersionUID = 1L;

			protected void onSortChanged() {
				dataView.setCurrentPage(0);
            }
        });

		
		
		add(new PagingNavigator("navigator", dataView));
		
	}

}
