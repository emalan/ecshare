package com.madalla.bo.page;

public interface IPageMetaLangData {

    String getName();

	  String getId();
	  
	  String getMountName(String defaultValue);
	  
	  String getDisplayName();
	  
	  void setDisplayName(String displayName);

	  String getTitle();

	  void setTitle(String title);

	  String getLang();

	  void setLang(String lang);

	  String getDescription();

	  void setDescription(String description);

	  String getKeywords();

	  void setKeywords(String keywords);

	  String getAuthor();

	  void setAuthor(String author);

}