package org.uowd.sskrs.models;

public class SoftwareFeature {
	
	private int id;
	private String description;
	private String softwareParadigmId;
	private String softwareParadigmDescription;
	private String subjectAreaId;
	private String subjectAreaIdDescription;
	
	public SoftwareFeature() {
		super();
		
		this.id = -1;
		this.description = "";
		this.softwareParadigmId = "-1";
		this.softwareParadigmDescription = "";
		this.subjectAreaId = "-1";
		this.subjectAreaIdDescription = "";
	}
	
	public SoftwareFeature(int id, String description)
	{
		super();
		this.id = id;
		this.description = description;
		this.softwareParadigmId = "-1";
		this.softwareParadigmDescription = "";
		this.subjectAreaId = "-1";
		this.subjectAreaIdDescription = "";
	}

	public SoftwareFeature(int id, String description, 
			String softwareParadigmId, String softwareParadigmDescription, 
			String subjectAreaId, String subjectAreaDescription) {
		super();
		this.id = id;
		this.description = description;
		this.softwareParadigmId = softwareParadigmId;
		this.softwareParadigmDescription = softwareParadigmDescription;
		this.subjectAreaId = subjectAreaId;
		this.subjectAreaIdDescription = subjectAreaDescription;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSoftwareParadigmDescription() {
		return softwareParadigmDescription;
	}

	public void setSoftwareParadigmDescription(String softwareParadigmDescription) {
		this.softwareParadigmDescription = softwareParadigmDescription;
	}

	public String getSubjectAreaIdDescription() {
		return subjectAreaIdDescription;
	}

	public void setSubjectAreaIdDescription(String subjectAreaIdDescription) {
		this.subjectAreaIdDescription = subjectAreaIdDescription;
	}
}
