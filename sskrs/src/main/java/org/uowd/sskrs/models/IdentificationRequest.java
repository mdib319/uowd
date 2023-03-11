package org.uowd.sskrs.models;

public class IdentificationRequest {
	private String paradigm;
	private String application;
	private String subject;

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

	@Override
	public String toString() {
		return "IdentificationRequest [paradigm=" + paradigm + ", application=" + application + ", subject=" + subject
				+ "]";
	}

}
