package com.madalla.bo.security;

import com.madalla.bo.AbstractData;

public abstract class UserSiteData extends AbstractData implements IUserSite, Comparable<UserSiteData>{

	private static final long serialVersionUID = 1L;

	public int compareTo(UserSiteData o) {
		if (!getName().equals(o.getName())){
			return getName().compareTo(o.getName());
		} else {
			return new Integer(hashCode()).compareTo(o.hashCode());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof UserSiteData)) return false;
		UserSiteData compare = (UserSiteData)obj;
		if (!getName().equals(compare.getName()))return false;
		return true;
	}
}
