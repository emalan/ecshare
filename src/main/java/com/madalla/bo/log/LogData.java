package com.madalla.bo.log;

import com.madalla.bo.AbstractData;

public abstract class LogData extends AbstractData implements ILogData, Comparable<LogData>{
	private static final long serialVersionUID = 1845745562650629210L;

	public int compareTo(LogData compare) {
		return compare.getId().compareTo(getId());
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof LogData)) return false;
		LogData compare = (LogData)obj;
		if (!getId().equals(compare.getId())) return false;
		return true;
	}
}
