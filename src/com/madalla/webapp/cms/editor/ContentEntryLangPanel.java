package com.madalla.webapp.cms.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;

import tiny_mce.TinyMce;

import com.madalla.bo.SiteLanguage;
import com.madalla.webapp.CmsSession;

/**
 * Content Entry Panel - Edit User Content using a WYSWYG HTML editor.
 * <p>
 * Panel uses the TinyMCE (Javascript WYSIWYG Editor).
 * Note: The Wicket application must implement the IContentServiceProvider 
 * interface.
 * </p>
 * @author Eugene Malan
 * 
 */
public class ContentEntryLangPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT_ADM = new CompressedResourceReference(
            ContentEntryLangPanel.class, "ContentEntryPanel_admin.js");
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
            ContentEntryLangPanel.class, "ContentEntryPanel.js");
    private static final CompressedResourceReference JAVASCRIPT_SPR = new CompressedResourceReference(
            ContentEntryLangPanel.class, "ContentEntryPanel_super.js");
    Log log = LogFactory.getLog(this.getClass());


    /**
	 * @param name - wicket id
	 * @param nodeName
	 * @param contentId
	 * @param returnPage - used to create return Link
	 * 
	 */
	public ContentEntryLangPanel(String name, final String nodeName, final String contentId) {
        super(name);
        add( JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));
        if (((CmsSession)getSession()).isSuperAdmin()){
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_SPR));
        } else if (((CmsSession)getSession()).isCmsAdminMode()) {
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_ADM));
        } else {
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT));
        }
        
        List<ITab> tabs = new ArrayList<ITab>();
        for (Iterator<SiteLanguage> iterator = SiteLanguage.getLanguages().iterator(); iterator.hasNext();) {
			SiteLanguage siteLanguage = (SiteLanguage) iterator.next();
			tabs.add(new AbstractTab(new Model<String>(siteLanguage.getDisplayName())){
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId) {
					
					return null;
				}
				
			});
			
		}
        add(new TabbedPanel("langtabs", tabs));
        
        
    }

    


}
