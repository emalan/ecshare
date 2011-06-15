package com.madalla.webapp.upload;

import java.io.Serializable;


/**
 * Store for File Upload Group information
 *
 * @author Eugene Malan
 *
 */
public class FileUploadGroup implements Serializable{
	private static final long serialVersionUID = -8817708187066185278L;

	final public String name;

	public FileUploadGroup(String name){
		this.name = name;
	}
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof FileUploadGroup)) return false;
		FileUploadGroup compare = (FileUploadGroup)obj;
		if (!name.equals(compare.name)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}


}
