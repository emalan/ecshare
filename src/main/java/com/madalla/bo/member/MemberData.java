package com.madalla.bo.member;

import com.madalla.bo.AbstractData;
import com.madalla.bo.security.IUserValidate;

public abstract class MemberData extends AbstractData implements IMemberData, IUserValidate, Comparable<MemberData>{
	private static final long serialVersionUID = -4830506791264979429L;
	
	abstract public int getKey();

	public int compareTo(MemberData compare) {
		return new Integer(compare.getKey()).compareTo(getKey());
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof MemberData)) return false;
		MemberData compare = (MemberData)obj; 
		if (!getId().equals(compare.getId())) return false;
		return true;
	}
    
    public String getDisplayName(){
    	return getFirstName() + ", " + getLastName() + " [" + getCompanyName() +"]";
    }
    
    public boolean isAuthorized(){
    	return getAuthorizedDate() == null ? false : true;
    }
}
