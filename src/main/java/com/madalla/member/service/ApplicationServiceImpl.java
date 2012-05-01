package com.madalla.member.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.emalan.cms.IDataService;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.bo.email.EmailEntryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.madalla.BuildInformation;
import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.EmailEntry;
import com.madalla.db.dao.EmailEntryDao;
import com.madalla.db.dao.Member;
import com.madalla.email.IEmailSender;
import com.madalla.member.MemberService;
import com.madalla.webapp.security.IPasswordAuthenticator;

public class ApplicationServiceImpl implements ApplicationService {

	private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);
	private static final Marker fatal = MarkerFactory.getMarker("FATAL");
	
	private BuildInformation buildInformation;
	private IRepositoryAdminService repositoryAdminService;
	private IDataService dataService;
	private MemberService memberService;
	private IEmailSender emailSender;
	private EmailEntryDao emailEntryDao;
	
	/**
	 * Initializing method
	 */
	public void init() {
    	if (buildInformation == null) {
    		log.error(fatal, "Build Information not configured Correctly.");
    		throw new IllegalStateException("Build Information not configured Correctly.");
    	}
    	log.info("Build Information. ecshare version:" + buildInformation.getVersion());
    	if (repositoryAdminService == null){
    		log.error(fatal, "Content Admin Service is not configured Correctly.");
    		throw new IllegalStateException("Repository Admin Service is not configured Correctly.");
    	}
    	if (dataService == null){
    		log.error(fatal, "Repository Data Service is not configured Correctly.");
    		throw new IllegalStateException("Repository Data Service is not configured Correctly.");
    	}
    	if (emailSender == null){
    		log.error(fatal, "Email Sender is not configured Correctly.");
    		throw new IllegalStateException("Email Service is not configured Correctly.");
    	}

	}
	
	/**
	 * Shutdown method
	 */
	public void destroy() {
		
	}
	
    public IRepositoryAdminService getRepositoryAdminService() {
		return repositoryAdminService;
	}

	public IDataService getRepositoryService() {
		return dataService;
	}

	public BuildInformation getBuildInformation() {
		return buildInformation;
	}
	
	public MemberData getMember(String id) {
		return memberService.getMember(id);
	}

	public List<Member> getMembers() {
		return memberService.getMembers();
	}

	public void deleteMember(MemberData data) {
		memberService.deleteMember(data);
	}
	
	public boolean isMemberExists(String name) {
		return memberService.isMemberExists(name);
	}

	public boolean saveMember(MemberData member) {
		return memberService.saveMember(member);
	}

	public boolean saveMemberPassword(String memberId, String password) {
		return memberService.saveMemberPassword(memberId, password);
	}
	
	public IPasswordAuthenticator getPasswordAuthenticator(String name) {
		return memberService.getPasswordAuthenticator(name);
	}

	public boolean sendEmail() {
		return emailSender.sendEmail();
	}

	public boolean sendEmail(String subject, String body) {
		return emailSender.sendEmail(subject, body);
	}

	public boolean sendUserEmail(String subject, String body, String email, String name) {
		return emailSender.sendUserEmail(subject, body, email, name);
	}

	public boolean sendUserEmail(String subject, String body, String email, String name, boolean copyEmailAdmin) {
		return emailSender.sendUserEmail(subject, body, email, name, copyEmailAdmin);
	}

	public boolean sendUserHtmlEmail(String subject, String body, String userEmail, String userName) {
		return emailSender.sendUserHtmlEmail(subject, body, userEmail, userName);
	}

	public void createEmailEntry(String name, String email, String comment) {
		EmailEntry data = new EmailEntry();
		data.setSenderName(name);
		data.setSenderEmailAddress(email);
		data.setSenderComment(comment);
		emailEntryDao.create(data);
	}

	public void deleteEmailEntry(EmailEntryData email) {
		emailEntryDao.delete(email);
	}

	public EmailEntryData getEmailEntry(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		} else {
			return emailEntryDao.find(id);
		}
	}

	public List<EmailEntryData> getEmailEntries() {
		return emailEntryDao.fetch();
	}

	///////////////////////////////////////////////
	//    Initializing methods
	///////////////////////////////////////////////
	public void setBuildInformation(BuildInformation buildInformation) {
		this.buildInformation = buildInformation;
	}
	public void setRepositoryAdminService(IRepositoryAdminService repositoryAdminService) {
		this.repositoryAdminService = repositoryAdminService;
	}
	public void setDataService(IDataService dataService) {
		this.dataService = dataService;
	}
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	public void setEmailSender(IEmailSender emailSender) {
		this.emailSender = emailSender;
	}
	public void setEmailEntryDao(EmailEntryDao emailEntryDao) {
		this.emailEntryDao = emailEntryDao;
	}

}
