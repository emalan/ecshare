package com.madalla.webapp.admin.member;

import java.io.Serializable;

import org.emalan.cms.bo.member.MemberData;

public abstract class MemberSession implements Serializable{

	private static final long serialVersionUID = 1L;

	private volatile boolean signedIn = false;
	private volatile MemberData member;

	public final boolean isSignedIn(){
		return signedIn;
	}

	public boolean signIn(final String memberName, final String password){
		return signedIn = authenticateMember(memberName, password);
	}
	
	public boolean signIn(final String memberName) {
		postAuthentication(memberName);
		return signedIn = true;
	}

	public void signOut(){
		signedIn = false;
	}

	protected void setMember(MemberData member){
		this.member = member;
	}

	public MemberData getMember(){
		return member;
	}

	protected abstract boolean authenticateMember(final String memberName, final String password);
	
	protected abstract void postAuthentication(final String memberName);

}
