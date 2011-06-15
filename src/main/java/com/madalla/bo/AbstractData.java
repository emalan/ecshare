package com.madalla.bo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Supplies common functionality for all Data objects
 * @author Eugene Malan
 *
 */
public abstract class AbstractData implements Serializable {

	private static final long serialVersionUID = 1489298293050633840L;

	public abstract String getId();

    public abstract String getName();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
