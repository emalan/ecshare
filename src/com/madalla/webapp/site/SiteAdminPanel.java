package com.madalla.webapp.site;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SiteAdminPanel extends Panel{

    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(SiteAdminPanel.class);
    
    public class SiteForm extends Form {

        public SiteForm(String id, IModel model) {
            super(id, model);
            // TODO Auto-generated constructor stub
        }

        private static final long serialVersionUID = 1L;
        
    }
    
    public SiteAdminPanel(String id, Class<? extends Page> returnPage) {
        super(id);
        
        add(new PageLink("returnLink", returnPage));
        
    }
    
}
