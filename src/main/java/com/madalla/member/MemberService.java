package com.madalla.member;

import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.emalan.cms.bo.security.IUserValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.Member;
import com.madalla.db.dao.MemberDao;
import com.madalla.member.service.ApplicationService;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator;

public class MemberService {
	
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);
	
	private PasswordAuthenticator authenticator;
	private MemberDao memberDao;
	
	public boolean isMemberExists(final String name) {
		return memberDao.exists(name);
	}
	
	public boolean saveMember(MemberData member) {
		log.debug("saveMember - " + member);
		authenticator.clearUser(member.getName());
		return memberDao.save(member);
	}
	
	public boolean saveMemberPassword(final String memberId, final String password) {
		return memberDao.savePassword(memberId, password);
	}
	
	public MemberData getMember(final String id) {
		return memberDao.get(id);
	}
	
	public List<Member> getMembers() {
		return memberDao.fetch();
	}
	
	public void deleteMember(final MemberData data) {
		memberDao.delete(data);
	}
	
	public IPasswordAuthenticator getPasswordAuthenticator(final String name) {
		if (name == null) {
			throw new WicketRuntimeException("Username argument may not be null");
		}
		final IUserValidate userValidate;
		if (isMemberExists(name)) {
			userValidate = memberDao.getMemberValidator(name);
		} else {
			userValidate = new IUserValidate() {
				
				public String getPassword() {
					return null;
				}
				
				public String getName() {
					return name;
				}
			};
		}
		authenticator.addUser(name, userValidate);
		return authenticator;
	}

	public void setAuthenticator(PasswordAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	

}
