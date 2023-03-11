package org.uowd.sskrs.models;

public class VerificationRequest {
	private String paradigm;
	private String application;
	private String subject;
	private String language;
	private String technology;

	public String getParadigm() {
		return paradigm;
	}

	public void setParadigm(String paradigm) {
		this.paradigm = paradigm;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Override
	public String toString() {
		return "VerificationRequest [paradigm=" + paradigm + ", application=" + application + ", subject=" + subject
				+ ", language=" + language + ", technology=" + technology + "]";
	}
}
