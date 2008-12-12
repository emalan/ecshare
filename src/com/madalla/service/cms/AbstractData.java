package com.madalla.service.cms;

import java.io.Serializable;

public abstract class AbstractData implements Serializable {

	private static final long serialVersionUID = 1489298293050633840L;
    
	public abstract String getId();

    public abstract String getName();
}
