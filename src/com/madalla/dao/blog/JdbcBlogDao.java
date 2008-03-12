package com.madalla.dao.blog;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.madalla.service.blog.BlogCategory;
import com.madalla.service.blog.BlogEntry;

public class JdbcBlogDao extends NamedParameterJdbcDaoSupport implements BlogDao, Serializable{
    
    private String site ;
    private int siteId;
    private final static String SQL_SITE_ID = "select SITE_ID from SITE where SITE_NAME = ?";
    private final static String SQL_CATEGORY = "select CATEGORY_ID, CATEGORY_NAME from CATEGORY";
    private final static String SQL_SELECT = "select ENTRY_ID, CATEGORY_ID, ENTRY_DATE, ENTRY_TEXT from ENTRY";
    private final static String SQL_SELECT_SITE_WHERE = " where SITE_ID = :siteId";
    private final static String SQL_SELECT_CATEGORY_WHERE = " and CATEGORY_ID = :category";
    
    private final static String SQL_EVENT_ALL = SQL_SELECT + SQL_SELECT_SITE_WHERE;
    private final static String SQL_EVENT_CATEGORY = SQL_EVENT_ALL + SQL_SELECT_CATEGORY_WHERE;
    
    private final static String SQL_EVENT_SELECT = "select ENTRY_ID, CATEGORY_ID, ENTRY_DATE, ENTRY_TEXT from ENTRY where ENTRY_ID = ?";
    
    private final static String SQL_INSERT = "insert into ENTRY (CATEGORY_ID,ENTRY_DATE,ENTRY_TEXT,SITE_ID) values(:blogCategoryId,:date,:text,:siteId)";
    private final static String SQL_UPDATE = "update ENTRY set CATEGORY_ID = :blogCategoryId, ENTRY_TEXT =:text, ENTRY_DATE = :date where ENTRY_ID = :id";
    private final static String SQL_DELETE = "delete from ENTRY where ENTRY.ENTRY_ID = ?";
    
    public void init(){
    }
    
    public List getBlogCategories(){
        return getJdbcTemplate().query(SQL_CATEGORY, new RowMapper(){

            public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new BlogCategory(resultSet.getInt("CATEGORY_ID"),resultSet.getString("CATEGORY_NAME") );
            }
            
        });
    }
    
    public List getBlogEntriesForSite(){
        SqlParameterSource namedParameters = new MapSqlParameterSource("siteId", getSiteId());
        return getNamedParameterJdbcTemplate().query(SQL_EVENT_ALL, namedParameters, new BlogRowMapper());
    }
    
    public List getBlogEntriesForCategory(int category) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("siteId", getSiteId());
        parameterSource.addValue("category", new Integer(category));
        return getNamedParameterJdbcTemplate().query(SQL_EVENT_CATEGORY, parameterSource, new BlogRowMapper());
    }
    
    public BlogEntry getBlogEntry(int blogEntryId){
        return (BlogEntry)getJdbcTemplate().queryForObject(SQL_EVENT_SELECT,new Object[]{new Integer(blogEntryId)}, new BlogRowMapper());
    }

    public int insertBlogEntry(BlogEntry blogEntry) {
    	blogEntry.setSiteId(getSiteId().intValue());
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(blogEntry);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(SQL_INSERT, parameterSource, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int saveBlogEntry(BlogEntry blogEntry) {
        blogEntry.setSiteId(getSiteId().intValue());
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(blogEntry);
        return getNamedParameterJdbcTemplate().update(SQL_UPDATE,parameterSource);
    }

    public void deleteBlogEntry(int blogEntryId) {
        getJdbcTemplate().update(SQL_DELETE, new Object[]{new Integer(blogEntryId)});
    }
    
    private static final class BlogRowMapper implements RowMapper{
        
        public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            BlogEntry blogEntry = new BlogEntry();
            blogEntry.setId(resultSet.getInt("ENTRY_ID"));
            blogEntry.setBlogCategory(new BlogCategory(resultSet.getInt("CATEGORY_ID"),"dummy"));
            blogEntry.setDate(resultSet.getDate("ENTRY_DATE"));
            blogEntry.setText(resultSet.getString("ENTRY_TEXT"));
            return blogEntry;
        }   
    }
    
    private Integer getSiteId(){
    	if (siteId == 0){
            siteId = getJdbcTemplate().queryForInt(SQL_SITE_ID,new String[]{site});
    	}
    	return new Integer(siteId);
    }

    public void setSite(String site) {
        this.site = site;
    }



}
