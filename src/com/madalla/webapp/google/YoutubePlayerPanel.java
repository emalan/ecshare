package com.madalla.webapp.google;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.madalla.wicket.configure.AjaxConfigureIcon;


public class YoutubePlayerPanel extends Panel implements IHeaderContributor{

	private static final long serialVersionUID = 1L;
	
	public class YtVideo implements Serializable {
		private String videoId;
		private int width;
		private int height;
		private int startSeconds;
		
		public String getVideoId() {
			return videoId;
		}
		public void setVideoId(String videoId) {
			this.videoId = videoId;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		public int getStartSeconds() {
			return startSeconds;
		}
		public void setStartSeconds(int startSeconds) {
			this.startSeconds = startSeconds;
		}
		
	}
	
	private YtVideo videoData;
	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public YoutubePlayerPanel(String id) {
		super(id);
		videoData = getYtVideo(id);
		String url = "http://www.youtube.com/v/"+videoData.getVideoId()+"&enablejsapi=1&playerapiid=ytplayer";
		add(new SwfObject(url,425, 356));
		add(new AjaxConfigureIcon("configureIcon","formDiv"));
	}
	
	//TODO get from Repository
	private YtVideo getYtVideo(String id){
		YtVideo videoData = new YtVideo();
		videoData.setVideoId("krb2OdQksMc");
		return videoData;
	}
	
	public void renderHead(IHeaderResponse response) {
		//response.renderOnLoadJavascript("cueVideo('"+videoData.getVideoId()+"');");
		
	}

	
	private Form newYtForm(String id){
		final Form form = new Form(id){
			private static final long serialVersionUID = 1L;;
			
			@Override
			protected void onSubmit() {

			}			
		};
		form.setOutputMarkupId(true);
		return form;
	}
	
	private FormComponent newFormField(String componentId, IModel model){
		final TextField field = new TextField(componentId, model);
		field.setOutputMarkupId(true);
		return field;
	}
	
}
