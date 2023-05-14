<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.uowd.sskrs.models.SecurityRequirementManager"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>

<link rel="stylesheet" href="/sskrs/resources/summernote/summernote-lite.css" />
<script src="/sskrs/resources/summernote/summernote-lite.js"></script>

<title>Manage Verification Practices</title>
</head>
<body style="background-color: #E6E6FA;">

	<a href="/sskrs/security-acquisition"><img align="middle"
		src="/sskrs/resources/images/uowd-logo.png" /></a>
	<h1>Manage Verification Practices</h1>
	
	<div style="margin-bottom: 10px;">
		<c:choose>
			<c:when test="${status == '1'}">
				<span style="color: green;">${message}</span>
			</c:when>
			<c:otherwise>
				<span style="color: red;">${message}</span>
			</c:otherwise>
		</c:choose>
	</div>

	<form:form action="/sskrs/security-acquisition/mvp" method="POST"
		modelAttribute="verificationPracticeManager">

		<table>
			<tbody>
				<tr>
					<td><form:label path="softwareFeatureId">Select Software Feature:</form:label></td>
					<td><form:select path="softwareFeatureId"
							style="width: 500px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<c:forEach var="sf" items="${softwareFeatureList}">
								<form:option value="${sf.id}" label="${sf.description}"
									title="${sf.description}" />
							</c:forEach>
						</form:select></td>
				</tr>
				<tr>
					<td><form:label path="securityRequirementId">Select Security Requirement:</form:label></td>
					<td><form:select path="securityRequirementId"
							style="width: 500px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<c:forEach var="sr" items="${srList}">
								<form:option value="${sr.id}" label="${sr.description}"
									title="${sr.description}" />
							</c:forEach>
						</form:select></td>
				</tr>
				<tr>
					<td><form:label path="securityErrorId">Select Security Error:</form:label></td>
					<td><form:select path="securityErrorId"
							style="width: 500px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<c:forEach var="se" items="${seList}">
								<form:option value="${se.id}" label="${se.description}"
									title="${se.description}" />
							</c:forEach>
						</form:select></td>
				</tr>
				<tr>
					<td colspan="2"><hr /></td>
				</tr>
				<tr>
					<td colspan="2"><form:radiobutton path="selectVerificationPractice" value="E" label="Select Existing Verification Practice" /></td>
				</tr>
				<tr>
					<td colspan="2">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:select path="verificationPracticeId" style="width: 300px;">
							<form:option value="" selected="true">Please Select..</form:option>							
							<c:forEach var="vp" items="${verificationPracticeList}">  
								<form:option value="${vp.id}" label="${vp.description}" title="${vp.description}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan="2"><form:radiobutton path="selectVerificationPractice" value="N" label="Add New Verification Practice" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:label path="verificationPracticeDescription">Verification Description:</form:label>						
					</td>
					<td><form:input path="verificationPracticeDescription" size="38" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="hasApproach" value="1"/>Has Approach: 
					</td>
					<td><form:input path="approachDescription" size="38" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="hasTechnique" value="1"/>Has Technique: 
					</td>
					<td style="background-color: #FFFFFF;"><form:textarea path="techniqueDetails" /></td>					
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="hasSecurityTool" value="1"/>Has Security Tool: 
					</td>
					<td><form:input path="securityTool" size="38" /></td>					
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="submit"
						value="Submit" />
					</td>						
				</tr>
			</tbody>
		</table>

	</form:form>

	<script>
	
		$(document).ready(function() {
			  $('#techniqueDetails').summernote({
				  height: 300,
				  width: 800
			  });
			  
			  $("#softwareFeatureId").change(function() {
					
					var slctSubcat = $('#securityRequirementId'), slctSubcat1 = $('#securityErrorId'), option = "";
					
					slctSubcat.empty();
					slctSubcat1.empty();
					
					option = "<option selected='true' value='''>Please Select..</option>";
					
					slctSubcat1.append(option);

					if (!$(this).val()) {
						slctSubcat.append(option);
					} else {
						var softwareFeatureId = $(this).val();

						$.ajax({
						type : 'GET',
						url : "/sskrs/security-acquisition/srl/" + softwareFeatureId,
						success : function(data) {
							
							for (var i = 0; i < data.length; i++) {
								option = option + "<option value='"+data[i].id + "' title='" + data[i].description + "'>" + data[i].description + "</option>";
							}
							
							slctSubcat.append(option);
						},
						error : function(xhr, status, error) {
							  var err = eval("(" + xhr.responseText + ")");
							  alert(err.Message);
							}

						});
					}

				});
				
				$("#securityRequirementId").change(function() {
					
					var slctSubcat = $('#securityErrorId'), option = "";
					
					slctSubcat.empty();

					if (!$(this).val()) {				
						option = "<option selected='true' value='''>Please Select..</option>";
						slctSubcat.append(option);				
					} else {
						var securityRequirementId = $(this).val();

						$.ajax({
						type : 'GET',
						url : "/sskrs/security-acquisition/sel/" + securityRequirementId,
						success : function(data) {					
							option = "<option selected='true' value='''>Please Select..</option>";

							for (var i = 0; i < data.length; i++) {
								option = option + "<option value='"+data[i].id + "' title='" + data[i].description + "'>" + data[i].description + "</option>";
							}
							
							slctSubcat.append(option);
						},
						error : function(xhr, status, error) {
							  var err = eval("(" + xhr.responseText + ")");
							  alert(err.Message);
							}

						});
					}

				});
				
				$("input[name=selectVerificationPractice]").change(function () {			
					
					if(this.value == 'N')
					{
						disableAllFields(false);	
					}
					else if(this.value == 'E')
					{
						disableAllFields(true);
					}
				});
				
				$("#hasApproach1").change(function () {
					
					$("#approachDescription").prop('disabled', !this.checked);
				});
				
				$("#hasTechnique1").change(function () {
					
					$("#techniqueDetails").summernote((!this.checked) ? 'disable' : 'enable');
					$("#hasSecurityTool1").prop('disabled', !this.checked);					
					$("#securityTool").prop('disabled', !this.checked || !$("#hasSecurityTool1").is(':checked'));
				});
				
				$("#hasSecurityTool1").change(function () {
					
					$("#securityTool").prop('disabled', !this.checked);
				});
				
				function disableAllFields(disable)
				{
					$("#verificationPracticeId").prop('disabled', !disable);
					$("#verificationPracticeDescription").prop('disabled', disable);
					$("#hasApproach1").prop('disabled', disable);					
					$("#hasTechnique1").prop('disabled', disable);
					$("#hasSecurityTool1").prop('disabled', disable || !$("#hasTechnique1").is(':checked'));
					
					$("#approachDescription").prop('disabled', disable || !$("#hasApproach1").is(':checked'));
					$("#securityTool").prop('disabled', disable || !$("#hasSecurityTool1").is(':checked'));

					$("#techniqueDetails").summernote((disable || !$("#hasTechnique1").is(':checked')) ? 'disable' : 'enable');
				}
				
				var selectVerPrac = $("input[name=selectVerificationPractice]:checked");
				
				if(selectVerPrac.val() == 'N')
				{
					disableAllFields(false);	
				}
				else if(selectVerPrac.val() == 'E')
				{
					disableAllFields(true);
				}
		});
	</script>

</body>
</html>