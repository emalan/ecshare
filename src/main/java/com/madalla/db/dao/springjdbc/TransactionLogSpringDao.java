package com.madalla.db.dao.springjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.madalla.db.dao.TransactionLog;
import com.madalla.db.dao.TransactionLogDao;

@Component
@Qualifier("Spring")
public class TransactionLogSpringDao extends AbstractSpringDao implements TransactionLogDao {

	private static final String INSERT = "insert into TRANSACTION_LOG (SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID) values (?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID from TRANSACTION_LOG where ID = ?";
	private static final String DELETE = "delete from TRANSACTION_LOG where ID = ?";
	private static final String FETCH = "select ID,SITE_NAME,LOG_DATE,USER_ID,TYPE,OBJECT_ID from TRANSACTION_LOG where SITE_NAME = ?";

	private ParameterizedRowMapper<TransactionLog> mapper = new ParameterizedRowMapper<TransactionLog>() {

		public TransactionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		    TransactionLog data = new TransactionLog();
			data.setKey(rs.getLong("ID"));
			data.setDateTime(new DateTime(rs.getTimestamp("LOG_DATE"), DateTimeZone.UTC));
			data.setUser(rs.getString("USER_ID"));
			data.setType(rs.getString("TYPE"));
			data.setCmsId(rs.getString("OBJECT_ID"));
			data.setSite(rs.getString("SITE_NAME"));
			return data;
		}
	};

	@Override
	public LogData find(String id) {
		return this.template.queryForObject(FIND, mapper, id);
	}

	@Override
	public int delete(LogData data){
		return template.update(DELETE, new Object[]{data.getId()});
	}

	@Override
	public List<? extends LogData> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}

    @Override
    public void create(String user, String type, String id) {
        template.update(INSERT, new Object[] { site, new DateTime(DateTimeZone.UTC).toDate(), user, type, id });
        
    }

}
