package com.madalla.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Behavior that will add the specified class name depending on contruction boolean. Useful for 
 * hide and show of components.
 *  
 * @author Eugene Malan
 *
 */
public abstract class ClassAppenderBehavior extends Behavior {

	private static final long serialVersionUID = 1L;

	private final boolean display;
	private final String className;

	/**
	 * This constructor ill use a default class of 'no'
	 * @param display - add class when this is true and condition is true
	 */
	public ClassAppenderBehavior(final boolean display){
		this(display,"no");
	}

	/**
	 * @param display - add class when this is true and condition is true
	 * @param className - class string to use
	 */
	public ClassAppenderBehavior(final boolean display, final String className){
		this.display = display;
		this.className = className;
	}
	abstract protected boolean setClass();

	protected IModel<String> getClassAtributeModel(){
	    return new AbstractReadOnlyModel<String>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (setClass()){
					return display ? "" : className;
				}
				return display ? className : "";
			}

	    };
	}

	@Override
	public void bind(Component component) {
		component.add(new AttributeAppender("class", true ,getClassAtributeModel(), " "));
		super.bind(component);
	}
}
