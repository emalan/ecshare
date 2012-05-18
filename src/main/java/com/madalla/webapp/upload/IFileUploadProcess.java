package com.madalla.webapp.upload;

import java.io.InputStream;
import java.io.Serializable;

public interface IFileUploadProcess extends Serializable{

	public void execute(InputStream inputStream, String string);
}
