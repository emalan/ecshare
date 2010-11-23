package com.madalla.cms.bo.impl.ocm;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.SiteData;

@Node
public class Site extends SiteData{
	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String metaKeywords;
	@Field private String metaDescription;
	@Field private String adminEmail;
	@Field private String locales;
	@Field private String siteName;
	@Field private String url;
	@Field private Boolean securityCertificate;
	@Field private String timeZone;
	
	public Site(){
		
	}
	
	public Site(String parentPath, String name){
		id = parentPath + "/" + name;
	}
	
	public String getName(){
	    return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public void setId(String id){
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public void setLocales(String locales) {
		this.locales = locales;
	}

	public String getLocales() {
		return locales;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	public Boolean getSecurityCertificate() {
		return securityCertificate == null? Boolean.FALSE : securityCertificate;
	}

	public void setSecurityCertificate(Boolean securityCertificate) {
		this.securityCertificate = securityCertificate;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}
