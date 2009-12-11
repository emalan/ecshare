package com.madalla.webapp.upload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUploadStore implements IFileUploadInfo, Serializable{

	private static final long serialVersionUID = 1L;
	
	final private Map<String, IFileUploadStatus> statusMap;
	final private Map<FileUploadGroup, List<String>> groupMap;
	
	public FileUploadStore(){
		statusMap = new HashMap<String, IFileUploadStatus>();
		groupMap = new HashMap<FileUploadGroup, List<String>>();
	}
	
	public IFileUploadStatus getFileUploadStatus(String id) {
		return statusMap.get(id);
	}

	public void setFileUploadStatus(String id, IFileUploadStatus status) {
		statusMap.put(id, status);
	}

	public void setFileUploadStatus(String id, FileUploadGroup group, IFileUploadStatus status) {
		statusMap.put(id, status);
		List<String> list = groupMap.get(group);
		if (list == null){
			groupMap.put(group, list = new ArrayList<String>());
		}
		//no duplicates, but we want to replace so order is maintained
		if (list.contains(id)){
			list.remove(id);
		}
		list.add(id);	
	}

	public void setFileUploadComplete(String id) {
		statusMap.remove(id);
	}
	
	public void setGroupUploadComplete(FileUploadGroup group){
		List<String> list = groupMap.get(group);
		if (list != null){
			statusMap.keySet().removeAll(list);
		}
		groupMap.remove(group);
	}
	
	public List<String> getFileUploadStatus(FileUploadGroup group){
		List<String> list = groupMap.get(group);
		if (list == null) return Collections.emptyList();
		return list ;
//		Map<String, IFileUploadStatus> temp = new HashMap<String, IFileUploadStatus>(statusMap);
//		temp.keySet().retainAll(list);
//		return temp.values();
		
	}
	
	public void clear(){
		statusMap.clear();
		groupMap.clear();
	}

}
