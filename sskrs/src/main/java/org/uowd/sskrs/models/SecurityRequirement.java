package org.uowd.sskrs.models;

public class SecurityRequirement {
	
	private String id;
	private String description;
	private String newSecurityRequirment;
	
	public SecurityRequirement() {
		this.id = "-1";
		this.description = "";
		this.newSecurityRequirment = "";
	}

	public SecurityRequirement(String id, String description) {
		super();
		this.id = id;
		this.description = description;
		this.newSecurityRequirment = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewSecurityRequirment() {
		return newSecurityRequirment;
	}

	public void setNewSecurityRequirment(String newSecurityRequirment) {
		this.newSecurityRequirment = newSecurityRequirment;
	}
}
