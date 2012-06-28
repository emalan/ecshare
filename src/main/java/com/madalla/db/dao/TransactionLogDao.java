package com.madalla.db.dao;

import org.emalan.cms.bo.log.LogData;
import org.emalan.cms.transaction.TransactionLogService;

public interface TransactionLogDao extends TransactionLogService{

    public int delete(LogData data);

}
