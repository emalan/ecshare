package com.madalla;

public class BuildInformation {

	private String version;
	private String webappVersion;

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setWebappVersion(String webappVersion) {
		this.webappVersion = webappVersion;
	}

	public String getWebappVersion() {
		return webappVersion;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
