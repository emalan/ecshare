package com.madalla.bo.member;

import org.joda.time.DateTime;

public interface IMemberData {

	String getMemberId();

	void setMemberId(String memberId);

	String getPassword();

	void setPassword(String password);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getCompanyName();

	void setCompanyName(String companyName);

	String getEmail();

	void setEmail(String email);

	DateTime getSignupDate();

	void setSignupDate(DateTime signupDate);

	DateTime getAuthorizedDate();

	void setAuthorizedDate(DateTime authorizedDate);

}