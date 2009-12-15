package com.madalla.webapp.google;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.madalla.bo.page.PageData;
import com.madalla.bo.video.VideoPlayerData;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.configure.AjaxConfigureIcon;


public class YoutubePlayerPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * 
	 * @param id wicket id
	 * @param page Page Name
	 */
	public YoutubePlayerPanel(String id, String page) {
		super(id);
		add(Css.CSS_FORM);
		add(Css.CSS_BUTTONS);
		final VideoPlayerData videoData = getVideo(id, page);
		String url = "http://www.youtube.com/v/"+videoData.getVideoId()+"&enablejsapi=1&playerapiid=ytplayer";
		add(new SwfObject(url,videoData.getWidth(), videoData.getHeight()));
		if (((CmsSession)getSession()).isLoggedIn()){
			//add(new AjaxConfigureIcon("configureIcon","formDiv"));	
		} else {
			add(new Label("configureIcon"));
		}
		
		Form form = new Form("videoForm");
		form.setOutputMarkupId(true);
		form.add(new AjaxSubmitLink("submit"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				target.appendJavascript("playVideo('"+videoData.getVideoId()+"','"+ videoData.getWidth()+"','"+videoData.getHeight()+"');");
				saveVideo(videoData);
				//AjaxConfigureIcon.hide(target, "formDiv");
			}
			
		});
		form.add(newFormField("videoid", new PropertyModel(videoData, "videoId")));
		form.add(newFormField("height", new PropertyModel(videoData, "height")).add(new RangeValidator(10,700)));
		form.add(newFormField("width", new PropertyModel(videoData, "width")).add(new RangeValidator(10,900)));
		add(form);
		
	}
	
	private FormComponent newFormField(String componentId, IModel model){
		final TextField field = new TextField(componentId, model);
		field.setOutputMarkupId(true);
		field.setRequired(true);
		return field;
	}

	private VideoPlayerData getVideo(String id, String page){
		IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
		PageData pageData = service.getPage(page);
		return service.getVideoPlayerData(pageData, id);
	}
	
	private void saveVideo(VideoPlayerData data){
		IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
		service.saveVideoPlayerData(data);
	}
	
}
