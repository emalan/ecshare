package tiny_mce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.cms.IContentAdmin;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Language;
import wicket.contrib.tinymce.settings.TinyMCESettings.Position;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;
import wicket.contrib.tinymce.settings.TinyMCESettings.Toolbar;

/**
 * Tiny Mce javascript library - version 3.0. Placeholder class for javascript resources.
 *
 * @author Eugene Malan
 *
 */
public class TinyMce {

	private TinyMce() {	}
	
	private static final Logger log = LoggerFactory.getLogger(TinyMce.class);
	
	public static TinyMceBehavior behavior(final Locale locale, final IContentAdmin session)
    {
		//set language
		Language language;
		try {
			language = Language.valueOf(locale.getLanguage());
		} catch (Exception e) {
			log.info("Editor does not support language. lang=" + locale.getLanguage() + ". Defaulting to english.");
			language = Language.en;
		}
		TinyMCESettings settings = new TinyMCESettings(Theme.advanced, language);
		
		
		settings.add(Button.bold, Toolbar.first, Position.after);
		settings.add(Button.italic, Toolbar.first, Position.after);
		settings.add(Button.underline, Toolbar.first, Position.after);
		settings.add(Button.strikethrough, Toolbar.first, Position.after);
		settings.add(Button.separator, Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
//		settings.add(Button., Toolbar.first, Position.after);
		
		
		//map.put("button1", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
		final List<Button> first = new ArrayList<Button>();
		final List<Button> second = new ArrayList<Button>();
		final List<Button> third = new ArrayList<Button>();
		
		if (session.isSuperAdmin()) {
			//newdocument,fullscreen,cleanup,removeformat,code,help,|,undo,redo,|,paste,pastetext,pasteword,|,link,unlink,anchor,image,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,
			//formatselect,fontselect,fontsizeselect|,forecolor,backcolor,|,tablecontrols
			//bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat,|blockquote
			first.add(Button.bold);
			first.add(Button.italic);
			first.add(Button.underline);
			first.add(Button.strikethrough);
			first.add(Button.separator);
			first.add(Button.undo);
			first.add(Button.redo);
			first.add(Button.separator);
			first.add(Button.cleanup);
			first.add(Button.separator);
			first.add(Button.bullist);
			first.add(Button.numlist);
			first.add(Button.separator);
			first.add(Button.sub);
			first.add(Button.sup);
			first.add(Button.separator);
			first.add(Button.charmap);
			first.add(Button.separator);
			first.add(Button.removeformat);
		} else if (session.isCmsAdminMode()) {
			//bold,italic,underline,strikethrough,|,sub,sup,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,|,image
			first.add(Button.bold);
			first.add(Button.italic);
			first.add(Button.underline);
			first.add(Button.strikethrough);
			first.add(Button.separator);
			first.add(Button.sub);
			first.add(Button.sup);
			first.add(Button.separator);
			first.add(Button.justifyleft);
			first.add(Button.justifycenter);
			first.add(Button.justifyright);
			first.add(Button.justifyfull);
			first.add(Button.separator);
			first.add(Button.styleselect);
			first.add(Button.formatselect);
			first.add(Button.separator);
			first.add(Button.image);
			//bullist,numlist,|,hr,charmap,removeformat,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,cleanup,code
			second.add(Button.bullist);
			second.add(Button.numlist);
			second.add(Button.separator);
			second.add(Button.hr);
			second.add(Button.charmap);
			second.add(Button.removeformat);
			second.add(Button.separator);
			second.add(Button.outdent);
			second.add(Button.indent);
			second.add(Button.separator);
			second.add(Button.undo);
			second.add(Button.redo);
			second.add(Button.separator);
			second.add(Button.link);
			second.add(Button.unlink);
			second.add(Button.anchor);
			second.add(Button.cleanup);
			second.add(Button.code);
			
		} else {
			first.add(Button.bold);
			first.add(Button.italic);
			first.add(Button.underline);
			first.add(Button.strikethrough);
			first.add(Button.separator);
			first.add(Button.undo);
			first.add(Button.redo);
			first.add(Button.separator);
			first.add(Button.cleanup);
			first.add(Button.separator);
			first.add(Button.bullist);
			first.add(Button.numlist);
			first.add(Button.separator);
			first.add(Button.sub);
			first.add(Button.sup);
			first.add(Button.separator);
			first.add(Button.charmap);
			first.add(Button.separator);
			first.add(Button.removeformat);
		}
		
		settings.setToolbarButtons(Toolbar.first, first);
		settings.setToolbarButtons(Toolbar.second, second);
		settings.setToolbarButtons(Toolbar.third, third);
		
        return new TinyMceBehavior(settings);
    }
	
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
