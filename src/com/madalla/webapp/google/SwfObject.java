package com.madalla.webapp.google;

import org.apache.wicket.Component;
import org.apache.wicket.Response;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.util.string.JavascriptUtils;

import com.madalla.webapp.scripts.JavascriptResources;

public class SwfObject extends AbstractBehavior{
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
	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(new CompressedResourceReference(JavascriptResources.class, "swfobject.js"));
		StringBuffer sb = new StringBuffer();
		sb.append("function onYouTubePlayerReady(playerId) {");
		sb.append("player = Wicket.$(\"myytplayer\");};");
		sb.append("function cueVideo(id){");
		sb.append("if (player) {player.cueVideoById(id,3);}};");
		response.renderJavascript(sb.toString(), "swfcallback");
	}

	@Override
	public void onRendered(Component component) {
		Response response = component.getResponse();
		
		response.write(JavascriptUtils.SCRIPT_OPEN_TAG);
		StringBuffer sb = new StringBuffer();
		sb.append("var params = { allowScriptAccess: \"always\" };");
		sb.append("var atts = { id: \"myytplayer\" };");
		sb.append("swfobject.embedSWF(\""+url+"\"");
		sb.append(",\"ytapiplayer\", \""+width+"\", \""+height+"\", \"8\", null, null, params, atts);");
		response.write(sb.toString());
		response.write(JavascriptUtils.SCRIPT_CLOSE_TAG);
	}


}
