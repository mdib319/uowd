package org.uowd.sskrs.models;

public class VerificationPracticeManager {
	
	private String softwareFeatureId;
	private String securityRequirementId;
	private String securityErrorId;
	private String selectVerificationPractice;
	private String verificationPracticeId;
	private String verificationPracticeDescription;
	private String hasApproach;
	private String approachDescription;
	private String hasTechnique;
	private String techniqueDetails;
	private String hasSecurityTool;
	private String securityTool;
	
	public VerificationPracticeManager() {
		super();
		
		this.softwareFeatureId = "-1";
		this.securityRequirementId = "-1";
		this.securityErrorId = "-1";
		this.selectVerificationPractice = "E";
		this.verificationPracticeId = "-1";
		this.verificationPracticeDescription = "";
		this.hasApproach = "0";
		this.approachDescription = "";
		this.hasTechnique = "0";
		this.techniqueDetails = "";
		this.hasSecurityTool = "0";
		this.securityTool = "";
	}

	public VerificationPracticeManager(String softwareFeatureId, String securityRequirementId, String securityErrorId,
			String selectVerificationPractice, String verificationPracticeId, String verificationPracticeDescription,
			String hasApproach, String approachDescription, String hasTechnique, String techniqueDetails,
			String hasSecurityTool, String securityTool) {
		super();
		this.softwareFeatureId = softwareFeatureId;
		this.securityRequirementId = securityRequirementId;
		this.securityErrorId = securityErrorId;
		this.selectVerificationPractice = selectVerificationPractice;
		this.verificationPracticeId = verificationPracticeId;
		this.verificationPracticeDescription = verificationPracticeDescription;
		this.hasApproach = hasApproach;
		this.approachDescription = approachDescription;
		this.hasTechnique = hasTechnique;
		this.techniqueDetails = techniqueDetails;
		this.hasSecurityTool = hasSecurityTool;
		this.securityTool = securityTool;
	}

	public String getSoftwareFeatureId() {
		return softwareFeatureId;
	}

	public void setSoftwareFeatureId(String softwareFeatureId) {
		this.softwareFeatureId = softwareFeatureId;
	}

	public String getSecurityRequirementId() {
		return securityRequirementId;
	}

	public void setSecurityRequirementId(String securityRequirementId) {
		this.securityRequirementId = securityRequirementId;
	}

	public String getSecurityErrorId() {
		return securityErrorId;
	}

	public void setSecurityErrorId(String securityErrorId) {
		this.securityErrorId = securityErrorId;
	}

	public String getSelectVerificationPractice() {
		return selectVerificationPractice;
	}

	public void setSelectVerificationPractice(String selectVerificationPractice) {
		this.selectVerificationPractice = selectVerificationPractice;
	}

	public String getVerificationPracticeId() {
		return verificationPracticeId;
	}

	public void setVerificationPracticeId(String verificationPracticeId) {
		this.verificationPracticeId = verificationPracticeId;
	}

	public String getVerificationPracticeDescription() {
		return verificationPracticeDescription;
	}

	public void setVerificationPracticeDescription(String verificationPracticeDescription) {
		this.verificationPracticeDescription = verificationPracticeDescription;
	}

	public String getHasApproach() {
		return hasApproach;
	}

	public void setHasApproach(String hasApproach) {
		this.hasApproach = hasApproach;
	}

	public String getApproachDescription() {
		return approachDescription;
	}

	public void setApproachDescription(String approachDescription) {
		this.approachDescription = approachDescription;
	}

	public String getHasTechnique() {
		return hasTechnique;
	}

	public void setHasTechnique(String hasTechnique) {
		this.hasTechnique = hasTechnique;
	}

	public String getTechniqueDetails() {
		return techniqueDetails;
	}

	public void setTechniqueDetails(String techniqueDetails) {
		this.techniqueDetails = techniqueDetails;
	}

	public String getHasSecurityTool() {
		return hasSecurityTool;
	}

	public void setHasSecurityTool(String hasSecurityTool) {
		this.hasSecurityTool = hasSecurityTool;
	}

	public String getSecurityTool() {
		return securityTool;
	}

	public void setSecurityTool(String securityTool) {
		this.securityTool = securityTool;
	}

	@Override
	public String toString() {
		return "VerificationPracticeManager [softwareFeatureId=" + softwareFeatureId + ", securityRequirementId="
				+ securityRequirementId + ", securityErrorId=" + securityErrorId + ", selectVerificationPractice="
				+ selectVerificationPractice + ", verificationPracticeId=" + verificationPracticeId
				+ ", verificationPracticeDescription=" + verificationPracticeDescription + ", hasApproach="
				+ hasApproach + ", approachDescription=" + approachDescription + ", hasTechnique=" + hasTechnique
				+ ", techniqueDetails=" + techniqueDetails + ", hasSecurityTool=" + hasSecurityTool + ", securityTool="
				+ securityTool + "]";
	}	
}
