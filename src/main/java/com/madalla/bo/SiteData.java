package com.madalla.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class SiteData  extends AbstractData implements ISiteData{

	private static final long serialVersionUID = 1L;

	private List<SiteLanguage> localeList = new ArrayList<SiteLanguage>();

	public List<SiteLanguage> getLocaleList(){
		localeList.clear();
		String localeString = getLocales();
		if (StringUtils.isNotEmpty(localeString)){
			String[] codes = StringUtils.split(localeString, ',');
			for (int i = 0; i < codes.length; i++) {
				localeList.add(SiteLanguage.getLanguage(codes[i]));
			}
		}
        return localeList;
	}

	public void setLocaleList(List<SiteLanguage> locales){
		String localeString = "";
		for (SiteLanguage language: locales){
			localeString += language.locale.getLanguage() + ",";
		}
		setLocales(localeString);
	}

	public static List<SiteLanguage> getAvailableLocales(){
		return SiteLanguage.getLanguages();
	}

}
