package com.madalla.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.madalla.bo.email.EmailEntryData;

public class EmailEntryDao {

	private static final String INSERT = "insert into EMAIL_RECORD (SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT) values (?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT from EMAIL_RECORD where ID = ?";
	private static final String DELETE = "delete from EMAIL_RECORD where ID = ?";
	private static final String FETCH = "select ID,SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT from EMAIL_RECORD where SITE_NAME = ?";
	
	private SimpleJdbcTemplate template;
	private String site;

	private ParameterizedRowMapper<EmailEntryData> mapper = new ParameterizedRowMapper<EmailEntryData>() {

		public EmailEntryData mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailEntry email = new EmailEntry();
			email.setId(rs.getInt("ID"));
			email.setDateTime(new DateTime(rs.getTimestamp("SENT_DATE")));
			email.setSenderName(rs.getString("SENDER_NAME"));
			email.setSenderEmailAddress(rs.getString("SENDER_EMAIL"));
			email.setSenderComment(rs.getString("COMMENT"));
			return email;
		}
	};

	public int create(final EmailEntry email) {
		
		return template.update(INSERT, new Object[] { email.getDateTime().toDate(), site, email.getSenderName(),
				email.getSenderEmailAddress(), email.getSenderComment() });
	}

	public EmailEntryData find(String id) {
		return this.template.queryForObject(FIND, mapper, id);
	}
	
	public int delete(EmailEntryData email){
		return template.update(DELETE, new Object[]{email.getId()});
	}
	
	public List<EmailEntryData> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}

	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}

	public void setSite(String site) {
		this.site = site;
	}
	

}
