package com.madalla.webapp.upload;

import java.io.InputStream;

public interface IFileUploadProcess {

	public void execute(InputStream inputStream, String string);
}
