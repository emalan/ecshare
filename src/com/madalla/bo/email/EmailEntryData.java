package com.madalla.bo.email;

import com.madalla.bo.AbstractData;

public abstract class EmailEntryData extends AbstractData implements IEmailEntryData, Comparable<EmailEntryData>{

	private static final long serialVersionUID = -5395435421377053468L;

	public int compareTo(EmailEntryData compare) {
		return compare.getId().compareTo(getId());
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof EmailEntryData)) return false;
		EmailEntryData compare = (EmailEntryData)obj; 
		if (!getId().equals(compare.getId())) return false;
		return true;
	}
}
