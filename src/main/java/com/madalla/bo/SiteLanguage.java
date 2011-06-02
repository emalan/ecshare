package com.madalla.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Define Languages that the application supports
 * 
 * @author Eugene Malan
 *
 */
public enum SiteLanguage{
	ENGLISH(Locale.ENGLISH, "Editable content", "Hover&Click to edit"),
	AFRIKAANS(new Locale("af"), "Afrikaans editable content", "Klik om te redigeer"),
	FRENCH(Locale.FRENCH,"French editable content","Hover&Click - French"),
	GERMAN(Locale.GERMAN,"German editable content","Hover&Click - German"),
	ITALIAN(Locale.ITALIAN,"Italian editable content","Hover&Click - Italian"),
	SPANISH(new Locale("es"),"Spanish editable content","Hover&Click - Spanish"),
	NEDERLANDS(new Locale("nl"),"Nederlands editable content","Hover&Click - Nederlands"),
	ZULU(new Locale("zu"),"Zulu editable content","Hover&Click - Zulu"),
	VENDA(new Locale("ve"), "Venda editable content", "Hover&Click - Venda"),
	XHOSA(new Locale("xh"), "Xhosa editable content", "Hover&Click - Xhosa");
	
	public Locale locale;
	
	public String defaultContent;
	
	public String defaultInlineContent;
	
	SiteLanguage(Locale locale, String defaultContent, String defaultInlineContent){
		this.locale = locale;
		this.defaultContent = defaultContent;
		this.defaultInlineContent = defaultInlineContent;
	}
	
	/**
	 * @return display the locale name in Base Language 
	 */
	public String getDisplayName(){
		return locale.getDisplayName(BASE_LOCALE);
	}
	
	public String getLanguageCode(){
		return locale.getLanguage();
	}
	
	// TODO use this as Base Language in application - so if we need to change it we can do it here
	public static final Locale BASE_LOCALE = Locale.ENGLISH;
	
	public static SiteLanguage getBaseLanguage(){
		return getLanguage(BASE_LOCALE.getLanguage());
	}
	
	public static SiteLanguage getLanguage(String code){
		if ("nl".equalsIgnoreCase(code)){
			return NEDERLANDS;
		} else if ("es".equalsIgnoreCase(code)){
			return SPANISH;
		} else if (Locale.ITALIAN.getLanguage().equalsIgnoreCase(code)){
			return ITALIAN;
		} else if (Locale.GERMAN.getLanguage().equalsIgnoreCase(code)){
			return GERMAN;
		} else if (Locale.FRENCH.getLanguage().equalsIgnoreCase(code)){
			return FRENCH;
		} else if ("af".equalsIgnoreCase(code)){
			return AFRIKAANS;
		} else if ("zu".equalsIgnoreCase(code)){
			return ZULU;
		} else if ("xh".equalsIgnoreCase(code)){
			return XHOSA;
		} else if ("ve".equalsIgnoreCase(code)){
			return VENDA;
		} else {
			return SiteLanguage.ENGLISH;
		}
	}
	
	public static List<SiteLanguage> getAllLanguages(){
		List<SiteLanguage> ret = new ArrayList<SiteLanguage>(SiteLanguage.values().length);
		for (int i = 0; i < SiteLanguage.values().length; i++) {
			ret.add(SiteLanguage.values()[i]);
		}
		return ret;
	}
	
	public static List<SiteLanguage> getLanguages(){
		List<SiteLanguage> ret = new ArrayList<SiteLanguage>(SiteLanguage.values().length);
		for (int i = 0; i < SiteLanguage.values().length; i++) {
			if (!SiteLanguage.values()[i].equals(getBaseLanguage())){
				ret.add(SiteLanguage.values()[i]);
			}
		}
		return ret;
	}

}
