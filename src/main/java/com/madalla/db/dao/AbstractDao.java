package com.madalla.db.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class AbstractDao {

	protected SimpleJdbcTemplate template;
	protected String site;

	public AbstractDao() {
		super();
	}

	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}

	public void setSite(String site) {
		this.site = site;
	}

}