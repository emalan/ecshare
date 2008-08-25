package com.madalla.util.ui;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;

public class HTMLParser {

	private HTMLParser() {}; // prevent instantiation

	public static String parseHTMLText(final String html, final int length) {
		final MutableInt ret = new MutableInt();

		HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
			private int total;
			private boolean found = false;

			public void handleText(char[] data, int pos) {
				if (!found) {
					total += data.length;
					System.out.println(data);
					System.out.println("pos=" + pos);
					System.out.println("total=" + total);
					if (total >= length) {
						ret.setValue(pos);
						found = true;
					}
				}
			}

		};

		Reader reader = new StringReader(html);
		try {
			new ParserDelegator().parse(reader, callback, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int lastp = StringUtils.lastIndexOf(html, "</p>");
		int lastP = StringUtils.lastIndexOf(html, "</P>");

		return StringUtils.substring(html,0, lastp >= lastP? lastp : lastP);
	}
}
