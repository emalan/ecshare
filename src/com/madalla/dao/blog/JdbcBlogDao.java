package com.madalla.dao.blog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.madalla.service.blog.BlogCategory;
import com.madalla.service.blog.BlogEntry;

public class JdbcBlogDao extends NamedParameterJdbcDaoSupport implements BlogDao{
    
    private String site ;
    private final static String SQL_CATEGORY = "select CATEGORY_ID, CATEGORY_NAME from CATEGORY";
    private final static String SQL_SELECT = "select ENTRY.ENTRY_ID, ENTRY.CATEGORY_ID, ENTRY.ENTRY_DATE, ENTRY.ENTRY_TEXT from ENTRY, SITE";
    private final static String SQL_SELECT_SITE_JOIN = " where ENTRY.SITE_ID = SITE.SITE_ID";
    private final static String SQL_SELECT_SITE_WHERE = " and SITE.SITE_NAME = :site";
    private final static String SQL_SELECT_CATEGORY_WHERE = " and ENTRY.CATEGORY_ID = :category";
    
    private final static String SQL_EVENT_ALL = SQL_SELECT + SQL_SELECT_SITE_JOIN + SQL_SELECT_SITE_WHERE;
    private final static String SQL_EVENT_CATEGORY = SQL_EVENT_ALL + SQL_SELECT_CATEGORY_WHERE;
    
    private final static String SQL_EVENT_SELECT = "select ENTRY_ID, CATEGORY_ID, ENTRY_DATE, ENTRY_TEXT from ENTRY where ENTRY_ID = ?";
    
    private final static String SQL_INSERT = "insert into ENTRY (CATEGORY_ID,ENTRY_DATE,ENTRY_TEXT,SITE_ID) values(:category,:date,:text,:siteId)";
    private final static String SQL_UPDATE = "update ENTRY set CATEGORY_ID = :category, ENTRY_TEXT =:text, ENTRY_DATE = :date where ENTRY_ID = :id";
    private final static String SQL_DELETE = "delete from ENTRY where ENTRY.ENTRY_ID = ?";
    
    public Collection<BlogCategory> getBlogCategories(){
        return getJdbcTemplate().query(SQL_CATEGORY, new RowMapper(){

            public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                BlogCategory category = new BlogCategory();
                category.setId(resultSet.getInt("CATEGORY_ID"));
                category.setName(resultSet.getString("CATEGORY_NAME"));
                return category;
            }
            
        });
    }
    
    public Collection<BlogEntry> getBlogEntriesForSite(){
        SqlParameterSource namedParameters = new MapSqlParameterSource("site", site);
        return getNamedParameterJdbcTemplate().query(SQL_EVENT_ALL, namedParameters, new BlogRowMapper());
    }
    
    public Collection<BlogEntry> getBlogEntriesForCategory(int category) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("site", site);
        parameterSource.addValue("category", category);
        return getNamedParameterJdbcTemplate().query(SQL_EVENT_CATEGORY, parameterSource, new BlogRowMapper());
    }
    
    public BlogEntry getBlogEntry(int blogEntryId){
        return (BlogEntry)getJdbcTemplate().queryForObject(SQL_EVENT_SELECT,new Object[]{blogEntryId}, new BlogRowMapper());
    }

    public int insertBlogEntry(BlogEntry blogEntry) {
    	
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(blogEntry);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(SQL_INSERT, parameterSource, keyHolder);
        //return getJdbcTemplate().queryForInt("values IDENTITY_VAL_LOCAL()");
        return keyHolder.getKey().intValue();
    }

    public int saveBlogEntry(BlogEntry blogEntry) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(blogEntry);
        return getNamedParameterJdbcTemplate().update(SQL_UPDATE,parameterSource);
    }

    public void deleteBlogEntry(int blogEntryId) {
        getJdbcTemplate().update(SQL_DELETE, new Object[]{blogEntryId});
    }
    
    private static final class BlogRowMapper implements RowMapper{
        
        public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            BlogEntry blogEntry = new BlogEntry();
            blogEntry.setId(resultSet.getInt("ENTRY_ID"));
            blogEntry.setCategory(resultSet.getInt("CATEGORY_ID"));
            blogEntry.setDate(resultSet.getDate("ENTRY_DATE"));
            blogEntry.setText(resultSet.getString("ENTRY_TEXT"));
            return blogEntry;
        }   
    }

    public void setSite(String site) {
        this.site = site;
    }



}
