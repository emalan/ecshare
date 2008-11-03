package com.madalla.service.cms;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class BackupFile implements Serializable, Comparable<BackupFile> {

	private static final long serialVersionUID = 1L;
	private static final String BACKUP = "-backup-";
	private static final String FILE_SUFFIX = ".xml";
	private static final Log log = LogFactory.getLog(BackupFile.class);
	private File file;
	
	public BackupFile(File file) {
		this.file = file;
	}
	
	public static File getBackupFile(String fileName, String repositoryHome) throws IOException {
		
        String dateTimeString = ISODateTimeFormat.basicDateTime().print(new LocalDateTime());

		File backupFile = new File(getRepositoryHomeDir(repositoryHome),fileName+BACKUP+dateTimeString+FILE_SUFFIX);
        
		return backupFile;
	}
	
	public static List<BackupFile> getFileList(final String fileStart, String repositoryHome){
    	File repositoryHomeDir;
		try {
			repositoryHomeDir = getRepositoryHomeDir(repositoryHome);
		} catch (IOException e) {
			log.error("Unable to get Repository Home Directory.", e);
			return null;
		}
    	File[] files = repositoryHomeDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(name.endsWith(FILE_SUFFIX)){
                    if (name.startsWith(fileStart)){
                        return true;
                    }
				}
				return false;
			}
    	});
        log.debug("getBackupFileList - Number of backup files found="+files.length);
        List<BackupFile> ret = new ArrayList<BackupFile>();
        for(int i = 0; i < files.length; i++){
        	ret.add(new BackupFile(files[i]));
        }
    	return ret;
    }
	
	private static File getRepositoryHomeDir(String repositoryHome) throws IOException{
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(repositoryHome);
        return resource.getFile();
	}
	
	public File getFile(){
		return file;
	}
	public String getDisplayName(){
		String s = file.getName();
		String site = StringUtils.substringBefore(s, BACKUP);
		String rt;
		try {
			String dateTimeString = StringUtils.substringBefore(StringUtils.substringAfter(s, BACKUP),FILE_SUFFIX);
			DateTime dateTime = ISODateTimeFormat.basicDateTime().parseDateTime(dateTimeString+"Z");
			DateTimeFormatter df;
			if (dateTime != null && dateTime.isAfter(new DateMidnight())){
				df = DateTimeFormat.forPattern("'Today' - HH:mm:ss.SSS");
			} else if (dateTime != null && dateTime.isAfter(new DateMidnight().minusDays(1))) {
				df = DateTimeFormat.forPattern("'Yesterday' - HH:mm:ss.SSS");
			} else {
				df = DateTimeFormat.forPattern("MMM dd, YYYY - HH:mm:ss.SSS");
			}
			rt = df.print(dateTime);
		} catch (Exception e){
			log.error("getDisplayName - unable to parse backup file date.", e);
			return s;
		}
		return rt;
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
