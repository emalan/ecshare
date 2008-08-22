package com.madalla.webapp.blog.admin;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;
import com.madalla.webapp.blog.IBlogAware;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;
import com.madalla.wicket.ValidationStyleBehaviour;

public class BlogEntryPanel extends Panel implements IBlogAware{
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(BlogEntryPanel.class, "BlogEntryPanel.js");
    private Log log = LogFactory.getLog(this.getClass());
    private BlogEntry blogEntry;
    private Class<?> returnPage;
    
    public BlogEntryPanel(String id, final PageParameters parameters) {
        super(id);
        String returnPageName = parameters.getString(RETURN_PAGE);
        try {
            this.returnPage = Class.forName(returnPageName);
        } catch (ClassNotFoundException e) {
            log.error("constructor - Exception while getting return Class.", e);
        }
        add(HeaderContributor.forJavaScript(TinyMce.class,"tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));

        //construct blog Entry
        String blogEntryId = parameters.getString(BLOG_ENTRY_ID);
        log.debug("Constructing Blog Entry. id="+blogEntryId);
        if (StringUtils.isEmpty(blogEntryId)){
            blogEntry = new BlogEntry();
            log.debug("Created new Blog Entry");
        } else {
            blogEntry = getBlogService().getBlogEntry(blogEntryId);
            log.debug("Retrieved Blog Entry from Service."+blogEntry);
        }
        
        add(new BlogEntryForm("blogForm",this));
    }
    
    final class BlogEntryForm extends Form{
        private static final long serialVersionUID = 1L;
        
        public BlogEntryForm(final String name, Component panel) {
            super(name);

            final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
            feedbackPanel.setOutputMarkupId(true);
            add(feedbackPanel);
            
            //Date
            blogEntry.setDate(Calendar.getInstance().getTime());
            DateTextField dateTextField = new DateTextField("dateTextField", new PropertyModel(blogEntry,"date"), new StyleDateConverter("S-",true));
            dateTextField.setRequired(true);
            dateTextField.add(new ValidationStyleBehaviour());
            dateTextField.add(new AjaxFormComponentUpdatingBehavior("onblur"){
            	protected void onUpdate(AjaxRequestTarget target) {
					target.addComponent(getFormComponent());
				}
            });
            dateTextField.setLabel(new Model(panel.getString("label.date")));
            add(dateTextField);
            dateTextField.add(new DatePicker());

            //category drop down
            List categories = getBlogService().getBlogCategories();
            FormComponent categoryDropDown = new DropDownChoice("category", new PropertyModel(blogEntry,"blogCategory"), categories);
            categoryDropDown.setRequired(true);
            categoryDropDown.add(new ValidationStyleBehaviour());
            categoryDropDown.add(new AjaxFormComponentUpdatingBehavior("onblur"){
            	protected void onUpdate(AjaxRequestTarget target){
            		target.addComponent(getFormComponent());
            	}
            });
            categoryDropDown.setLabel(new Model(panel.getString("label.category")));
            add(categoryDropDown);

            TextField title = new TextField("title",new PropertyModel(blogEntry,"title"));
            title.add(new ValidationStyleBehaviour());
            title.setRequired(true);
            title.setLabel(new Model(panel.getString("label.title")));
            title.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				protected void onUpdate(AjaxRequestTarget target) {
					target.addComponent(getFormComponent());
					target.addComponent(feedbackPanel);
				}
            });
            add(title);
            
            add(new TextArea("description",new PropertyModel(blogEntry,"description")).setConvertEmptyInputStringToNull(false));
            add(new TextField("keywords",new PropertyModel(blogEntry,"keywords")).setConvertEmptyInputStringToNull(false));
            
            add(new TextArea("text", new PropertyModel(blogEntry, "text")));
            
            Button saveButton = new Button("saveButton");
            setDefaultButton(saveButton);
            add(saveButton);
            
            Button cancelButton = new Button("cancelButton"){
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					
					setResponsePage(returnPage);
					
				}
            };
            cancelButton.setDefaultFormProcessing(false);
            add(cancelButton);
            
            
            
        }
        
        public void onSubmit() {
            log.debug("onSubmit - Saving populated Blog Entry to Blog service. " + blogEntry);
            try {
                getBlogService().saveBlogEntry(blogEntry);
                info("Success");
                log.info("Blog Entry successfully saved. " + blogEntry);
                setResponsePage(returnPage);
            } catch (Exception e) {
                info("There was a problem saving Entry.");
                info(e.getMessage());
                log.error("Exception while saving entry to blog service.", e);
            }
        }

    }
    
    private IBlogService getBlogService(){
    	return ((IBlogServiceProvider)getApplication()).getBlogService();
    }

}
