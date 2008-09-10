package com.madalla.wicket;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.StringHeaderContributor;

public class KeywordHeaderContributor extends StringHeaderContributor {
	private static final long serialVersionUID = 1L;
	private static final String contribution = "<meta content=\"{0}\" name=\"KEYWORDS\"/>";
	
	public KeywordHeaderContributor(String keywords) {
		super(StringUtils.isEmpty(keywords) ? "" : MessageFormat.format(contribution, keywords));
	}

}
