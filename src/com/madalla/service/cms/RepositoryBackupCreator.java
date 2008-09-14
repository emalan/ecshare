package com.madalla.service.cms;

public class RepositoryBackupCreator {

	private static RepositoryBackupCreator instance;
	
	private IRepositoryAdminService repositoryAdminService;
	
	public RepositoryBackupCreator(IRepositoryAdminService repositoryAdminService){
		this.repositoryAdminService = repositoryAdminService;
		instance = this;
	}
	
	public static RepositoryBackupCreator getInstance(){
		return instance;
	}
	
	public String backupRepository(){
		return repositoryAdminService.backupContentRoot();
	}
}
