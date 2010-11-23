package com.madalla.cms.service.ocm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.bo.member.MemberData;
import com.madalla.bo.security.IUserValidate;
import com.madalla.db.dao.MemberDao;
import com.madalla.db.dao.TransactionLogDao;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator;


public class MemberService {

	private static final Log log = LogFactory.getLog(MemberService.class);
	
	private PasswordAuthenticator authenticator;
	private MemberDao memberDao;
    private TransactionLogDao transactionLogDao;
	
	public boolean isMemberExists(String name){
		return memberDao.find(name) != null;
	}
	
    public boolean createMember(MemberData member){
    	return memberDao.create(member);
    }
    
    public MemberData getMember(String name){
    	return memberDao.find(name);
    }
	
	public IPasswordAuthenticator getPasswordAuthenticator(final String name){
		if (name == null){
			throw new WicketRuntimeException("Username Argument may not be null");
		}
		IUserValidate userValidate;
		if (isMemberExists(name)){
			userValidate = getMember(name);
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

	public void setTransactionLogDao(TransactionLogDao transactionLogDao) {
		this.transactionLogDao = transactionLogDao;
	}

}
