package com.madalla.service.cms;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class BackupFile implements Serializable, Comparable<BackupFile> {


	private static final long serialVersionUID = 1L;
	private File file;
	
	public BackupFile(File file) {
		this.file = file;
	}
	
	public static File getBackupFile(String fileName, File repositoryHomeDir,  String fileSuffix) throws IOException {
		
        String dateTimeString = ISODateTimeFormat.basicDateTime().print(new LocalDateTime());

		File backupFile = new File(repositoryHomeDir,fileName+"-backup-"+dateTimeString+fileSuffix);
        
		return backupFile;
	}
	
	public File getFile(){
		return file;
	}
	public String getDisplayName(){
		return file.getName()+"zzz";
	}
	public String getName(){
		return file.getName();
	}

	public int compareTo(BackupFile o) {
		return o.getFile().compareTo(file);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof BackupFile)) return false;
		BackupFile compare = (BackupFile)obj;
		return file.equals(compare.getFile());
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
