package com.madalla.db.dao;

import java.util.Date;

import org.emalan.cms.bo.member.MemberData;
import org.joda.time.DateTime;

public final class Member extends MemberData {
	private static final long serialVersionUID = 1L;

	private int id;
	private String memberId;
	private String firstName;
	private String lastName;
	private String companyName;
	private String email;
	private DateTime signupDate;
	private DateTime authorizedDate;
	private Date subscriptionEnd;

	@Override
	public int getKey(){
		return id;
	}
	@Override
	public String getId() {
		return String.valueOf(id);
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String getName() {
		return memberId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public DateTime getSignupDate() {
		return signupDate;
	}
	public void setSignupDate(DateTime signupDate) {
		this.signupDate = signupDate;
	}
	public DateTime getAuthorizedDate() {
		return authorizedDate;
	}
	public void setAuthorizedDate(DateTime authorizedDate) {
		this.authorizedDate = authorizedDate;
	}
	public void setSubscriptionEnd(Date subscriptionEnd) {
		this.subscriptionEnd = subscriptionEnd;
	}
	public Date getSubscriptionEnd() {
		return subscriptionEnd;
	}

}
