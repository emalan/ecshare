package com.madalla.webapp.cms.editor;

import java.util.HashMap;
import java.util.Map;

import com.madalla.webapp.cms.IContentAdmin;

public class EditorSetup {

	public static Map<String, Object> setupTemplateVariables(IContentAdmin session){
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.isSuperAdmin()) {
			map.put("button1", "newdocument,fullscreen,cleanup,removeformat,code,help,|,undo,redo,|,paste,pastetext,pasteword,|,link,unlink,anchor,image,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,");
			map.put("button2", "formatselect,fontselect,fontsizeselect|,forecolor,backcolor,|,tablecontrols");
			map.put("button3", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat,|blockquote");
		} else if (session.isCmsAdminMode()) {
			map.put("button1", "bold,italic,underline,strikethrough,|,sub,sup,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,|,image");
			map.put("button2", "bullist,numlist,|,hr,charmap,removeformat,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,cleanup,code");
			map.put("button3", "");
		} else {
			map.put("button1", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
			map.put("button2", "");
			map.put("button3", "");
		}

		return map;
	}

}
