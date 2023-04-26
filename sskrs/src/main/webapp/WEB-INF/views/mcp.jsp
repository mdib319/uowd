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
						</form:select></td>
				</tr>
				<tr>
					<td><form:label path="securityErrorId">Select Security Error:</form:label></td>
					<td><form:select path="securityErrorId"
							style="width: 500px;">
							<form:option value="" selected="true">Please Select..</form:option>
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
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:checkbox path="mechanismUtilizesSecurityTool" value="1"/>Mechanism utilizes security tool: 
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
	</script>

</body>
</html>