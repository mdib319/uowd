package org.uowd.sskrs.models;

public class SoftwareWeaknessManager {
	
	private String securityErrorId;
	private String securityErrorDescription;
	
	private String softwareWeaknessId;
	private String softwareWeaknessDescription;
	private String softwareWeaknessNewSoftwareWeakness;
	
	public SoftwareWeaknessManager() {
		super();
		
		this.securityErrorId = "-1";
		this.securityErrorDescription = "";
		
		this.softwareWeaknessId = "-1";
		this.softwareWeaknessDescription = "";
		this.softwareWeaknessNewSoftwareWeakness = "";
	}

	public SoftwareWeaknessManager(String securityErrorId, String securityErrorDescription,
			String softwareWeaknessId, String softwareWeaknessDescription, String softwareWeaknessNewSoftwareWeakness) {
		super();
		this.securityErrorId = securityErrorId;
		this.securityErrorDescription = securityErrorDescription;
		this.softwareWeaknessId = softwareWeaknessId;
		this.softwareWeaknessDescription = softwareWeaknessDescription;
		this.softwareWeaknessNewSoftwareWeakness = softwareWeaknessNewSoftwareWeakness;
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

	public String getSoftwareWeaknessId() {
		return softwareWeaknessId;
	}

	public void setSoftwareWeaknessId(String softwareWeaknessId) {
		this.softwareWeaknessId = softwareWeaknessId;
	}

	public String getSoftwareWeaknessDescription() {
		return softwareWeaknessDescription;
	}

	public void setSoftwareWeaknessDescription(String softwareWeaknessDescription) {
		this.softwareWeaknessDescription = softwareWeaknessDescription;
	}

	public String getSoftwareWeaknessNewSoftwareWeakness() {
		return softwareWeaknessNewSoftwareWeakness;
	}

	public void setSoftwareWeaknessNewSoftwareWeakness(String softwareWeaknessNewSoftwareWeakness) {
		this.softwareWeaknessNewSoftwareWeakness = softwareWeaknessNewSoftwareWeakness;
	}
}
