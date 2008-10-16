package com.madalla.wicket.javascript;

import org.apache.wicket.behavior.AbstractAjaxBehavior;

public class JavascriptFunction {

	private static final long serialVersionUID = 1L;
	private String function;

	public JavascriptFunction(String params)
	{
		this.function = "function("+params+") {";
	}

	public String getFunction()
	{
		return function + "}";
	}
	
	public JavascriptFunction addLine(String line)
	{
		function = function + line;
		return this;
	}
	
	public void addAjaxCallback(AbstractAjaxBehavior behavior)
	{
		addLine("wicketAjaxGet('" + behavior.getCallbackUrl() + "');\n");
	}

}
