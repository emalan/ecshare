package com.madalla.webapp.cms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.emalan.cms.bo.page.ContentData;
import org.emalan.cms.bo.page.ContentEntryData;
import org.emalan.cms.bo.page.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.CmsPanel;

/**
 * In-Line Edit Panel - allows in-line editing of Content when logged in.
 *
 * @author Eugene Malan
 *
 */
public class InlineContentPanel extends CmsPanel {
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Use this constructor for Border or application Level Content
     *
     * @param id
     */
    public InlineContentPanel(final String id) {
    	this(id, APPNODE);
    }
    /**
     *
     * @param id
     * @param nodeName
     */
    public InlineContentPanel(final String id, final String nodeName) {
        super(id);

        log.debug("Content Panel being created for node=" + nodeName + " id=" + id);

        Panel editableLabel = new AjaxEditableLabel<Object>("contentText"){

			private static final long serialVersionUID = 1L;

			private ContentEntryData contentEntry;

        	@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				log.debug("onSubmit - value="+ getDefaultModel());
	            log.debug("Submiting populated Content object to Content service.");
	            saveData(contentEntry);
			}

        	@Override
			protected void onBeforeRender(){
                log.debug("onBeforeRender - setting new Content. id="+id);
                PageData page = getRepositoryService().getPage(nodeName);
	            ContentData contentData = getRepositoryService().getContent(page, id);
	            contentEntry = getRepositoryService().getInlineContentEntry(contentData, getSession().getLocale());

                setDefaultModel(new PropertyModel<String>(contentEntry, "text"));
                if (((IContentAdmin)getSession()).isLoggedIn()) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
                super.onBeforeRender();
            }

			@Override
			protected void onComponentTag(ComponentTag tag) {
				if (((IContentAdmin)getSession()).isLoggedIn()) {
					CharSequence s = tag.getAttribute("class");
                    tag.put("class", "editabel" + (s.length()<=0?"":" "+s));
                }
				super.onComponentTag(tag);
			}

        };
        add(editableLabel);

    }


}
