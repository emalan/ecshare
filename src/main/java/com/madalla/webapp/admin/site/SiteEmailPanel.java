package com.madalla.webapp.admin.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.SiteData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class SiteEmailPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(SiteEmailPanel.class);
	
	private abstract class EmailForm extends AjaxValidationForm<Object> {
		private static final long serialVersionUID = 1L;

		public EmailForm(String id) {
			super(id);
			
            Component subject = new RequiredTextField<String>("subject",new PropertyModel<String>(SiteEmailPanel.this, "subject"));
            add(subject);
            subject.add(new ValidationStyleBehaviour());

            TextArea<String> body = new TextArea<String>("body",new PropertyModel<String>(SiteEmailPanel.this,"body"));
            add(body);
		}

		@Override
		protected FeedbackPanel addFeedbackPanel(){
			IFeedbackMessageFilter filter = new ContainerFeedbackMessageFilter(this);
			final FeedbackPanel formFeedback = new FeedbackPanel("formFeedback", filter);
			add(formFeedback);
			return formFeedback;
		}
		
	}
	
	private Collection<UserData> selectedUsers = new ArrayList<UserData>();
	private List<UserData> userList = new ArrayList<UserData>();
	private List<UserData> allUsers = getRepositoryService().getUsers();
	
	private String subject;
	private String body;
	
	public SiteEmailPanel(String id) {
		super(id);

		// site select
		final List<SiteData> sitesChoices = getRepositoryService().getSiteEntries();
		Component checkbox = new CheckBoxMultipleChoice<SiteData>("site", getSitesModel(),sitesChoices, new ChoiceRenderer<SiteData>("name"));
		add(checkbox);
		
		IModel<Collection<UserData>> usersModel = new PropertyModel<Collection<UserData>>(this, "selectedUsers") ;
		IModel<List<UserData>> userListModel = new PropertyModel<List<UserData>>(this, "userList") ;
		
		final ListMultipleChoice<UserData> userSelect = new ListMultipleChoice<UserData>("userSelect", usersModel, userListModel, new IChoiceRenderer<UserData>(){
					private static final long serialVersionUID = 1L;

					public Object getDisplayValue(UserData object) {
						return object.getName() + " (" 
							+ StringUtils.defaultIfEmpty(object.getFirstName()," ") 
							+ " " + StringUtils.defaultIfEmpty(object.getLastName()," ") + ")"
							+ " " + StringUtils.defaultIfEmpty(object.getEmail(), getString("label.noemail"));
					}

					public String getIdValue(UserData object, int index) {
						return object.getName();
					}
			
		});
		userSelect.setOutputMarkupId(true);
		userSelect.setMaxRows(10);
		add(userSelect);
		
		//Actions

		userSelect.add(new AjaxFormComponentUpdatingBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				log.debug("selected User Count: " + selectedUsers.size());
				
			}
			
		});

		checkbox.add(new AjaxFormChoiceComponentUpdatingBehavior(){
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Collection<SiteData> sites = (Collection<SiteData>) getComponent().getDefaultModelObject();
				
				//clear selections
				selectedUsers.clear();
				
				//mess with actual displayed list
				userList.clear();
				userList.addAll(allUsers);
				for (UserData item : allUsers){
					if (!isUserInSite(item, sites)) {
						userList.remove(item);
					}
				}
				
				//start with all selected
				selectedUsers.addAll(userList);
				
				target.addComponent(userSelect);
			}
			
		});
		
		add(new AjaxLink<String>("sendEmail"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (selectedUsers != null) {
					for (UserData item : selectedUsers){
						log.info("Test sending email. user=" + item.getName() + ", email=" + item.getEmail());
					}
				}
				
				
			}
			
		});
		
		add(new EmailForm("emailForm"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				log.debug("onSubmit - sending Emails.");
				getEmailSender().sendEmail(subject, body);
				if (selectedUsers != null){
					for (UserData user : selectedUsers) {
						if (StringUtils.isEmpty(user.getEmail())){
							log.debug("onSubmit - no email. " + user);
							error(getString("message.noemail", new Model<UserData>(user)));
						} else {
							log.debug("onSubmit - email sent. " + user);
							if (getEmailSender().sendUserEmail(subject, body, user.getEmail(), user.getFirstName())){
								info(getString("message.email.success", new Model<UserData>(user)));
							} else {
								error(getString("message.email.fail", new Model<UserData>(user)));
							}
							
						}
						
					}
				}
				
			}
		});
		

	}
	
	private boolean isUserInSite(UserData user, Collection<SiteData> sites) {

		List<UserSiteData> userSites = getRepositoryService().getUserSiteEntries(user);
		for (UserSiteData userSite : userSites){
			String site = userSite.getName();
			log.debug("isUserInSite - " + site);
			boolean found = false;
			for (SiteData siteData : sites){
				log.debug("isUserInSite - " + siteData);
				if (siteData.getName().equals(site)){
					found = true;
					break;
				}
			}
			if (found){
				return true;
			}
		}

		return false;
	}
	
	@SuppressWarnings("unchecked")
	private IModel<Collection<SiteData>> getSitesModel(){
		return new Model((Serializable) new ArrayList<SiteData>());
	}

	public void setSelectedUsers(Collection<UserData> userData) {
		this.selectedUsers = userData;
	}

	public Collection<UserData> getSelectedUsers() {
		return selectedUsers;
	}

	public void setUserList(List<UserData> userList) {
		this.userList = userList;
	}

	public List<UserData> getUserList() {
		return userList;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}
	


}
