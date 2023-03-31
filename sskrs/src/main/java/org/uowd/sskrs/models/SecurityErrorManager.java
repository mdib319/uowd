package org.uowd.sskrs.models;

public class SecurityErrorManager {
	
	private String securityRequirementId;
	private String securityRequirementDescription;
	
	private String securityErrorId;
	private String securityErrorDescription;
	private String securityErrorNewSecurityRequirment;
	
	public SecurityErrorManager() {
		super();
		
		this.securityRequirementId = "-1";
		this.securityRequirementDescription = "";
		
		this.securityErrorId = "-1";
		this.securityErrorDescription = "";
		this.securityErrorNewSecurityRequirment = "";
	}

	public SecurityErrorManager(String securityRequirementId, String securityRequirementDescription,
			String securityErrorId, String securityErrorDescription, String securityErrorNewSecurityRequirment) {
		super();
		this.securityRequirementId = securityRequirementId;
		this.securityRequirementDescription = securityRequirementDescription;
		this.securityErrorId = securityErrorId;
		this.securityErrorDescription = securityErrorDescription;
		this.securityErrorNewSecurityRequirment = securityErrorNewSecurityRequirment;
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

	public String getSecurityErrorId() {
		return securityErrorId;
	}

	public void setSecurityErrorId(String securityErrorId) {
		this.securityErrorId = securityErrorId;
	}

	public String getSecurityErrorDescription() {
		return securityErrorDescription;
	}

	public void setSecurityErrorDescription(String securityErrorDescription) {
		this.securityErrorDescription = securityErrorDescription;
	}

	public String getSecurityErrorNewSecurityRequirment() {
		return securityErrorNewSecurityRequirment;
	}

	public void setSecurityErrorNewSecurityRequirment(String securityErrorNewSecurityRequirment) {
		this.securityErrorNewSecurityRequirment = securityErrorNewSecurityRequirment;
	}
}
