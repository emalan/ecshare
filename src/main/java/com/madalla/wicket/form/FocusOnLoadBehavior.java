package com.madalla.wicket.form;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.request.Response;

public class FocusOnLoadBehavior extends Behavior{
	private static final long serialVersionUID = 1L;

		@Override
		public void afterRender(Component component) {
			final Response response = component.getResponse();
			response.write("<script type=\"text/javascript\" language=\"javascript\">var elem = document.getElementById(\"" +
					component.getMarkupId() +
					"\"); if (elem.offsetWidth) elem.focus();</script>");
			super.afterRender(component);
		}

}
