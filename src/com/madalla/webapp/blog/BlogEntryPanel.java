package com.madalla.webapp.blog;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

public class BlogEntryPanel extends Panel implements IBlogAware{
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(BlogEntryPanel.class, "BlogEntryPanel.js");
    private Log log = LogFactory.getLog(this.getClass());
    private final IBlogService service;
    private BlogEntry blogEntry;
    
    public BlogEntryPanel(String id, final PageParameters parameters, IBlogService service) {
        super(id);
        this.service = service;
        add(HeaderContributor.forJavaScript(TinyMce.class,"tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));

        //construct blog Entry
        int blogEntryId = parameters.getInt(BLOG_ENTRY_ID);
        log.debug("Constructing Blog Entry. id="+blogEntryId);
        if (blogEntryId > 0){
            blogEntry = service.getBlogEntry(blogEntryId);
            log.debug("Retrieved Blog Entry from Service."+blogEntry);
        } else {
            blogEntry = new BlogEntry();
            log.debug("Created new Blog Entry");
        }
        
        add(new BlogEntryForm("blogForm"));
    }
    
    final class BlogEntryForm extends Form{
        private static final long serialVersionUID = 1L;
        
        public BlogEntryForm(final String name) {
            super(name);
            add(new TextArea("text", new PropertyModel(blogEntry, "text")));
            
            //category drop down
            List categories = service.getBlogCategories();
            FormComponent categoryDropDown = new DropDownChoice("category", new PropertyModel(blogEntry,"blogCategory"), categories, new ChoiceRenderer("name","id"));
            add(categoryDropDown);
            
            //Date
            DateTextField dateTextField = new DateTextField("dateTextField", new PropertyModel(blogEntry,"date"), new StyleDateConverter("S-",true));
            add(dateTextField);
            dateTextField.add(new DatePicker());
            
            add(new FeedbackPanel("feedback"));
        }
        
        public void onSubmit() {
            log.debug("onSubmit - Saving populated Blog Entry to Blog service. " + blogEntry);
            try {
                service.saveBlogEntry(blogEntry);
            } catch (Exception e) {
                info("There was a problem saving Entry. " + e.getMessage());
                log.error("Exception while saving entry to blog service.", e);
            }
            info("Blog Entry saved to repository");
            log.info("Blog Entry successfully saved. " + blogEntry);
        }

    }



}
