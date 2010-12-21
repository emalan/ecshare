package com.madalla.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public abstract class ShowHideBehavior extends AbstractBehavior {

	private static final long serialVersionUID = 1L;
	
	private final boolean show;
	
	/**
	 * @param show 
	 */
	public ShowHideBehavior(final boolean show){
		this.show = show;
	}
	
	abstract protected boolean isLoggedIn();

	protected IModel<String> getClassAtributeModel(){
	    return new AbstractReadOnlyModel<String>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (isLoggedIn()){
					return show ? "" : "no";
				} 
				return show ? "no" : "";
			}
	    	
	    };
	}
	
	@Override
	public void bind(Component component) {
		component.add(new AttributeAppender("class", true ,getClassAtributeModel(), " "));
		super.bind(component);
	}
}
