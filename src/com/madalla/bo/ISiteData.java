package com.madalla.bo;

public interface ISiteData {

	String getId();
	
	String getName();

	String getMetaDescription();

	String getAdminEmail();

	String getMetaKeywords();
	
	String getUrl();
	
	void setUrl(String url);
	
	void setAdminEmail(String adminEmail);
	
	String getLocales();
	
	void setLocales(String locales);

}
