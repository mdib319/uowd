<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.uowd.sskrs.models.SoftwareParadigm"%>
<%@ page import="org.uowd.sskrs.models.SubjectArea"%>
<%@ page import="org.uowd.sskrs.models.SoftwareFeature"%>
<%@ page import="org.uowd.sskrs.models.SecurityRequirement"%>
<%@ page import="org.uowd.sskrs.models.SecurityRequirementManager" %>
<%@ page import="org.uowd.sskrs.models.SecurityErrorManager" %>
<%@ page import="java.util.List"%>


<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>
<title>Manage Security Requirements</title>
</head>
<body style="background-color: #E6E6FA;">
	
	<a href="/sskrs/security-acquisition"><img align="middle" src="/sskrs/resources/images/uowd-logo.png" /></a> 
	<h1>Manage Security Requirements</h1>
	
	<div>
		<p><strong>Software Paradigm:</strong> <c:out value="${softwareParadigm.description}" /></p>
	</div>
	<div>
		<p><strong>Subject Area:</strong> <c:out value="${subjectArea.description}" /></p>
	</div>
	<div>
		<p><strong>Software Feature:</strong> <c:out value="${softwareFeature.description}" /></p>
	</div>
	
	<hr />
	
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
	
	<form:form action="/sskrs/security-acquisition/msr/manage?action=add" method="POST" modelAttribute="securityRequirementManager">
		<form:hidden path="softwareFeatureId" />
		<form:hidden path="softwareParadigmId" />
		<form:hidden path="subjectAreaId" />
		<table>
			<tbody>
				<tr>
					<td><form:radiobutton path="securityRequirementNewSecurityRequirment" value="E" label="Select Existing Security Requirement" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:select path="securityRequirementId" style="width: 300px;">
							<form:option value="" selected="true">Please Select..</form:option>							
							<c:forEach var="sr" items="${securityRequirmentList}">  
								<form:option value="${sr.id}" label="${sr.description}" title="${sr.description}" />  
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td><form:radiobutton path="securityRequirementNewSecurityRequirment" value="N" label="Add New Security Requirement" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:input path="securityRequirementDescription" size="38" />
					</td>
				</tr>
				<tr>
					<td align="right"><input type="submit" value="Add" /></td>
				</tr>
			</tbody>
		</table>		
	</form:form>
	
	<hr />	

	<c:if test="${not empty result}">
		<input id="myInput" type="text" placeholder="Search..">

		<br>
		<br>

		<div style="overflow-x:auto;">
		<table class="styled-table">
			<thead>
				<tr>
					<th>Security Requirement</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="myTable">
				<%
					SoftwareParadigm sp = (SoftwareParadigm) request.getAttribute("softwareParadigm");
					SubjectArea sa = (SubjectArea) request.getAttribute("subjectArea");
					SoftwareFeature sf = (SoftwareFeature) request.getAttribute("softwareFeature");
				
					List<SecurityRequirement> srList = (ArrayList<SecurityRequirement>) request.getAttribute("result");
					
					for(SecurityRequirement sr : srList)
					{
						out.print("<tr>");
						out.print("<td>" + sr.getDescription() + "</td>");
						out.print("<td>");						
						
						out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/mse/manage\" modelAttribute=\"securityErrorManager\" target=\"_blank\">");						
						out.print("<input type=\"hidden\" id=\"securityRequirementId\" name=\"securityRequirementId\" value=\"" + sr.getId() + "\" />");						
						out.print("<input type=\"submit\" value=\"Manage Security Errors\" />");						
						out.print("</form>");
						
						out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msr/manage?action=delete\" modelAttribute=\"securityRequirementManager\">");
						out.print("<input type=\"hidden\" id=\"SoftwareParadigmId\" name=\"SoftwareParadigmId\" value=\"" + sp.getId() + "\" />");
						out.print("<input type=\"hidden\" id=\"subjectAreaId\" name=\"subjectAreaId\" value=\"" + sa.getId() + "\" />");
						out.print("<input type=\"hidden\" id=\"softwareFeatureId\" name=\"softwareFeatureId\" value=\"" + sf.getId() + "\" />");
						out.print("<input type=\"hidden\" id=\"securityRequirementId\" name=\"securityRequirementId\" value=\"" + sr.getId() + "\" />");						
						out.print("<input type=\"submit\" value=\"Remove\" />");
						out.print("</form>");
						
						out.print("</td>");
						out.print("</tr>");
					}
				%>
			</tbody>
		</table>
		</div>
	</c:if>

</body>

<c:if test="${not empty result}">
	<script>
	$(document).ready(function(){
		  $("#myInput").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    $("#myTable tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
		  });
		});
	</script>
</c:if>

</html>