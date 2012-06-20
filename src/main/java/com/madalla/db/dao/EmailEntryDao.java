package com.madalla.db.dao;

import java.util.List;

import org.emalan.cms.bo.email.EmailEntryData;

public interface EmailEntryDao {

	public EmailEntryData find(String id);

	public int delete(EmailEntryData email);

	public List<EmailEntryData> fetch();

    public int create(String name, String email, String comment);

}
