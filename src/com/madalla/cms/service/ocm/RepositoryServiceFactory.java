package com.madalla.cms.service.ocm;

import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.SiteData;
import com.madalla.bo.SiteLanguage;
import com.madalla.bo.security.UserData;
import com.madalla.cms.bo.impl.ocm.security.UserSite;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.cms.service.ocm.util.JcrOcmUtils;
import com.madalla.image.ImageUtilities;
import com.madalla.service.ISessionDataService;
import com.madalla.service.ISessionDataServiceProvider;
import com.madalla.util.security.SecurityUtils;

/**
 * Factory for creating Repository Services. Will also do some repository setup 
 * if neccessary.
 * 
 * NOTE : At present this class is not used
 * 
 * @author Eugene Malan
 *
 */
public class RepositoryServiceFactory implements ISessionDataServiceProvider{
	
	private static final Log log = LogFactory.getLog(RepositoryServiceFactory.class);
    
	private String site ;
	private JcrTemplate template;
    private ObjectContentManager ocm;
    private RepositoryTemplate repositoryTemplate;
    private List<SiteLanguage> locales;
    private RepositoryService repositoryService;

	public void init() {
    	Session session;
		try {
			session = template.getSessionFactory().getSession();
		} catch (RepositoryException e) {
			log.error("Exception while getting Session from JcrTemplate", e);
			throw new WicketRuntimeException("Exception getting Session from JcrTemplate", e);
		}
		ocm =  JcrOcmUtils.getObjectContentManager(session);
		
    	repositoryTemplate = new RepositoryTemplate(template, ocm, site);
    	
    	//Process data migration if necessary
    	//RepositoryDataMigration.transformData(template, site);
    	
    	ImageUtilities.validateImageIO();

    	//Create Repository Service
    	repositoryService = new RepositoryService();
    	repositoryService.setSite(site);
    	repositoryService.setTemplate(template);
    	repositoryService.setRepositoryTemplate(repositoryTemplate);
    	//repositoryService.setOcm(ocm);
    	
    	//Create site node
    	SiteData siteData = repositoryService.getSite(site);
    	
    	//Create default Users if they don't exist yet
    	repositoryService.getNewUser("guest", SecurityUtils.encrypt("password"));
    	UserData adminUser = repositoryService.getNewUser("admin", SecurityUtils.encrypt("password"));
    	if (adminUser == null){
    	    adminUser = repositoryService.getUser("admin");
    	} else {
    		repositoryService.saveUserSite(new UserSite(adminUser.getId(), site));
    	}
        adminUser.setAdmin(true);
        repositoryService.saveDataObject(adminUser);
        
        //setup locales
        locales = siteData.getLocaleList();
        locales.add(SiteLanguage.ENGLISH); // english is default
        
        //repositoryService.setLocales(locales);
	}



	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public ISessionDataService getRepositoryService() {
		return null;
	}
	

}
