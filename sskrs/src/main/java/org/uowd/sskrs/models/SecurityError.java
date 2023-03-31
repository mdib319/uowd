package org.uowd.sskrs.models;

public class SecurityError {
	
	private String id;
	private String description;
	private String newSecurityError;
	
	public SecurityError() {
		this.id = "-1";
		this.description = "";
		this.newSecurityError = "";
	}

	public SecurityError(String id, String description) {
		super();
		this.id = id;
		this.description = description;
		this.newSecurityError = "";
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

	public String getNewSecurityError() {
		return newSecurityError;
	}

	public void setNewSecurityError(String newSecurityError) {
		this.newSecurityError = newSecurityError;
	}
}
