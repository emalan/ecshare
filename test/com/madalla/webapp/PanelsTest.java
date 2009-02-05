package com.madalla.webapp;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.webapp.pages.AlbumAdminPage;

public class PanelsTest extends TestCase{
	Log log = LogFactory.getLog(this.getClass());

	public void testPanelsContructors(){
		
		Map map = new HashMap();
		PageParameters params = new PageParameters(map);
		
		//Exception for the id
		try {
		    Panels.albumAdminPanel("", params);
		    fail();
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//Exception for no album name
		try {
			Panels.albumAdminPanel("testID", params);
			fail();
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//Exception for no return Page
		map.put("albumName", "testAlbum");
		params = new PageParameters(map);
		try {
			Panels.albumAdminPanel("testID", params);
			fail();
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//Exception Class Not Found
		map.put("returnPage", "testReturnPage");
		params = new PageParameters(map);
		try {
			Panels.albumAdminPanel("testID", params);
			fail();
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//Exception - wrong class Type
		map.put("returnPage", this.getClass().getName());
		params = new PageParameters(map);
		try {
			Panels.albumAdminPanel("testID", params);
			fail();
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//This should really pass - but Wicket is not setup
		map.put("returnPage", AlbumAdminPage.class.getName() );
		params = new PageParameters(map);
		try {
			Panels.albumAdminPanel("testID", params);
		} catch (WicketRuntimeException e){
			log.debug(e);
		}
		
		//Panels.albumPanel(id, album, returnPage);
		
		//Panels.blogEntryPanel(id, params);
		
		//Panels.blogPanel(id, blog, returnPage, params);
		
		//Panels.contentPanel(id, node, returnPage);
		
		//Panels.emailPanel(id, subject);
	}
}
