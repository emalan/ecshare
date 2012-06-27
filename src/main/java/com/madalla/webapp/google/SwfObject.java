package com.madalla.webapp.google;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Response;

import com.madalla.webapp.scripts.JavascriptResources;

public class SwfObject extends Behavior{
	private static final long serialVersionUID = 1L;
	private final String url ;
	private int height;
	private int width;

	public SwfObject(String url, int width, int height){
		this.url = url;
		this.height = height;
		this.width = width;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(JavascriptResources.SWF_OBJECT));
		StringBuffer sb = new StringBuffer();
		sb.append("function onYouTubePlayerReady(playerId) {");
		sb.append("player = Wicket.$(\"myytplayer\");};");
		sb.append("function playVideo(id, width, height){");
		sb.append("if (player) {player.cueVideoById(id,3);}};");
		response.render(JavaScriptHeaderItem.forScript(sb.toString(), "swfcallback"));
	}

//	@Override
//	public void onRendered(Component component) {
//		Response response = component.getResponse();
//
//		response.write(JavaScriptUtils.SCRIPT_OPEN_TAG);
//		StringBuffer sb = new StringBuffer();
//		sb.append("var params = { allowScriptAccess: \"always\" };");
//		sb.append("var atts = { id: \"myytplayer\" };");
//		sb.append("swfobject.embedSWF(\""+url+"\"");
//		sb.append(",\"ytapiplayer\", \""+width+"\", \""+height+"\", \"8\", null, null, params, atts);");
//		response.write(sb.toString());
//		response.write(JavaScriptUtils.SCRIPT_CLOSE_TAG);
//	}


}
