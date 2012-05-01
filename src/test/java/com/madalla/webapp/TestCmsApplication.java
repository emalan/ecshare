package com.madalla.webapp;

import java.util.Collection;

import org.apache.wicket.Page;

import com.madalla.email.IEmailSender;

public class TestCmsApplication extends CmsApplication{

	@Override
	protected void addAppPages(Collection<Class<? extends Page>> pages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addPagesForMenu(Collection<Class<? extends Page>> pages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isSiteMultilingual() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return null;
	}

}
