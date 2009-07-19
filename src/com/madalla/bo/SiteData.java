package com.madalla.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class SiteData  extends AbstractData implements ISiteData{

	private static final long serialVersionUID = 1L;
	
	private List<Locale> localeList = new ArrayList<Locale>();
	
	public List<Locale> getLocaleList(){
		localeList.clear();
		String[] codes = getLocales().split(",");
		for (int i = 0; i < codes.length; i++) {
			localeList.add(new Locale(codes[i]));
		}
        return localeList;
	}
	
	public void setLocaleList(List<Locale> locales){
		String localeString = "";
		for (Locale locale: locales){
			localeString += locale.getLanguage() + ",";
		}
		setLocales(localeString);
	}
	
	public static List<Locale> getAvailableLocales(){
		List<Locale> list = new ArrayList<Locale>();
		list.add(Locale.ENGLISH);
		list.add(Locale.FRENCH);
		list.add(Locale.GERMAN);
		list.add(Locale.ITALIAN);
		list.add(new Locale("af"));
		list.add(new Locale("es"));
		list.add(new Locale("nl"));
		return list;
	}

}
