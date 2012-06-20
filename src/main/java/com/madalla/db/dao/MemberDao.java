package com.madalla.db.dao;

import java.util.List;

import org.emalan.cms.bo.security.IUserValidate;

import com.madalla.bo.member.MemberData;

public interface MemberDao {

    public boolean save(final MemberData data);
    
    public boolean exists(String memberId);
    
    public Member findbyMemberId(String id);
    
    public IUserValidate getMemberValidator(final String id);
    
    public MemberData get(String id) ;
    
    public String getPassword(final String memberId) ;

    public boolean savePassword(final String memberId, final String password) ;
    
    public int delete(MemberData data);

    public List<Member> fetch();
}
