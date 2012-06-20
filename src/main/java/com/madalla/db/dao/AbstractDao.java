package com.madalla.db.dao;

import org.springframework.beans.factory.annotation.Value;


public class AbstractDao {

	protected String site;

	public AbstractDao() {
		super();
	}

	@Value("#{'${site.code}'}")
	public void setSite(String site) {
		this.site = site;
	}

}