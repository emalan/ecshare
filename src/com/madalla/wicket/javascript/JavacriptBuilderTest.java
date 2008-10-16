package com.madalla.wicket.javascript;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class JavacriptBuilderTest extends TestCase {
	Log log = LogFactory.getLog(this.getClass());

	public void testJavascriptBuilder(){
		JavascriptBuilder builder = new JavascriptBuilder();
		builder.addLine("var e = $('"+"testID"+"');");
		builder.addLine("e.addClassName('droppable');");
		
		Map map = new HashMap();
		map.put("accept", "draggable");
		map.put("hoverclass", "hover");
		map.put("onDrop", new JavascriptFunction("dragged, dropped, event").
				addLine("console.log(dragged);").addLine("console.log(dropped);").
				addLine("console.log(event);").addLine("e.addClassName('droppable');") );
		
		builder.addLine("Droppables.add(e,"+builder.formatAsJavascriptHash(map)).addLine(");");

		
		log.debug(builder.toJavascript());
		
//		String s = "var e = $('"+testID+"'); Droppables.add(e, {accept: 'draggable',hoverclass: 'hover',"+
//		"onDrop: function(dragged, dropped, event){"+
//		"console.log(dragged);console.log(dropped);console.log(event);"+
//		"var wcall = wicketAjaxGet('"+getCallbackUrl()+"');"+
//        "}});"+
//		"e.addClassName('droppable')";
	}
}
