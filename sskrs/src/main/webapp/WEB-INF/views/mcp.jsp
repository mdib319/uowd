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

<title>Manage Construction Practices</title>
</head>
<body style="background-color: #E6E6FA;">

	<a href="/sskrs/security-acquisition"><img align="middle"
		src="/sskrs/resources/images/uowd-logo.png" /></a>
	<h1>Manage Construction Practices</h1>
	
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

	<form:form action="/sskrs/security-acquisition/mcp" method="POST"
		modelAttribute="constructionPracticeManager">

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
					<td colspan="2"><form:radiobutton path="selectConstructionPractice" value="E" label="Select Existing Construction Practice" /></td>
				</tr>
				<tr>
					<td colspan="2">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:select path="constructionPracticeId" style="width: 300px;">
							<form:option value="" selected="true">Please Select..</form:option>							
							<c:forEach var="cp" items="${constructionPracticeList}">  
								<form:option value="${cp.id}" label="${cp.description}" title="${cp.description}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan="2"><form:radiobutton path="selectConstructionPractice" value="N" label="Add New Construction Practice" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:label path="constructionPracticeDescription">Construction Description:</form:label>						
					</td>
					<td><form:input path="constructionPracticeDescription" size="38" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="followStrategy" value="1"/>Follow Strategy: 
					</td>
					<td><form:input path="strategyDescription" size="38" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="hasMethod" value="1"/>Has Method: 
					</td>
					<td style="background-color: #FFFFFF;"><form:textarea path="methodDetails" /></td>					
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="relatedLanguage" value="1"/>Related Language: 
					</td>
					<td><form:input path="language" size="38" /></td>					
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="hasMechanism" value="1"/>Has Mechanism: 
					</td>
					<td><form:input path="mechanism" size="38" /></td>					
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="mechanismUtilizesSecurityTool" value="1"/>Mechanism utilizes Security Tool: 
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
			  $('#methodDetails').summernote({
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
				
				$("input[name=selectConstructionPractice]").change(function () {			
					
					if(this.value == 'N')
					{
						disableAllFields(false);	
					}
					else if(this.value == 'E')
					{
						disableAllFields(true);
					}
				});
				
				$("#followStrategy1").change(function () {
					
					$("#strategyDescription").prop('disabled', !this.checked);
				});
				
				$("#hasMethod1").change(function () {
					
					$("#methodDetails").summernote((!this.checked) ? 'disable' : 'enable');
					$("#relatedLanguage1").prop('disabled', !this.checked);
					$("#hasMechanism1").prop('disabled', !this.checked);
					
					$("#language").prop('disabled', !this.checked || !$("#relatedLanguage1").is(':checked'));
					
					$("#mechanism").prop('disabled', !this.checked || !$("#hasMechanism1").is(':checked'));
					
					$("#mechanismUtilizesSecurityTool1").prop('disabled', !this.checked || !$("#hasMechanism1").is(':checked'));
					$("#securityTool").prop('disabled', !this.checked || !$("#hasMechanism1").is(':checked') || !$("#mechanismUtilizesSecurityTool1").is(':checked'));
				});
				
				$("#relatedLanguage1").change(function () {
					
					$("#language").prop('disabled', !this.checked);
				});
				
				$("#hasMechanism1").change(function () {
					
					$("#mechanism").prop('disabled', !this.checked);				
					$("#mechanismUtilizesSecurityTool1").prop('disabled', !this.checked);
				});
				
				$("#mechanismUtilizesSecurityTool1").change(function () {
					
					$("#securityTool").prop('disabled', !this.checked);
				});
				
				function disableAllFields(disable)
				{
					$("#constructionPracticeId").prop('disabled', !disable);
					$("#constructionPracticeDescription").prop('disabled', disable);
					$("#followStrategy1").prop('disabled', disable);					
					$("#hasMethod1").prop('disabled', disable);
					$("#relatedLanguage1").prop('disabled', disable || !$("#hasMethod1").is(':checked'));
					$("#hasMechanism1").prop('disabled', disable || !$("#hasMethod1").is(':checked'));
					
					$("#strategyDescription").prop('disabled', disable || !$("#followStrategy1").is(':checked'));
					$("#language").prop('disabled', disable || !$("#relatedLanguage1").is(':checked'));
					$("#mechanism").prop('disabled', disable || !$("#hasMechanism1").is(':checked'));				
					$("#mechanismUtilizesSecurityTool1").prop('disabled', disable || !$("#hasMethod1").is(':checked') || !$("#hasMechanism1").is(':checked'));
					$("#securityTool").prop('disabled', disable || !$("#hasMethod1").is(':checked') || !$("#hasMechanism1").is(':checked') || !$("#mechanismUtilizesSecurityTool1").is(':checked'));
					$("#methodDetails").summernote((disable || !$("#hasMethod1").is(':checked')) ? 'disable' : 'enable');
				}
				
				var selectConPrac = $("input[name=selectConstructionPractice]:checked");
				
				if(selectConPrac.val() == 'N')
				{
					disableAllFields(false);	
				}
				else if(selectConPrac.val() == 'E')
				{
					disableAllFields(true);
				}
		});
	</script>

</body>
</html>