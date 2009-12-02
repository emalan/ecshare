package com.madalla.webapp.panel;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.webapp.CmsApplication;

/**
 * @deprecated Panels used by Admin Pages are only used in one place, while application
 * panels are now instanciated from CmsPage which should be extended by all Application Pages.
 * <p>
 * Utility static instantiation methods to create Wicket Panels
 * for your Pages. Provided to simplify the API by providing one
 * place for getting Panels.
 * 
 * NOTE: Your Wicket Application needs to extend {@link CmsApplication} before you can use these Panels
 * 
 * TODO restyle class to a Page Utils
 * </p>
 * @author Eugene Malan
 *
 */
public class Panels {
	
	private Panels(){}
	
	

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
		String paramValue = parameters.getString(paramName);
		if (StringUtils.isEmpty(paramValue)){
			error(panel + " - A pageParameter with value '"+ paramName+"' needs to be supplied.");
		}
		return paramValue;
	}
	
	/** supply default value if param not found */
	public static String getPageParameter(String paramName, PageParameters parameters, String panel, String defaultValue){
		String paramValue = parameters.getString(paramName);
		if (StringUtils.isEmpty(paramValue)){
			return defaultValue;
		} else {
			return paramValue;
		}
	}

}
