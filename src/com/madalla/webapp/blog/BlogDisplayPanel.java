package com.madalla.webapp.blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;

public class BlogDisplayPanel extends Panel implements IBlogAware  {

    private static final long serialVersionUID = 1L;

    public BlogDisplayPanel(String id, final Class blogEntryPage, final boolean adminMode, final IBlogService service) {
        super(id);
        //new Blog link
        add(new BookmarkablePageLink("CreateNew",blogEntryPage, new PageParameters(BLOG_ENTRY_ID+"=0")){
            private static final long serialVersionUID = 1L;

            protected final void onBeforeRender(){
                if (adminMode){
                    setVisible(true);
                } else {
                    setVisible(false);
                }
                super.onBeforeRender();
            }
        });
        
        //List existing Blogs
        List commentList = service.getBlogEntries();
        add(new ListView("comments", commentList) {
            public void populateItem(final ListItem listItem) {
                final BlogEntry entry = (BlogEntry) listItem.getModelObject();
                listItem.add(new DateLabel("date", new Model(entry.getDate()), new StyleDateConverter("MS",true)));
                listItem.add(new MultiLineLabel("text", entry.getText()).setEscapeModelStrings(false));

                //Edit link
                Map params = new HashMap();
                params.put(BLOG_ENTRY_ID,new Integer(entry.getId()));
                listItem.add(new BookmarkablePageLink("EditBlog",blogEntryPage,new PageParameters(BLOG_ENTRY_ID+"="+entry.getId())){
                    private static final long serialVersionUID = 1L;
                    protected final void onBeforeRender(){
                        if (adminMode){
                            setVisible(true);
                        } else {
                            setVisible(false);
                        }
                        super.onBeforeRender();
                    }
                });
                
                //Delete Link
                listItem.add(new Link("DeleteBlog"){
                    private static final long serialVersionUID = 1L;
                    protected final void onBeforeRender(){
                        if (adminMode){
                            setVisible(true);
                        } else {
                            setVisible(false);
                        }
                        super.onBeforeRender();
                    }
                    public void onClick() {
                        service.deleteBlogEntry(entry.getId());
                        getParent().setVisible(false);
                        getPage().render();
                    }
                    
                });
            }

        });
    }

}


