package com.madalla.cms.service.jcr;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

public class RepositoryWorkspaceAdmin {

    private JcrTemplate template;
    private String workspace;
    
    public void init(){
        createNewWorkspace(workspace);
    }

    //TODO allow switching between different workspaces
    public String[] getAvailableWorkspaces(){
        return (String[]) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException{
                JackrabbitWorkspace workpace = (JackrabbitWorkspace)session.getWorkspace();
                return workpace.getAccessibleWorkspaceNames();
            }
        });
    }
    
    // TODO create new workspaces from admin console
    public void createNewWorkspace(final String workspaceName) {
        if (!doesWorkspaceExist(workspaceName)) {
            template.execute(new JcrCallback() {

                public Object doInJcr(Session session) throws IOException, RepositoryException {
                    JackrabbitWorkspace workpace = (JackrabbitWorkspace) session.getWorkspace();
                    workpace.createWorkspace(workspaceName);
                    return null;
                }
            });
        }
    }

    public boolean doesWorkspaceExist(final String workspaceName){
        String[] spaces = getAvailableWorkspaces();
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i].equals(workspaceName)){
                return true;
            }
        }
        return false;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

    public JcrTemplate getTemplate() {
        return template;
    }

    
}
