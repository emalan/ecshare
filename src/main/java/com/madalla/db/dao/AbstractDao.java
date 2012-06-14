package com.madalla.db.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AbstractDao {

	protected JdbcTemplate template;
	protected String site;

	public AbstractDao() {
		super();
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	public void setSite(String site) {
		this.site = site;
	}

}