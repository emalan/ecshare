package com.madalla.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.madalla.bo.log.LogData;

public class TransactionLogDao extends AbstractDao {

	private static final String INSERT = "insert into TRANSACTION_LOG (SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID) values (?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID from TRANSACTION_LOG where ID = ?";
	private static final String DELETE = "delete from TRANSACTION_LOG where ID = ?";
	private static final String FETCH = "select ID,SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID from TRANSACTION_LOG where SITE_NAME = ?";

	private ParameterizedRowMapper<LogData> mapper = new ParameterizedRowMapper<LogData>() {

		public LogData mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransactionLog data = new TransactionLog();
			data.setId(rs.getInt("ID"));
			data.setDateTime(new DateTime(rs.getTimestamp("LOG_DATE"), DateTimeZone.UTC));
			data.setUser(rs.getString("USER_ID"));
			data.setType(rs.getString("TYPE"));
			data.setCmsId(rs.getString("OBJECT_ID"));
			return data;
		}
	};

	public int create(final LogData data) {
		return template.update(INSERT, new Object[] { site, new DateTime(DateTimeZone.UTC).toDate(), data.getUser(), data.getType(), data.getCmsId() });
	}

	public LogData find(String id) {
		return this.template.queryForObject(FIND, mapper, id);
	}

	public int delete(LogData data){
		return template.update(DELETE, new Object[]{data.getId()});
	}

	public List<LogData> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}

}
