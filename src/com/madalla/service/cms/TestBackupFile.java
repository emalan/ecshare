package com.madalla.service.cms;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class TestBackupFile extends TestCase{

	public void testBackupFile() throws IOException{
		
		
		File file = BackupFile.getBackupFile("test", new File("test"), ".xml");
		String fileName = file.getName();
	}
}
