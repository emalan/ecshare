package com.madalla.webapp;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.CompoundRequestMapper;

public class TestMapper extends CompoundRequestMapper {

	final IRequestMapper delegate;
	public TestMapper(IRequestMapper delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public IRequestHandler mapRequest(Request request) {
		return delegate.mapRequest(request);
	}
	
	@Override
	public Url mapHandler(IRequestHandler handler) {
		return delegate.mapHandler(handler);
	}

}
