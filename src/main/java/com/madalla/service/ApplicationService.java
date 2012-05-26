package com.madalla.service;

import java.util.List;

import org.emalan.cms.IDataService;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.bo.email.EmailEntryData;

import com.madalla.BuildInformation;
import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.Member;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.security.IPasswordAuthenticator;

public interface ApplicationService {

	BuildInformation getBuildInformation();
	
	IAuthenticator getUserAuthenticator();
	
	IPasswordAuthenticator getUserPasswordAuthenticator(final String name);
	
	boolean isMemberExists(String name);
	
	boolean saveMember(MemberData member);
	
	boolean saveMemberPassword(String memberId, String password);
	
	MemberData getMember(String id);
	
	List<Member> getMembers();
	
	void deleteMember(MemberData data);
	
	IPasswordAuthenticator getMemberPasswordAuthenticator(final String name);
	
    boolean sendEmail();

    boolean sendEmail(String subject, String body);
    
    boolean sendUserEmail(String subject, String body, String email, String name);
    
    boolean sendUserEmail(String subject, String body, String email, String name, boolean copyEmailAdmin);
    
    boolean sendUserHtmlEmail(String subject, String body, String userEmail, String userName);

    void createEmailEntry(String name, String email, String comment);
    
    void deleteEmailEntry(EmailEntryData email);
    
    EmailEntryData getEmailEntry(String id);
    
    List<EmailEntryData> getEmailEntries();

	IRepositoryAdminService getRepositoryAdminService();

	IDataService getRepositoryService();
	
}
