package org.uowd.sskrs.models;

public class ImplementationRequest {
	private String softwareParadigmId;
	private String subjectAreaId;
	
	public ImplementationRequest() {
		super();
		
		this.softwareParadigmId = "-1";
		this.subjectAreaId = "-1";
	}
	
	public ImplementationRequest(String softwareParadigmId, String subjectAreaId) {
		super();
		this.softwareParadigmId = softwareParadigmId;
		this.subjectAreaId = subjectAreaId;
	}

	public String getSoftwareParadigmId() {
		return softwareParadigmId;
	}

	public void setSoftwareParadigmId(String softwareParadigmId) {
		this.softwareParadigmId = softwareParadigmId;
	}

	public String getSubjectAreaId() {
		return subjectAreaId;
	}

	public void setSubjectAreaId(String subjectAreaId) {
		this.subjectAreaId = subjectAreaId;
	}

	@Override
	public String toString() {
		return "ImplementationRequest [softwareParadigmId=" + softwareParadigmId + ", subjectAreaId=" + subjectAreaId
				+ "]";
	}

}
