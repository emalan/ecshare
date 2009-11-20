package com.madalla.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author emalan
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
	
	public String getDisplayName(){
		return locale.getDisplayName(Locale.ENGLISH);
	}
	
	public String getLanguageCode(){
		return locale.getLanguage();
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
	
	public static List<SiteLanguage> getLanguages(){
		List<SiteLanguage> ret = new ArrayList<SiteLanguage>(SiteLanguage.values().length -1);
		for (int i = 0; i < SiteLanguage.values().length; i++) {
			if (!SiteLanguage.values()[i].equals(SiteLanguage.ENGLISH)){
				ret.add(SiteLanguage.values()[i]);
			}
		}
		return ret;
	}

}
