package com.madalla.bo.security;

import java.io.Serializable;

public interface IProfile extends Serializable{

	 String getIdentifier();

	 void setIdentifier(String identifier);

	 String getProviderName();

	 void setProviderName(String providerName);

	 String getDisplayName();

	 void setDisplayName(String displayName);

	 String getPreferredUsername();

	 void setPreferredUsername(String preferredUsername);

	 String getEmail();

	 void setEmail(String email);

	 String getBirthday();

	 void setBirthday(String birthday);

	 String getUtcOffset();

	 void setUtcOffset(String utcOffset);

}