package com.madalla.wicket.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

public class ValidationStyleBehaviour extends AbstractBehavior {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	private String validClass ;
	private String invalidClass ;
	
	public ValidationStyleBehaviour(){
		this("inputValid","inputError");
	}
	
	public ValidationStyleBehaviour(String validClass, String invalidClass){
		this.validClass = validClass;
		this.invalidClass = invalidClass;
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void onComponentTag(final Component component,
			final ComponentTag tag) {
	    if (component instanceof FormComponent) {
	        FormComponent formComponent = (FormComponent) component;
	        processFormComponent(formComponent, tag);
        }
	}
	
	private void processFormComponent(FormComponent<?> comp, ComponentTag tag){
        if (comp.isValid() && comp.getConvertedInput() != null) {
            log.debug("onComponentTag - Valid, setting class to "+validClass+". component="+comp.getId());
            tag.getAttributes().put("class", validClass);
        } else if (!comp.isValid()) {
            log.debug("onComponentTag - Invalid, setting class to "+invalidClass+". component="+comp.getId());
            tag.getAttributes().put("class", invalidClass);
        }
	}
	
}
