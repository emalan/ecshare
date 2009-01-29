package com.madalla.bo.security;

import com.madalla.bo.AbstractData;

public abstract class UserData extends AbstractData implements IUser, Comparable<UserData>{

	private static final long serialVersionUID = -4482310969197177191L;

	public int compareTo(UserData o) {
		if (!getName().equals(o.getName())){
			return getName().compareTo(o.getName());
		} else {
			return new Integer(hashCode()).compareTo(o.hashCode());
		}
	}	
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof UserData)) return false;
		UserData compare = (UserData)obj; 
		if (!getName().equals(compare.getName()))return false;
		if (!getEmail().equals(compare.getEmail()))return false;
		if (!getPassword().equals(compare.getPassword())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
