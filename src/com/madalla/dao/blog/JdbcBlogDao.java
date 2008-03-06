package com.madalla.dao.blog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.madalla.service.blog.BlogEntry;

public class JdbcBlogDao extends NamedParameterJdbcDaoSupport implements BlogDao{
    
    private String site ;
    private final static String SQL_SELECT_ALL = 
        "select category.category_name, entry.entry_date, entry.entry_text from entry, category, site " +
        "where entry.category_id = category.category_id and " +
        "entry.site_id = site.site_id and " +
        "site_name = :site_name";
    private final static String SQL_SELECT_CATEGORY = "";
    private final static String SQL_INSERT = "";
    private final static String SQL_UPDATE = "";
    private final static String SQL_DELETE = "";
    
    public Collection<BlogEntry> getBlogEntriesForSite(){
        SqlParameterSource namedParameters = new MapSqlParameterSource("site_name", site);
        return getNamedParameterJdbcTemplate().queryForList(SQL_SELECT_ALL, namedParameters);
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




    public Collection<BlogEntry> getBlogentriesForCategory(String category) {
        // TODO Auto-generated method stub
        return null;
    }




    public void setSite(String site) {
        this.site = site;
    }
}
