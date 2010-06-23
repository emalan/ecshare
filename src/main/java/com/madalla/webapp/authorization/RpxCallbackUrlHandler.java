package com.madalla.webapp.authorization;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.AbstractRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.madalla.rpx.Rpx;

/**
 * Handles URL callback from Rpx authentication system.
 * 
 * @author Eugene Malan
 *
 *	Uses token to retrieve Profile information from rpx provider after user has
 *  logged in with their choice of authenticator.
 *  
 */
public abstract class RpxCallbackUrlHandler extends AbstractRequestTargetUrlCodingStrategy{

	private final static Log log = LogFactory.getLog(RpxCallbackUrlHandler.class);
	
	protected final WeakReference<Class<? extends Page>> successBookmarkablePageClassRef;
	protected final WeakReference<Class<? extends Page>> failBookmarkablePageClassRef;
	protected final Rpx rpx;
	
	public <C extends Page> RpxCallbackUrlHandler(String mountPath, final Class<? extends Page> successBookmarkablePageClassRef,
			final Class<? extends Page> failBookmarkablePageClassRef, Rpx rpx) {
		super(mountPath);
		this.successBookmarkablePageClassRef = new WeakReference<Class<? extends Page>>(successBookmarkablePageClassRef);
		this.failBookmarkablePageClassRef = new WeakReference<Class<? extends Page>>(failBookmarkablePageClassRef);
		this.rpx = rpx;
	}

	public IRequestTarget decode(RequestParameters requestParameters) {
		log.debug("decode - requestParameters:" + requestParameters);
		
		boolean loginSuccess = false;
		
		String[]params =  (String[]) requestParameters.getParameters().get("token");
		String token = params[0];
		
		if (StringUtils.isEmpty(token)){
			log.info("Page called without token. User cancelled openid login.");
		} else {
			log.debug("Token received. token:" + token);
			
			//Rpx rpx = new Rpx("1f9259520b17b0e3467bc6e2adf6e5b4d61d13a0", "https://ecsite.rpxnow.com/");
			Element element = rpx.authInfo(token);
			Node profile = element.getFirstChild();
			log.debug("Profile -->" + profile.getNodeName());

			HashMap<String, String> personalData = new HashMap<String, String>();
			NodeList nodes = profile.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++){
				Node node = nodes.item(i);
				personalData.put(node.getNodeName(), node.getChildNodes().item(0).getNodeValue());
			}
			log.debug(personalData);
			
			if (rpxLogin(personalData)){
				loginSuccess = true;
			}
			
		}
		
		//redirect to Home Page 
		//return new BookmarkablePageRequestTarget(dest);
		//return new RedirectPageRequestTarget(dest);
		
		if (loginSuccess){
			return new BookmarkablePageRequestTarget(requestParameters.getPageMapName(),
					(Class<? extends Page>)successBookmarkablePageClassRef.get() );
		} else {
			return new BookmarkablePageRequestTarget(requestParameters.getPageMapName(),
				(Class<? extends Page>)failBookmarkablePageClassRef.get() );
		}
		
	}
	
	protected abstract boolean rpxLogin(HashMap<String, String> personalData);

	public CharSequence encode(IRequestTarget requestTarget) {
		log.info("encode - Should not be called");
		return null;
	}

	public boolean matches(IRequestTarget requestTarget) {
		return false;
	}
	


}
