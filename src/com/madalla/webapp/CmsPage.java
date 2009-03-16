package com.madalla.webapp;

import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.WebPage;

import com.madalla.webapp.css.Css;

public class CmsPage extends WebPage {

	public CmsPage(){
		super();
		add(Css.YUI_CORE);
		add(Css.BASE);
		
		add(new StringHeaderContributor("<meta name=\"AUTHOR\" content=\"Eugene Malan\" />"));
		//TODO get meta info from Site and insert here
	}
}
