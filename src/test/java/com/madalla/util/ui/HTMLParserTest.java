package com.madalla.util.ui;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HTMLParserTest extends TestCase {
	private Log log = LogFactory.getLog(this.getClass());

	final static String doc = "<p>&nbsp;</p><p><span style=\"font-family: Arial; color: blue; font-size: small;\"><span style=\"font-size: 12pt; color: blue; font-family: Arial;\">urgency.&nbsp; The project team&rsquo;s focus during this period will be on installation,	configuration and&nbsp;testing.</span></span><span style=\"font-family: Arial; color: blue; font-size: x-small;\"><span style=\"font-size: 10pt; color: blue; font-family: Arial;\">&nbsp; </span></span></p>	<p><span style=\"font-family: Arial; color: blue; font-size: small;\"><span style=\"font-size: 12pt; color: blue; font-family: Arial;\">Additional details"+
	"about the upgrade weekend will be communicated closer to the "+
	"upgrade.</span></span></p>"+
	"<p><span style=\"font-family: Arial; color: blue; font-size: small;\"><span style=\"font-size: 12pt; color: blue; font-family: Arial;\">If you have any"+
	"questions or wish to request an urgent change, please contact Darel Gustafson"+
	"(952-967-7739), Sr. Manager, Client Office Solutions and Security Management</span></span></p>";

	public void testHTMLParserParse() throws IOException{
	     String result = HTMLParser.parseHTMLText(doc, 150);
	     log.debug("Result="+result);
	}
}
