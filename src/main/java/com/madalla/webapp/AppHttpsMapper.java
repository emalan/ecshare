package com.madalla.webapp;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

public class AppHttpsMapper extends HttpsMapper {

    private final IRequestMapper delegate;
    private final RuntimeConfigurationType type;
    final boolean securitySite; 

    public AppHttpsMapper(final IRequestMapper delegate, RuntimeConfigurationType type, boolean securitySite) {
        super(delegate, new HttpsConfig(80, 443));
        this.delegate = delegate;
        this.type = type;
        this.securitySite = securitySite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRequestHandler mapRequest(final Request request) {
        if (type.equals(RuntimeConfigurationType.DEPLOYMENT) && !securitySite){
            return super.mapRequest(request);
        } else {
            return delegate.mapRequest(request);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        if (type.equals(RuntimeConfigurationType.DEPLOYMENT) && !securitySite){
            return super.mapHandler(requestHandler);
        } else {
            return delegate.mapHandler(requestHandler);
        }
    }
}
