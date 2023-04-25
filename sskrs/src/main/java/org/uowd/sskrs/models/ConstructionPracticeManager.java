package org.uowd.sskrs.models;

public class ConstructionPracticeManager {
	
	private String softwareFeatureId;
	private String securityRequirementId;
	private String securityErrorId;
	private String selectConstructionPractice;
	private String constructionPracticeId;
	private String constructionPracticeDescription;
	private String followStrategy;
	private String strategyDescription;
	private String hasMethod;
	private String methodDetails;
	
	public ConstructionPracticeManager() {
		super();
		
		this.softwareFeatureId = "-1";
		this.securityRequirementId = "-1";
		this.securityErrorId = "-1";
		this.selectConstructionPractice = "E";
		this.constructionPracticeId = "-1";
		this.constructionPracticeDescription = "";
		this.followStrategy = "0";
		this.strategyDescription = "";
		this.hasMethod = "0";
		this.methodDetails = "";
	}

	public ConstructionPracticeManager(String softwareFeatureId, String securityRequirementId, String securityErrorId, String selectConstructionPractice, 
			String constructionPracticeId, String constructionPracticeDescription, String followStrategy, String strategyDescription,
			String hasMethod, String methodDetails) {
		super();
		this.softwareFeatureId = softwareFeatureId;
		this.securityRequirementId = securityRequirementId;
		this.securityErrorId = securityErrorId;
		this.selectConstructionPractice = selectConstructionPractice;
		this.constructionPracticeId = constructionPracticeId;
		this.constructionPracticeDescription = constructionPracticeDescription;
		this.followStrategy = followStrategy;
		this.strategyDescription = strategyDescription;
		this.hasMethod = hasMethod;
		this.methodDetails = methodDetails;
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

	public String getSelectConstructionPractice() {
		return selectConstructionPractice;
	}

	public void setSelectConstructionPractice(String selectConstructionPractice) {
		this.selectConstructionPractice = selectConstructionPractice;
	}

	public String getConstructionPracticeId() {
		return constructionPracticeId;
	}

	public void setConstructionPracticeId(String constructionPracticeId) {
		this.constructionPracticeId = constructionPracticeId;
	}

	public String getConstructionPracticeDescription() {
		return constructionPracticeDescription;
	}

	public void setConstructionPracticeDescription(String constructionPracticeDescription) {
		this.constructionPracticeDescription = constructionPracticeDescription;
	}

	public String getFollowStrategy() {
		return followStrategy;
	}

	public void setFollowStrategy(String followStrategy) {
		this.followStrategy = followStrategy;
	}

	public String getStrategyDescription() {
		return strategyDescription;
	}

	public void setStrategyDescription(String strategyDescription) {
		this.strategyDescription = strategyDescription;
	}

	public String getHasMethod() {
		return hasMethod;
	}

	public void setHasMethod(String hasMethod) {
		this.hasMethod = hasMethod;
	}

	public String getMethodDetails() {
		return methodDetails;
	}

	public void setMethodDetails(String methodDetails) {
		this.methodDetails = methodDetails;
	}
}
