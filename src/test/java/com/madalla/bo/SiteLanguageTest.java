package com.madalla.bo;

import java.util.List;

import junit.framework.TestCase;

import org.emalan.cms.bo.SiteLanguage;

public class SiteLanguageTest extends TestCase {

	public void testSiteLanguage(){
		List<SiteLanguage> langs = SiteLanguage.getLanguages();
		for (SiteLanguage lang: langs){
			System.out.println(lang);
		}
		assertFalse(langs.contains(SiteLanguage.getBaseLanguage()));
	}
}
