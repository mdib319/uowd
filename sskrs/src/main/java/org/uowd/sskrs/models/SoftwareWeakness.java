package org.uowd.sskrs.models;

public class SoftwareWeakness {
	
	private String id;
	private String description;
	private String newSoftwareWeakness;
	
	public SoftwareWeakness() {
		this.id = "-1";
		this.description = "";
		this.newSoftwareWeakness = "";
	}

	public SoftwareWeakness(String id, String description) {
		super();
		this.id = id;
		this.description = description;
		this.newSoftwareWeakness = "";
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

	public String getNewSoftwareWeakness() {
		return newSoftwareWeakness;
	}

	public void setNewSoftwareWeakness(String newSoftwareWeakness) {
		this.newSoftwareWeakness = newSoftwareWeakness;
	}
}
