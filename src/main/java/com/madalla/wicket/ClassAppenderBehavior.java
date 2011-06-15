package com.madalla.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public abstract class ClassAppenderBehavior extends AbstractBehavior {

	private static final long serialVersionUID = 1L;

	private final boolean display;
	private final String className;

	/**
	 * @param show
	 */
	public ClassAppenderBehavior(final boolean show){
		this(show,"no");
	}

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
