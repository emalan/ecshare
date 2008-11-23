package com.madalla;

import org.apache.wicket.util.tester.WicketTester;

public class AppTester extends WicketTester {
	public AppTester() {
        super(new TestApplication());
    }
    public void initialize() { // not always called
        //getSecuritySettings().setAuthorizationStrategy(new AdminAuthStrategy());
        //ServiceInitializer.initializeDevelopmentServices();
    }
}
