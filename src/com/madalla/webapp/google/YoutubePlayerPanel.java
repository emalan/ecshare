package com.madalla.webapp.google;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.madalla.webapp.scripts.JavascriptResources;

public class YoutubePlayerPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public class YtVideo {
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

	public YoutubePlayerPanel(String id) {
		super(id);
		add(JavascriptResources.SWF_OBJECT);
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
	
	protected Component newEditLink(MarkupContainer parent, String componentId){
		AjaxLink link = new AjaxLink(componentId)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", getString("label.configure"));
				super.onComponentTag(tag);
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				//TODO make abstract
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new IAjaxCallDecorator(){

					private static final long serialVersionUID = 1L;

					public CharSequence decorateOnFailureScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateOnSuccessScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateScript(CharSequence script) {
						String s =("var e = Wicket.$('resourceLink'); if (Utils.hasClassName(e, 'editing'))"+
								"{Utils.removeClassName(e, 'editing');wicketHide('resourceFormDiv');} else "+
								"{Utils.addClassName(e, 'editing');wicketShow('resourceFormDiv');};");
						
						return script + s;
					}
					
				};
			}

		};
		//link.setVisible(editMode);
		link.setOutputMarkupId(true);
		return link;
	}


}
