package com.madalla.wicket.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Behaviour that sets the components class value based on validation result.
 *
 * Note: Make sure to setoutputMarkupId if using with Ajax Submit
 *
 * @author Eugene Malan
 *
 */
public class ValidationStyleBehaviour extends AbstractBehavior {

	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private String validClass;
	private String invalidClass;

	public ValidationStyleBehaviour() {
		this("inputValid", "inputError");
	}

	public ValidationStyleBehaviour(String validClass, String invalidClass) {
		this.validClass = validClass;
		this.invalidClass = invalidClass;
	}

	@Override
	public void onComponentTag(final Component component, final ComponentTag tag) {
		log.debug("onComponentTag - "+component);
		if (component instanceof FormComponent) {
			@SuppressWarnings("rawtypes")
			FormComponent formComponent = (FormComponent) component;
			processFormComponent(formComponent, tag);
		}
	}

	private void processFormComponent(FormComponent<?> comp, ComponentTag tag) {
		if (comp.isValid() && comp.getConvertedInput() != null) {
			log.debug("onComponentTag - Valid, setting class to " + validClass
					+ ". component=" + comp.getId() + ", value=" + comp.getConvertedInput());
			tag.getAttributes().put("class", validClass);
		} else if (!comp.isValid()) {
			log.debug("onComponentTag - Invalid, setting class to "
					+ invalidClass + ". component=" + comp.getId());
			tag.getAttributes().put("class", invalidClass);
		}
	}

}
