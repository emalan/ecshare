package com.madalla.wicket;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;

import com.madalla.wicket.javascript.JavascriptBuilder;
import com.madalla.wicket.javascript.JavascriptFunction;

public class AbstractDroppableBehaviour extends AbstractTransformerBehavior {

	private static final long serialVersionUID = 1L;

	private AbstractDefaultAjaxBehavior callBack;
	
	public AbstractDroppableBehaviour(AbstractDefaultAjaxBehavior callBack) {
		this.callBack = callBack;
	}

	@Override
	public CharSequence transform(Component component, CharSequence output)
			throws Exception {
		return output + getJavascriptTemplate(component.getMarkupId());
	}

	private String getJavascriptTemplate(String id){
		JavascriptBuilder builder = new JavascriptBuilder();
		builder.addLine("var e = $('"+id+"'); ");
		builder.addLine("e.addClassName('droppable');");
		
		Map map = new LinkedHashMap();
		map.put("accept", "draggable");
		map.put("hoverclass", "hover");
		
		JavascriptFunction onDropFunction = new JavascriptFunction("dragged, dropped, event");
		onDropFunction.addLine("console.log(dragged);").addLine("console.log(dropped);").
		addLine("console.log(event);"); //.addAjaxCallback(getAjaxBehavior());
		
		map.put("onDrop", onDropFunction);
		
		builder.addLine("Droppables.add(e,"+builder.formatAsJavascriptHash(map)).addLine(");");
		
		return builder.buildScriptTagString();

	}

}
