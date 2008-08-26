package com.madalla.util.ui;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HTMLParser {
	
	private final static Log log = LogFactory.getLog(HTMLParser.class);

	private HTMLParser() {}; // prevent instantiation

	public static String parseHTMLText(final String html, final int length) {
		final MutableInt endPos = new MutableInt();

		HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
			private int total;
			private boolean found = false;

			public void handleText(char[] data, int pos) {
				if (!found) {
					log.debug("---------handleText---------");
					log.debug("handleText - text="+String.valueOf(data));
					total += data.length;
					log.debug("handleText - pos="+pos+" total="+total);
					if (total >= length) {
						endPos.setValue(pos);
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
			log.error("parseHTMLText - Exception. "+e.getMessage());
			return html;
		}

		log.debug("parseHTMLText - Using pos="+endPos);
		String result;
		if (endPos.intValue() > 0){
			result = StringUtils.substring(html, 0, endPos.intValue());
		} else {
			result = html;
		}
		log.debug("parseHTMLText - result="+result);
		int lastp = StringUtils.lastIndexOf(result, "</p>")+4;
		int lastP = StringUtils.lastIndexOf(result, "</P>")+4;

		String ret = StringUtils.substring(result,0, lastp >= lastP? lastp : lastP);
		log.debug("parseHTMLText - result trimmed to last <P>="+ret);
		return ret;
	}
}
