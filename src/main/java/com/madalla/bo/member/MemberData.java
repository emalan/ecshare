package com.madalla.bo.member;

import com.madalla.bo.AbstractData;

public abstract class MemberData extends AbstractData implements IMemberData, Comparable<MemberData>{
	private static final long serialVersionUID = -4830506791264979429L;

	public int compareTo(MemberData compare) {
		return compare.getId().compareTo(getId());
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
}
