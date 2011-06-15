package com.madalla.wicket.javascript;

import java.util.Iterator;
import java.util.Map;



/**
 * Helper class for programatically constructing javascript.
 *
 * @author <a href="mailto:wireframe6464@users.sourceforge.net">Ryan Sonnek</a>
 * @author Eugene Malan  - Thanks Ryan!  made some usability changes
 */
public class JavascriptBuilder
{
	private StringBuffer buffer = new StringBuffer();

	public JavascriptBuilder addLine(String line)
	{
		buffer.append(line);
		return this;
	}

	public String buildScriptTagString()
	{
		return "\n<script type=\"text/javascript\">\n" + toJavascript() + "</script>\n";
	}

	public String toJavascript()
	{
		return buffer.toString();
	}

	public String formatAsJavascriptHash(Map<String,Object> options)
	{
		if (options.isEmpty())
		{
			return "{}";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		for (Iterator<String> iter = options.keySet().iterator(); iter.hasNext();)
		{
			String key = iter.next();
			Object value = options.get(key);

			//buffer.append("\n");
			buffer.append("  ").append(key).append(": ");
			buffer.append(formatJavascriptValue(value));

			if (iter.hasNext())
			{
				buffer.append(", ");
			}
		}
		//buffer.append("\n");
		buffer.append("}");
		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	private String formatJavascriptValue(Object value)
	{
		if (value instanceof CharSequence)
		{
			return "'" + value + "'";
		}
		if (value instanceof Map)
		{
			return formatAsJavascriptHash((Map<String, Object>)value);
		}
		if (value instanceof Boolean)
		{
			return value.toString();
		}
		if (value instanceof JavascriptFunction)
		{
			return ((JavascriptFunction)value).getFunction();
		}
		return value.toString();
	}

	public void addOptions(Map<String, Object> options)
	{
		addLine(formatAsJavascriptHash(options));
	}
}
