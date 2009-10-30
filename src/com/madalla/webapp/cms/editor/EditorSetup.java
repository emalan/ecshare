package com.madalla.webapp.cms.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.madalla.bo.SiteLanguage;
import com.madalla.webapp.CmsSession;

public class EditorSetup {

	public static Map<String, Object> setupTemplateVariables(CmsSession session, List<SiteLanguage> locales, Locale currentLocale){
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.isSuperAdmin()) {
			map.put("button1", "newdocument,fullscreen,cleanup,removeformat,code,help,|,undo,redo,|,paste,pastetext,pasteword,|,link,unlink,anchor,image,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,");
			map.put("button2", "formatselect,fontselect,fontsizeselect|,forecolor,backcolor,|,tablecontrols");
			map.put("button3", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
		} else if (session.isCmsAdminMode()) {
			map.put("button1", "bold,italic,underline,strikethrough,|,sub,sup,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect");
			map.put("button2", "bullist,numlist,|,hr,charmap,removeformat,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,cleanup,code");
			map.put("button3", "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,blockquote,|,hr,advhr,charmap,emotions,|,insertdate,inserttime");
		} else {
			map.put("button1", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
			map.put("button2", "");
			map.put("button3", "");
		}
		
		//Translate source langs
		StringBuffer sb = new StringBuffer();
		for (SiteLanguage lang : locales) {
			sb.append("if (google.language.isTranslatable('"+ lang.getLanguageCode() +"')) {dst.options.add(new Option('" +lang.getDisplayName() + "','" + lang.getLanguageCode() + "'))};");
		}
		map.put("dstlangs", sb.toString());
		
		//map.put("srclang", "src.options.add(new Option('" + currentLocale.getDisplayLanguage() + "','" + currentLocale.getLanguage()+ "'));");
		
		return map;
	}

}
