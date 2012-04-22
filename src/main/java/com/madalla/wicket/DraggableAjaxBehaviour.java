package com.madalla.wicket;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;
import org.apache.wicket.request.Request;

import com.madalla.wicket.javascript.JavascriptBuilder;

public class DraggableAjaxBehaviour extends AbstractTransformerBehavior {

	private static final long serialVersionUID = 1L;

	public static String getDraggablesId(Request request){
		return request.getRequestParameters().getParameterValue("amp;dragId").toString();
	}

	private String dragId;

	public DraggableAjaxBehaviour(){

	}

	/**
	 * @param dragId - ID to be passed to droppable
	 */
	public DraggableAjaxBehaviour(String dragId) {
		this.dragId = dragId;
	}

	@Override
	public CharSequence transform(Component component, CharSequence output)
			throws Exception {
		return output + getJavascriptTemplate(component.getMarkupId());
	}

	private String getJavascriptTemplate(String id){

		JavascriptBuilder builder = new JavascriptBuilder();
		builder.addLine("var e = $('"+id+"');");
		builder.addLine("new Draggable(e,{ghosting: true, revert: true});");
		builder.addLine("e.setStyle({ cursor: 'move'});");
		builder.addLine("e.addClassName('draggable');");
		if (!StringUtils.isEmpty(dragId)){
		    builder.addLine("e.writeAttribute('dragId', value = '"+ dragId+"');");
		}

		return builder.buildScriptTagString();

	}

}
