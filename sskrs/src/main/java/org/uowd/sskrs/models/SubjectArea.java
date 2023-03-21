package org.uowd.sskrs.models;

public class SubjectArea {
	
	private int id;
	private String description;
	
	public SubjectArea() {
		super();
		
		this.id = -1;
		this.description = "";
	}

	public SubjectArea(int id, String description) {
		super();
		this.id = id;
		this.description = description;
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

}
