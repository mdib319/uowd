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
	
	<form:form action="/sskrs/security-acquisition/msr/manage?action=add" method="POST" modelAttribute="securityRequirement">
		
		<table>
			<tbody>
				<tr>
					<td><form:radiobutton path="newSecurityRequirment" value="N" label="Add New Security Requirement" /></td>
					<td>
						<form:label path="description">Name</form:label>
						<form:input path="description" />
					</td>
				</tr>
				<tr>
					<td><form:radiobutton path="newSecurityRequirment" value="E" label="Select Existing Security Requirement" /></td>
					<td>
						<form:select path="id" style="width: 200px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<form:options items="${securityRequirmentList}" itemValue="id" itemLabel="description" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="submit" value="Add" /></td>
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
					List<SecurityRequirement> srList = (ArrayList<SecurityRequirement>) request.getAttribute("result");
					
					for(SecurityRequirement sr : srList)
					{
						out.print("<tr>");
						out.print("<td>" + sr.getDescription() + "</td>");
						out.print("<td>");						
						out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/mse/manage\" modelAttribute=\"softwareFeature\" target=\"_blank\">");						
						out.print("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"" + sr.getId() + "\" />");						
						out.print("<input type=\"submit\" value=\"Manage Security Errors\" />");						
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