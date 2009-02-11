package com.madalla.bo;

public interface ISiteData {

	String getId();
	
	String getName();

	String getMetaDescription();

	String getAdminEmail();

	String getMetaKeywords();
	
	void setAdminEmail(String adminEmail);
}
