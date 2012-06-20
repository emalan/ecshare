package com.madalla.db.dao.springjdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.madalla.db.dao.AbstractDao;

public class AbstractSpringDao extends AbstractDao{

    protected JdbcTemplate template;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

}
