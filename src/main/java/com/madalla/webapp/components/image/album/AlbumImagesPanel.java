package com.madalla.webapp.components.image.album;

import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.CROSSFADE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.FAST_INIT;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class AlbumImagesPanel extends Panel{
	private static final long serialVersionUID = 1L;

	public AlbumImagesPanel(String id, final AlbumData album, final IModel<List<ImageData>> imagesModel, final boolean navigation) {
		super(id);
		
		add(JavascriptPackageResource.getHeaderContribution(PROTOTYPE));
		add(JavascriptPackageResource.getHeaderContribution(JavascriptResources.ANIMATOR));
		add(JavascriptPackageResource.getHeaderContribution(FAST_INIT));

		if (navigation) {
			add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
			add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.BANNER));
			add(ScriptUtils.BANNER_CSS);
		} else {
			add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
			add(ScriptUtils.CROSSFADE_CSS);
		}

		final WebMarkupContainer imageListContainer  = new WebMarkupContainer("images");
        imageListContainer.setOutputMarkupId(true);
        add(imageListContainer);

        imageListContainer.add(new ListView<ImageData>("image-list", imagesModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageData> item) {
				final ImageData imageData = item.getModelObject();
				item.add(createImage(album, imageData));
			}

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
			}

			@Override
			public void renderHead(HtmlHeaderContainer container) {
				int interval = album.getInterval();
				int width = album.getWidth();
				int height = album.getHeight();
				String params = "{interval:" + interval + ", height:" + height + ", width: " + width + "}";

				if (navigation) {
					container.getHeaderResponse().renderOnLoadJavascript(
							"new Banner($('" + imageListContainer.getMarkupId() + "')," + params + ")");
				} else {
					container.getHeaderResponse().renderOnLoadJavascript(
							"new Crossfade($('" + imageListContainer.getMarkupId() + "')," + params + ")");
				}
				super.renderHead(container);
			}

		});
		
		
	}
	
	private WebMarkupContainer createImageWrapper(final AlbumData album) {
		return new WebMarkupContainer("imageWrapper") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("style", "height: " + (album.getHeight() + 15) + "px; width: " + (album.getWidth() + 15)
						+ "px;");
				tag.put("class", "imageWrapper");
			}
		};
	}

	private Component createImage(final AlbumData album, final ImageData imageData) {
		WebMarkupContainer imageWrapper = createImageWrapper(album);

		Image image = new Image("id", imageData.getImageFull()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("height", album.getHeight());
				tag.put("width", album.getWidth());
				tag.put("title", imageData.getTitle());
			}

		};

		if (StringUtils.isNotEmpty(imageData.getUrl())) {
			image.add(new AjaxEventBehavior("onclick") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					getRequestCycle().setRequestTarget(new RedirectRequestTarget(imageData.getUrl()));
				}
			});
		}
		imageWrapper.add(image);

		return imageWrapper;
	}

}
