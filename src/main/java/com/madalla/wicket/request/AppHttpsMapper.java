package com.madalla.wicket.request;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.CompoundRequestMapper;

public abstract class AppHttpsMapper extends CompoundRequestMapper {

	private final IRequestMapper delegate;
	private final RuntimeConfigurationType type;
	private final HttpsConfig httpsConfig;
	private final HttpsRequestChecker checker;

	public AppHttpsMapper(final IRequestMapper delegate, final RuntimeConfigurationType type,
			final HttpsConfig httpsConfig) {
		this.delegate = delegate;
		this.type = type;
		this.httpsConfig = httpsConfig;
		this.checker = new HttpsRequestChecker();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRequestHandler mapRequest(final Request request) {

		IRequestHandler requestHandler = delegate.mapRequest(request);

		if (requestHandler != null && type.equals(RuntimeConfigurationType.DEPLOYMENT) && isSiteSecure()) {
			final IRequestHandler httpsHandler = checker.checkSecureIncoming(requestHandler, httpsConfig);

			if (httpsConfig.isPreferStateful()) {
				Session.get().bind();
			}

			requestHandler = httpsHandler;
		}

		return requestHandler;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		Url url = delegate.mapHandler(requestHandler);
		if (type.equals(RuntimeConfigurationType.DEPLOYMENT) && isSiteSecure()) {

			switch (checker.getProtocol(requestHandler)) {
			case HTTP:
				url.setProtocol("http");
				url.setPort(httpsConfig.getHttpPort());
				break;
			case HTTPS:
				url.setProtocol("https");
				url.setPort(httpsConfig.getHttpsPort());
				break;
			}
		}
		return url;

	}

	@Override
	public int getCompatibilityScore(Request request) {
		return delegate.getCompatibilityScore(request);
	}

	public abstract boolean isSiteSecure();
}
