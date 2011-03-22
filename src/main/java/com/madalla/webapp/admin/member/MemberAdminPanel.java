package com.madalla.webapp.admin.member;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.Member;
import com.madalla.webapp.components.member.AbstractMemberPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.wicket.ClassAppenderBehavior;
import com.madalla.wicket.animation.Animator;
import com.madalla.wicket.animation.AnimatorSubject;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class MemberAdminPanel extends AbstractMemberPanel {
	private static final long serialVersionUID = 1L;

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
	
	public abstract class MemberForm extends AjaxValidationForm<MemberData> {
		private static final long serialVersionUID = 1L;
		
		public MemberForm(String id, final IModel<MemberData> model){
			super(id, model);
			
			MarkupContainer memberDiv;
			add(memberDiv = new WebMarkupContainer("memberDiv"));
			FeedbackPanel memberIdFeedback;
			memberDiv.add(memberIdFeedback = new FeedbackPanel("memberIdFeedback"));
			TextField<String> memberIdField;
			memberDiv.add(memberIdField = new AjaxValidationRequiredTextField("memberId", memberIdFeedback));
			memberIdField.add(new AbstractValidator<String>(){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onValidate(IValidatable<String> validatable) {
					if (model.getObject().getKey() <= 0) {
						String memberId = validatable.getValue();
						if (getRepositoryService().isMemberExist(memberId)) {
							validatable.error(new ValidationError().addMessageKey("message.invalidMemberID"));
						}
					}
				}
				
			});
			memberDiv.add(getHideShowBehavior(true));
			
			MarkupContainer signupDiv;
			add(signupDiv = new WebMarkupContainer("signupDiv"));
			signupDiv.add(getHideShowBehavior(false));
			signupDiv.add(new Label("signupDate", new AbstractReadOnlyModel<String>(){
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					DateTime signupDate = model.getObject().getSignupDate() == null? null : model.getObject().getSignupDate().toDateTime(dateTimeZone);
					return signupDate == null? "" : signupDate.toString("yyyy-MM-dd HH:mm");
				}
				
			}));
			
			MarkupContainer authDateDiv;
			add(authDateDiv = new WebMarkupContainer("authDateDiv"));
			authDateDiv.add(getHideShowBehavior(false));
			authDateDiv.add(new Label("authorizedDate", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					DateTime authorizedDate = model.getObject().getAuthorizedDate() == null ? null : model.getObject().getAuthorizedDate().toDateTime(dateTimeZone);
					return authorizedDate == null? "" : authorizedDate.toString("yyyy-MM-dd HH:mm");
				}
				
			}));
			
			MarkupContainer authDiv;
			add(authDiv = new WebMarkupContainer("authDiv"));
			authDiv.add(getHideShowBehavior(false));
			authDiv.add(new CheckBox("authorized"));
			
			FeedbackPanel emailFeedback;
			add(emailFeedback = new FeedbackPanel("emailFeedback"));
			TextField<String> emailField = new AjaxValidationRequiredTextField("email", emailFeedback);
            emailField.add(EmailAddressValidator.getInstance());
            add(emailField);

			FeedbackPanel firstNameFeedback;
			add(firstNameFeedback = new FeedbackPanel("firstNameFeedback"));
			add(new AjaxValidationRequiredTextField("firstName", firstNameFeedback));
			
			FeedbackPanel lastNameFeedback;
			add(lastNameFeedback = new FeedbackPanel("lastNameFeedback"));
			add(new AjaxValidationRequiredTextField("lastName", lastNameFeedback));
			
			add(new TextField<String>("companyName"));
			
			final DateTextField subscriptionEnd;
			add(subscriptionEnd = DateTextField.forDatePattern("subscriptionEnd", "yyyy-MM-dd"));
			subscriptionEnd.add(new DatePicker(){
				private static final long serialVersionUID = 1L;

				@Override
				protected void configure(Map<String, Object> widgetProperties) {
					super.configure(widgetProperties);
					widgetProperties.put("title", Boolean.FALSE);
					widgetProperties.put("close", Boolean.FALSE);
				}
			});
			
		}
		
		private IBehavior getHideShowBehavior(boolean show){
			return new ClassAppenderBehavior(show){
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean setClass() {
					return getModelObject().getKey() == 0;
				}
				
			};
		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			boolean newUser = (getModelObject().getKey() <= 0);
			getRepositoryService().saveMember(getModelObject());
			if (newUser){
				if (sendRegistrationEmail(getModelObject())){
					info(getString("message.success.new"));
				} else {
					error(getString("message.fail.email"));
				}
			} else {
				info(getString("message.success"));
			}
			target.addComponent(MemberForm.this);
			processSubmit(target);
		}
		
		protected abstract void processSubmit(AjaxRequestTarget target);
		
	}
	
	private DateTimeZone dateTimeZone;
	private final IModel<MemberData> current;
	
	public MemberAdminPanel(String id) {
		super(id);
		
		dateTimeZone = getRepositoryService().getDateTimeZone();
		
		add(Css.CSS_ICON);
		add(JavascriptPackageResource.getHeaderContribution(JavascriptResources.PROTOTYPE));
		
		current = new CompoundPropertyModel<MemberData>(new Member());
		
		final MarkupContainer editFormDiv;
		add(editFormDiv = new WebMarkupContainer("editFormDiv"));
		editFormDiv.setOutputMarkupId(true);

		// animator to open/close profile form
		final Animator hideShowForm = new Animator().addSubjects(AnimatorSubject.slideOpen(editFormDiv.getMarkupId(), 42));
		add(hideShowForm);
		
		final MarkupContainer container ;
		add(container = new WebMarkupContainer("memberContainer"));
		container.setOutputMarkupId(true);

		final Form<MemberData> editForm;
		editFormDiv.add(editForm = new MemberForm("editForm", current){
			private static final long serialVersionUID = 1L;

			@Override
			protected void processSubmit(AjaxRequestTarget target) {
				target.addComponent(container);
				
			}
			
		});
		editForm.setOutputMarkupId(true);
		
		add(new IndicatingAjaxButton("newItem", editForm){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				current.setObject(new Member());
				target.appendJavascript("var e = $('"+ container.getMarkupId() + "'); e.select('tr').each(function(s){ s.removeClassName('selected')});");
				form.modelChanged();
				form.clearInput();
				target.appendJavascript(hideShowForm.seekToEnd());
				target.addComponent(editForm);
			}
			
		}.setDefaultFormProcessing(false));
		
		final SortableMemberProvider provider = new SortableMemberProvider();
		final DataView<MemberData> dataView = new DataView<MemberData>("memberSorting", provider){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<MemberData> item) {
				final MemberData entry = item.getModelObject();
				//item.add(new Label("id", entry.getId()));
				item.add(new Label("loginId", entry.getMemberId()));
				item.add(new Label("firstName", entry.getFirstName()));
				item.add(new Label("lastName", entry.getLastName()));
				item.add(new Label("companyName", entry.getCompanyName()));
				item.add(new Label("auth", entry.isAuthorized()? "Yes":"No"));
				item.add(new Label("subscribed", entry.isMemberSubscribed()? "Yes":"No"));
				
				item.add(new AjaxLink<Void>("selectRow") {
                    private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						current.setObject(entry);
						target.addComponent(editForm);
						target.appendJavascript("var e = $('"+ item.getMarkupId() + "'); e.adjacent('tr').each(function(s){ s.removeClassName('selected')}); e.addClassName(\"selected\");" + hideShowForm.seekToEnd());
						target.addComponent(item);
					}
                });
				
				item.add(new IndicatingAjaxLink<String>("delete", new Model<String>("")){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						getRepositoryService().deleteMember(entry);
						target.addComponent(container);
					}
					
				});
				
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<Object>() {
					private static final long serialVersionUID = 1L;

					public Object getObject() {
                        String rt =  (item.getIndex() % 2 == 1) ? "even" : "odd";
                        if (entry.equals(current.getObject())){
                        	rt = rt + " selected";
                        }
                        return rt;
                    }
                }));
				
				item.setOutputMarkupId(true);
			}

		};
		
		container.add(dataView);
		
		dataView.setItemsPerPage(10);
		
//		container.add(new OrderByBorder("orderById", "id", provider) {
//			private static final long serialVersionUID = 1L;
//
//			protected void onSortChanged() {
//				dataView.setCurrentPage(0);
//            }
//        });
		
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
