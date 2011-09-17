package com.madalla.cms.service.ocm;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.bo.member.MemberData;
import com.madalla.bo.security.IUserValidate;
import com.madalla.db.dao.Member;
import com.madalla.db.dao.MemberDao;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator;


public class MemberService {

	private static final Log log = LogFactory.getLog(MemberService.class);

	private PasswordAuthenticator authenticator;
	private MemberDao memberDao;

	public boolean isMemberExists(String name){
		return memberDao.exists(name);
	}

    public boolean saveMember(MemberData member){
    	log.debug("saveMember - "+ member);
    	authenticator.clearUser(member.getName());
    	return memberDao.save(member);
    }
    
	public boolean saveMemberPassword(String memberId, String password) {
		return memberDao.savePassword(memberId, password);
	}

    public MemberData getMember(String name){
    	return memberDao.findbyMemberId(name);
    }

    public MemberData getMemberById(String id){
    	return memberDao.get(id);
    }

    public List<Member> getMembers(){
    	return memberDao.fetch();
    }

    public void deleteMember(MemberData data){
    	memberDao.delete(data);
    }

	public IPasswordAuthenticator getPasswordAuthenticator(final String name){
		if (name == null){
			throw new WicketRuntimeException("Username Argument may not be null");
		}
		final IUserValidate userValidate;
		if (isMemberExists(name)){
			userValidate = memberDao.getMemberValidator(name);
		} else {
			userValidate = new IUserValidate(){

				public String getName() {
					return name;
				}

				public String getPassword() {
					return null;
				}

			};
		}
		authenticator.addUser(name, userValidate);
		return authenticator;

	}

	public void setAuthenticator(PasswordAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
	}

}
