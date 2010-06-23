package com.madalla.bo.security;

import com.madalla.bo.AbstractData;

public abstract class ProfileData extends AbstractData implements IProfile, Comparable<ProfileData>{

	private static final long serialVersionUID = 1L;
	
	public int compareTo(ProfileData o) {
		if (!getIdentifier().equals(o.getIdentifier())){
			return getIdentifier().compareTo(o.getIdentifier());
		} else {
			return new Integer(hashCode()).compareTo(o.hashCode());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ProfileData)) return false;
		ProfileData compare = (ProfileData)obj; 
		if (!getIdentifier().equals(compare.getIdentifier()))return false;
		return true;
	}
}
