package com.madalla.bo;

public interface ISiteData {

	String getId();

	String getName();

	String getTimeZone();

	String getAdminEmail();

	String getUrl();

	String getSiteName();

	String getMetaDescription();

	String getMetaKeywords();

	void setUrl(String url);

	void setSiteName(String siteName);

	void setAdminEmail(String adminEmail);

	void setTimeZone(String timeZone);

	String getLocales();

	void setLocales(String locales);

	Boolean getSecurityCertificate();

	void setSecurityCertificate(Boolean requiresAuthentication);

}
