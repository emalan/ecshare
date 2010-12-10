package com.madalla.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.madalla.bo.member.MemberData;

public class MemberDao extends AbstractDao {

	private static final String INSERT = "insert into MEMBER (SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD) values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String FIND = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where MEMBER_ID = ?";
	private static final String UPDATE = "update MEMBER set FIRST_NAME=?, LAST_NAME=?, COMPANY_NAME=?, MEMBER_EMAIL=?, PASSWORD=?,AUTHORIZED_DATE=?,SUBSCRIPTION_END_DATE=? where ID = ?";
	private static final String DELETE = "delete from MEMBER where ID = ?";
	private static final String FETCH = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where SITE_NAME = ?";
	private static final String GET = "select ID,SITE_NAME,MEMBER_ID,FIRST_NAME,LAST_NAME,COMPANY_NAME,MEMBER_EMAIL,SIGNUP_DATE,PASSWORD,AUTHORIZED_DATE,SUBSCRIPTION_END_DATE from MEMBER where ID = ?";

	private ParameterizedRowMapper<Member> mapper = new ParameterizedRowMapper<Member>() {

		public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
			Member data = new Member();
			data.setId(rs.getInt("ID"));
			data.setMemberId(rs.getString("MEMBER_ID"));
			data.setFirstName(rs.getString("FIRST_NAME"));
			data.setLastName(rs.getString("LAST_NAME"));
			data.setEmail(rs.getString("MEMBER_EMAIL"));
			data.setPassword(rs.getString("PASSWORD"));
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
		int count = 0;
		if (exists(data.getMemberId())){
			count = template.update(UPDATE, new Object[] { data.getFirstName(), data.getLastName(), data.getCompanyName(), 
					data.getEmail(), data.getPassword(), data.getAuthorizedDate() == null? null:data.getAuthorizedDate().toDate(), 
							data.getSubscriptionEnd(), data.getId()});
		} else {
			count = template.update(INSERT, new Object[] { site, data.getMemberId(), data.getFirstName(), data.getLastName(), data.getCompanyName(), 
					data.getEmail(), new DateTime(DateTimeZone.UTC).toDate(), data.getPassword() });
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
	
	
	public MemberData get(String id) {
		return this.template.queryForObject(GET, mapper, id);
	}
	

	public int delete(MemberData data){
		return template.update(DELETE, new Object[]{data.getId()});
	}
	
	public List<Member> fetch(){
		return template.query(FETCH, mapper, new Object[]{site});
	}
}
