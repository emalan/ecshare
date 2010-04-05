package com.madalla;

import java.io.IOException;


public class BuildInformation {
	
	private String version;

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVersion(){
		return version;
	}
	
	private String getPackageInformation(){
		try {
			BuildInformation.class.getClassLoader().getSystemResources("META-INF/MANIFEST.MF");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "TODO";
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
	
}
