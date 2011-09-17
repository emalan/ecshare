package com.madalla.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.madalla.bo.member.MemberData;
import com.madalla.bo.security.IUserValidate;

public class MemberDao extends AbstractDao {

	private static final Log log = LogFactory.getLog(MemberDao.class);

	private static final String INSERT = "insert into MEMBER (SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where MEMBER_ID = ?";
	private static final String UPDATE = "update MEMBER set FIRST_NAME=?, LAST_NAME=?, COMPANY_NAME=?, MEMBER_EMAIL=?,AUTHORIZED_DATE=?,SUBSCRIPTION_END_DATE=? where ID = ?";
	private static final String DELETE = "delete from MEMBER where ID = ?";
	private static final String FETCH = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where SITE_NAME = ?";
	private static final String GET = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where ID = ?";
	private static final String PWDGET = "select PASSWORD from MEMBER where MEMBER_ID = ?";
	private static final String PWDSAVE = "update MEMBER set PASSWORD=? where MEMBER_ID = ?";
	

	private ParameterizedRowMapper<Member> mapper = new ParameterizedRowMapper<Member>() {

		public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			Member data = new Member();
			data.setId(rs.getInt("ID"));
			data.setMemberId(rs.getString("MEMBER_ID"));
			data.setFirstName(rs.getString("FIRST_NAME"));
			data.setLastName(rs.getString("LAST_NAME"));
			data.setEmail(rs.getString("MEMBER_EMAIL"));
			data.setCompanyName(StringUtils.defaultString(rs.getString("COMPANY_NAME")));
			data.setSignupDate(new DateTime(rs.getTimestamp("SIGNUP_DATE"), DateTimeZone.UTC));
			Timestamp authDate = rs.getTimestamp("AUTHORIZED_DATE");
			if (authDate != null){
				data.setAuthorizedDate(new DateTime(authDate, DateTimeZone.UTC));
			}
			data.setSubscriptionEnd(rs.getDate("SUBSCRIPTION_END_DATE"));
			return data;
		}
	};

	/**
	 * Designed to enforce memberId as unique. If member exists with that memberId, we do update.
	 * @param data
	 * @return
	 */
	public boolean save(final MemberData data){
		log.info("save - " + data);
		int count = 0;
		if (exists(data.getMemberId())){
			count = template.update(UPDATE, new Object[] { data.getFirstName(), data.getLastName(), data.getCompanyName(),
					data.getEmail(), data.getAuthorizedDate() == null? null:data.getAuthorizedDate().toDate(),
							data.getSubscriptionEnd(), data.getId()});
		} else {
			count = template.update(INSERT, new Object[] { site, data.getMemberId(), data.getFirstName(), data.getLastName(), data.getCompanyName(),
					data.getEmail(), new DateTime(DateTimeZone.UTC).toDate() });
			//update member with id
			if (data instanceof Member){
				Member existing = (Member) data;
				MemberData newOne = findbyMemberId(data.getMemberId());
				existing.setId(Integer.valueOf(newOne.getId()));
			}

		}
		return count == 1;
	}


	/**
	 * Exists using memberId
	 * @param id
	 * @return
	 */
	public boolean exists(String memberId){
		return !(findbyMemberId(memberId) == null);
	}

	public Member findbyMemberId(String id) {
		try {
			return this.template.queryForObject(FIND, mapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public IUserValidate getMemberValidator(final String id) {
		return new IUserValidate() {
			
			public String getPassword() {
				return template.queryForObject(PWDGET, String.class, new Object[]{id});
			}
			
			public String getName() {
				return id;
			}
		};
	}

	public MemberData get(String id) {
		return this.template.queryForObject(GET, mapper, id);
	}
	
	public String getPassword(final String memberId) {
		return template.queryForObject(PWDGET, String.class, new Object[]{memberId});
	}

	public boolean savePassword(final String memberId, final String password) {
		log.info("saving password. " + memberId);
		return template.update(PWDSAVE, new Object[]{password, memberId}) == 1;
		
	}
	
	public int delete(MemberData data){
		return template.update(DELETE, new Object[]{data.getId()});
	}

	public List<Member> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}
	
}
