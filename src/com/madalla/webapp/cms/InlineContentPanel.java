package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;
import org.apache.wicket.model.Model;

import com.madalla.bo.IPageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.wicket.javascript.JavascriptBuilder;

public class InlineContentPanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    private Log log = LogFactory.getLog(this.getClass());
    private Class<? extends Page> returnPage;
    private String nodeName;
    private String nodeId;

    /**
     * 
     * @param id
     * @param node
     * @param returnPage
     */
    public InlineContentPanel(String id, String node, Class<? extends Page> returnPage) {
        super(id);
        add(JavascriptResources.PROTOTYPE);
        add(JavascriptResources.SCRIPTACULOUS_CONTROLS);
        
        
        this.nodeName = node;
        this.nodeId = id;
        this.returnPage = returnPage;
        log.debug("Content Panel being created for node=" + node + " id=" + id);
        IPageData page = getRepositoryservice().getPage(node);
        String contentBody = getRepositoryservice().getContentText(page, id , getSession().getLocale());
        Component contentBlock = new ContentContainer("contentBlock", id, node, contentBody);

        add(contentBlock);
    }

    public class ContentContainer extends WebMarkupContainer {
        private static final long serialVersionUID = 1L;

        public ContentContainer(String id, String contentId, String contentNode, String contentBody) {
            super(id);

            final Model contentModel = new Model(contentBody);
            
            // add content
            Component label = new Label("contentText", contentModel){
				private static final long serialVersionUID = 6930776696843471636L;

				protected void onBeforeRender(){
                    IPageData page = getRepositoryservice().getPage(nodeName);
                    String contentBody = getRepositoryservice().getContentText(page, nodeId, getSession().getLocale());
                    log.debug("onBeforeRender - setting new Content.");
                    contentModel.setObject(contentBody);
                    super.onBeforeRender();
                }
            };
            label.setEscapeModelStrings(false);
            label.setOutputMarkupId(true);
            label.add(new AbstractTransformerBehavior(){
				private static final long serialVersionUID = 1L;

				@Override
				public CharSequence transform(Component component,
						CharSequence output) throws Exception {
					return output + createInlineScript(component.getMarkupId());
				}
            });
            add(label);

//            // add link to edit it
//            Link link = new BookmarkablePageLink("contentLink", ContentEditPage.class, new PageParameters(CONTENT_NODE + "="
//                    + contentNode + "," + CONTENT_ID + "=" + contentId + "," + RETURN_PAGE + "="
//                    + returnPage.getName())) {
//                private static final long serialVersionUID = 1801145612969874170L;
//
//                protected final void onBeforeRender() {
//                    if (((IContentAdmin)getSession()).isLoggedIn()) {
//                        setEnabled(true);
//                    } else {
//                        setEnabled(false);
//                    }
//                    super.onBeforeRender();
//                }
//
//            };
//            link.setBeforeDisabledLink("");
//            link.setAfterDisabledLink("");
//            add(link);

        }
    }
    
	private String createInlineScript(String id){
		JavascriptBuilder builder = new JavascriptBuilder();
		//builder.addLine("document.observe('dom:loaded', function() {");
		builder.addLine("new Ajax.InPlaceEditor($('" + id + "'),'');");
		//builder.addLine("});");
		
		return builder.buildScriptTagString();
	}
    
    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }

}
