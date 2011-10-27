package com.madalla.webapp.components.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.util.MapModel;
import org.springframework.dao.DataAccessException;

import com.madalla.bo.SiteData;
import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.Member;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsPanel;

public abstract class AbstractMemberPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AbstractMemberPanel.class);

	public AbstractMemberPanel(String id) {
		super(id);
		if (!getCmsApplication().hasMemberService()){
			log.error("Member service not configured Correctly.");
			throw new WicketRuntimeException("Member service not configured Correctly.");
		}
	}

	protected String resetPassword(MemberData member){
        String password = SecurityUtils.getGeneratedPassword();
        log.debug("resetPassword - memeber="+member.getName() + "password="+ password);
        saveMemberPassword(member.getMemberId(), SecurityUtils.encrypt(password) );
        return password;
	}

	protected boolean saveMemberPassword(final String memberId, final String password){
		return getRepositoryService().saveMemberPassword(memberId, password);
	}
	
	protected boolean saveMemberData(MemberData member){
		return getRepositoryService().saveMember(member);
	}

    final protected boolean sendRegistrationEmail(final MemberData member){
    	logEmail(member.getDisplayName(), member.getEmail(), getString("email.subject"));
        String body = getRegistrationEmail(member);
        return getEmailSender().sendUserEmail(getString("email.subject"), body, member.getEmail(), member.getFirstName(), true);
    }

    
    /**
     * Will reset password and send email
     * 
     * @param member
     * @return
     */
    protected boolean sendResetPasswordEmail(final MemberData member){
    	logEmail(member.getDisplayName(), member.getEmail(), getString("email.subject"));
        String body = getResetPasswordEmail(member);
        return getEmailSender().sendUserEmail(getString("email.subject"), body, member.getEmail(), member.getFirstName(), true);
    }

    private void logEmail(String name, String email, String comment){
    	if (name.length() >= 25){
    		name = StringUtils.substring(name, 0, 23) + "..";
    	}
    	try {
    		getRepositoryService().createEmailEntry(name, email, comment);
    	} catch (DataAccessException e){
    		log.error("Data Access Exception while logging registration email.", e);
    	}
    }

    protected String getRegistrationEmail(final MemberData member){
    	Map<String, String> map = new HashMap<String, String>();
    	SiteData site = getRepositoryService().getSiteData();
    	map.put("siteName", site.getSiteName());
    	map.put("firstName", StringUtils.defaultString(member.getFirstName()));
		map.put("lastName", StringUtils.defaultString(member.getLastName()));
		map.put("companyName", StringUtils.defaultString(member.getCompanyName()));
		map.put("memberId", member.getMemberId());
		map.put("password", resetPassword(member));
		String url = StringUtils.defaultString(site.getUrl());
		map.put("url", url );
    	map.put("passwordChangePage", CmsApplication.MEMBER_PASSWORD);

		MapModel<String, String> values = new MapModel<String, String>(map);
		String message = getString("email.registration", values);

		message = message + getString("message.password", values);

		message = message + getString("message.note") + getString("message.closing");

		log.debug("formatMessage - " + message);
    	return message;
    }

    /**
     * Gets email message and resets password
     * @param member
     * @return
     */
    protected String getResetPasswordEmail(final MemberData member){
    	Map<String, String> map = new HashMap<String, String>();
    	SiteData site = getRepositoryService().getSiteData();
    	map.put("siteName", site.getSiteName());
    	map.put("firstName", StringUtils.defaultString(member.getFirstName()));
		map.put("lastName", StringUtils.defaultString(member.getLastName()));
		map.put("companyName", StringUtils.defaultString(member.getCompanyName()));
		map.put("memberId", member.getMemberId());
		map.put("password", resetPassword(member));
		String url = StringUtils.defaultString(site.getUrl());
		map.put("url", url );
    	map.put("passwordChangePage", CmsApplication.MEMBER_PASSWORD);

		MapModel<String, String> values = new MapModel<String, String>(map);
		String message = getString("email.reset", values);

		message = message + getString("message.password", values);

		message = message + getString("message.note") + getString("message.closing");

		log.debug("formatMessage - " + message);
    	return message;

    }
    
	protected Member instanciateMember() {
		return new Member();
	}

}
