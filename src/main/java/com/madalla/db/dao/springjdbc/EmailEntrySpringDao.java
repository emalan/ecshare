package com.madalla.db.dao.springjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.emalan.cms.bo.email.EmailEntryData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.madalla.db.dao.EmailEntry;
import com.madalla.db.dao.EmailEntryDao;

@Component()
@Qualifier("Spring")
public class EmailEntrySpringDao  extends AbstractSpringDao implements EmailEntryDao{

	private static final String INSERT = "insert into EMAIL_RECORD (SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT) values (?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT from EMAIL_RECORD where ID = ?";
	private static final String DELETE = "delete from EMAIL_RECORD where ID = ?";
	private static final String FETCH = "select ID,SENT_DATE,SITE_NAME,SENDER_NAME,SENDER_EMAIL,COMMENT from EMAIL_RECORD where SITE_NAME = ?";

	private ParameterizedRowMapper<EmailEntryData> mapper = new ParameterizedRowMapper<EmailEntryData>() {

		public EmailEntryData mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailEntry email = new EmailEntry();
			email.setKey(rs.getLong("ID"));
			email.setDateTime(new DateTime(rs.getTimestamp("SENT_DATE"), DateTimeZone.UTC));
			email.setSenderName(rs.getString("SENDER_NAME"));
			email.setSenderEmailAddress(rs.getString("SENDER_EMAIL"));
			email.setSenderComment(rs.getString("COMMENT"));
			return email;
		}
	};
	
    @Override
    public int create(String name, String email, String comment) {
        EmailEntry data = new EmailEntry();
        data.setSenderName(name);
        data.setSenderEmailAddress(email);
        data.setSenderComment(comment);
        return template.update(INSERT, new Object[] { new DateTime(DateTimeZone.UTC).toDate(), site, data.getSenderName(),
                data.getSenderEmailAddress(), data.getSenderComment() });
    }
    
	@Override
	public EmailEntryData find(String id) {
		return this.template.queryForObject(FIND, mapper, id);
	}

	@Override
	public int delete(EmailEntryData email){
		return template.update(DELETE, new Object[]{email.getId()});
	}

	@Override
	public List<EmailEntryData> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}

}
