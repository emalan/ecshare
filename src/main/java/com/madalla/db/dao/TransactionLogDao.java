package com.madalla.db.dao;

import java.util.List;

import org.emalan.cms.bo.log.LogData;

public interface TransactionLogDao {

    public int create(final LogData data);
    public LogData find(String id);

    public int delete(LogData data);

    public List<LogData> fetch();

}
