package com.madalla.webapp.components.member;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.springframework.dao.DataAccessException;

import com.madalla.bo.member.MemberData;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsPanel;

public abstract class AbstractMemberPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AbstractMemberPanel.class);
	
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
        member.setPassword(SecurityUtils.encrypt(password));
        saveMemberData(member);
        return password;
	}
	
	protected boolean saveMemberData(MemberData member){
		return getRepositoryService().saveMember(member);
	}
	
    protected boolean sendEmail(final MemberData member){
    	logEmail(member.getDisplayName(), member.getEmail(), getEmailSubject());
        String body = getEmailBody(member);
        return getEmailSender().sendUserEmail(getEmailSubject(), body, member.getEmail(), member.getFirstName(), true);
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
    
    protected String getEmailBody(final MemberData member){
    	return "Email body";
    }
    
    protected String getEmailSubject(){
    	return "Member email";
    }

}
