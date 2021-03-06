package com.madalla.webapp.admin.pages;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Utility functions for Wicket Pages
 *
 * @author Eugene Malan
 *
 */
public class PageUtils {

	private PageUtils(){}

	private static void error(String message){
		throw new WicketRuntimeException(message);
	}
	private static void error(String message, Exception e){
		throw new WicketRuntimeException(message, e);
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Page> getReturnPage(PageParameters parameters, String panel){
		String pageString = getPageParameter(RETURN_PAGE, parameters, panel);
		Class returnPage = null;
		try {
            returnPage = Class.forName(pageString);
            returnPage.asSubclass(Page.class);
        } catch (ClassNotFoundException e) {
        	error(panel + " - ClassNotFoundException while getting return Class from PageParameters.", e);
        } catch (ClassCastException e){
        	error(panel + " - The Class Type of the PageParameter '"+RETURN_PAGE+"' needs to extend the Page class.");
        }
        return returnPage;
	}

	/** throws Exception if param not found */
	public static String getPageParameter(String paramName, PageParameters parameters, String panel){
		String paramValue = parameters.get(paramName).toString();
		if (StringUtils.isEmpty(paramValue)){
			error(panel + " - A pageParameter with value '"+ paramName+"' needs to be supplied.");
		}
		return paramValue;
	}
	
	public static String getPageParameter(int index, PageParameters parameters, String panel) {
	    StringValue value = parameters.get(index);
	    if(value.isEmpty()) {
	        error(panel + " - The pageParameter at index '"+ index+"' needs to be supplied.");
	    }
	    return value.toString();
	}

	/** supply default value if param not found */
	public static String getPageParameter(String paramName, PageParameters parameters, String panel, String defaultValue){
		String paramValue = parameters.get(paramName).toString();
		if (StringUtils.isEmpty(paramValue)){
			return defaultValue;
		} else {
			return paramValue;
		}
	}

}
