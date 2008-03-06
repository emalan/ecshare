package com.madalla.dao.blog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.madalla.service.blog.BlogEntry;

public class JdbcBlogDao extends NamedParameterJdbcDaoSupport implements BlogDao{
    
    private String site ;
    private final static String SQL_SELECT_ALL = 
        "select CATEGORY.CATEGORY_NAME, ENTRY.ENTRY_DATE, ENTRY.ENTRY_TEXT from ENTRY, CATEGORY, SITE " +
        "where ENTRY.CATEGORY_ID = CATEGORY.CATEGORY_ID and " +
        "ENTRY.SITE_ID = SITE.SITE_ID and " +
        "SITE.SITE_NAME = :SITENAME";
    private final static String SQL_SELECT_CATEGORY = SQL_SELECT_ALL +
        " and CATEGORY.CATEGORY_NAME = :categoryName";
    private final static String SQL_INSERT = "insert into ENTRY";
    private final static String SQL_UPDATE = "update ENTRY";
    private final static String SQL_DELETE = "delete from ENTRY where ENTRY.ENTRY_ID = ?";
    
    public Collection<BlogEntry> getBlogEntriesForSite(){
        SqlParameterSource namedParameters = new MapSqlParameterSource("siteName", site);
        return getNamedParameterJdbcTemplate().queryForList(SQL_SELECT_ALL, namedParameters);
    }
    
    public Collection<BlogEntry> getBlogEntriesForCategory(String category) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("siteName", site);
        return getNamedParameterJdbcTemplate().queryForList(SQL_SELECT_CATEGORY, parameterSource);
    }

    public int insertBlogEntry(BlogEntry blogEntry) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(blogEntry);
        return getNamedParameterJdbcTemplate().update(SQL_INSERT, parameterSource);
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
            blogEntry.setCategory(resultSet.getString("CATEGORY_NAME"));
            blogEntry.setDate(resultSet.getDate("ENTRY_DATE"));
            blogEntry.setText(resultSet.getString("SITE_NAME"));
            return null;
        }   
    }

    public void setSite(String site) {
        this.site = site;
    }

}
