package com.madalla.webapp.google;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.NumberValidator;

import com.madalla.webapp.css.Css;
import com.madalla.wicket.configure.AjaxConfigureIcon;


public class YoutubePlayerPanel extends Panel {

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
		add(Css.CSS_FORM);
		add(Css.CSS_BUTTONS);
		videoData = getVideo(id);
		String url = "http://www.youtube.com/v/"+videoData.getVideoId()+"&enablejsapi=1&playerapiid=ytplayer";
		add(new SwfObject(url,videoData.getWidth(), videoData.getHeight()));
		final AjaxConfigureIcon icon = new AjaxConfigureIcon("configureIcon","formDiv");
		add(icon);
		
		Form form = new Form("videoForm");
		form.setOutputMarkupId(true);
		form.add(new AjaxSubmitLink("submit"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				target.appendJavascript("cueVideo('"+videoData.getVideoId()+"','"+ videoData.getWidth()+"','"+videoData.getHeight()+"');");
				target.appendJavascript("cueVideo('"+videoData.getVideoId()+"');");
				saveVideo();
				icon.hideConfigureArea(target);
			}
			
		});
		form.add(newFormField("videoid", new PropertyModel(videoData, "videoId")));
		form.add(newFormField("height", new PropertyModel(videoData, "height")).add(new NumberValidator.RangeValidator(10,700)));
		form.add(newFormField("width", new PropertyModel(videoData, "width")).add(new NumberValidator.RangeValidator(10,900)));
		add(form);
		
	}
	
	private FormComponent newFormField(String componentId, IModel model){
		final TextField field = new TextField(componentId, model);
		field.setOutputMarkupId(true);
		field.setRequired(true);
		return field;
	}

	//TODO get from Repository
	private YtVideo getVideo(String id){
		YtVideo videoData = new YtVideo();
		videoData.setVideoId("krb2OdQksMc");
		videoData.setWidth(425);
		videoData.setHeight(356);
		return videoData;
	}
	
	private void saveVideo(){
		
	}
	
}
