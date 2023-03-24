package org.uowd.sskrs.models;

public class SecurityRequirementManager {
	
	private String softwareFeatureId;
	private String softwareFeatureDescription;
	private String softwareParadigmId;
	private String softwareParadigmDescription;
	private String subjectAreaId;
	private String subjectAreaIdDescription;
	
	private String securityRequirementId;
	private String securityRequirementDescription;
	private String securityRequirementNewSecurityRequirment;
	
	public SecurityRequirementManager() {
		super();
		
		this.softwareFeatureId = "-1";
		this.softwareFeatureDescription = "";
		this.softwareParadigmId = "-1";
		this.softwareParadigmDescription = "";
		this.subjectAreaId = "-1";
		this.subjectAreaIdDescription = "";
		this.securityRequirementId = "-1";
		this.securityRequirementDescription = "";
		this.securityRequirementNewSecurityRequirment = "";
	}	

	public SecurityRequirementManager(String softwareFeatureId, String softwareFeatureDescription,
			String softwareParadigmId, String softwareParadigmDescription, String subjectAreaId,
			String subjectAreaIdDescription, String securityRequirementId, String securityRequirementDescription,
			String securityRequirementNewSecurityRequirment) {
		super();
		
		this.softwareFeatureId = softwareFeatureId;
		this.softwareFeatureDescription = softwareFeatureDescription;
		this.softwareParadigmId = softwareParadigmId;
		this.softwareParadigmDescription = softwareParadigmDescription;
		this.subjectAreaId = subjectAreaId;
		this.subjectAreaIdDescription = subjectAreaIdDescription;
		this.securityRequirementId = securityRequirementId;
		this.securityRequirementDescription = securityRequirementDescription;
		this.securityRequirementNewSecurityRequirment = securityRequirementNewSecurityRequirment;
	}

	public String getSoftwareFeatureId() {
		return softwareFeatureId;
	}

	public void setSoftwareFeatureId(String softwareFeatureId) {
		this.softwareFeatureId = softwareFeatureId;
	}

	public String getSoftwareFeatureDescription() {
		return softwareFeatureDescription;
	}

	public void setSoftwareFeatureDescription(String softwareFeatureDescription) {
		this.softwareFeatureDescription = softwareFeatureDescription;
	}

	public String getSoftwareParadigmId() {
		return softwareParadigmId;
	}

	public void setSoftwareParadigmId(String softwareParadigmId) {
		this.softwareParadigmId = softwareParadigmId;
	}

	public String getSoftwareParadigmDescription() {
		return softwareParadigmDescription;
	}

	public void setSoftwareParadigmDescription(String softwareParadigmDescription) {
		this.softwareParadigmDescription = softwareParadigmDescription;
	}

	public String getSubjectAreaId() {
		return subjectAreaId;
	}

	public void setSubjectAreaId(String subjectAreaId) {
		this.subjectAreaId = subjectAreaId;
	}

	public String getSubjectAreaIdDescription() {
		return subjectAreaIdDescription;
	}

	public void setSubjectAreaIdDescription(String subjectAreaIdDescription) {
		this.subjectAreaIdDescription = subjectAreaIdDescription;
	}

	public String getSecurityRequirementId() {
		return securityRequirementId;
	}

	public void setSecurityRequirementId(String securityRequirementId) {
		this.securityRequirementId = securityRequirementId;
	}

	public String getSecurityRequirementDescription() {
		return securityRequirementDescription;
	}

	public void setSecurityRequirementDescription(String securityRequirementDescription) {
		this.securityRequirementDescription = securityRequirementDescription;
	}

	public String getSecurityRequirementNewSecurityRequirment() {
		return securityRequirementNewSecurityRequirment;
	}

	public void setSecurityRequirementNewSecurityRequirment(String securityRequirementNewSecurityRequirment) {
		this.securityRequirementNewSecurityRequirment = securityRequirementNewSecurityRequirment;
	}
	
	

}
