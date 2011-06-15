package com.madalla;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

public class UtilTest extends TestCase{


	public void testLocales(){
		String[] languages = Locale.getISOLanguages();
		Locale locale = new Locale("EN");
		System.out.println(languages);

		Locale[] locales = Locale.getAvailableLocales();
		System.out.println(locales);

		List<Locale> list = new ArrayList<Locale>();
		list.add(Locale.ENGLISH);
		list.add(Locale.FRENCH);
		list.add(Locale.GERMAN);
		list.add(Locale.ITALIAN);
		list.add(new Locale("af"));
		list.add(new Locale("es"));
		list.add(new Locale("nl"));
	}
}
