<!DOCTYPE html>

<%@page import="org.uowd.sskrs.models.SoftwareWeakness"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.uowd.sskrs.models.SecurityRequirement"%>
<%@ page import="org.uowd.sskrs.models.SecurityErrorManager" %>
<%@ page import="org.uowd.sskrs.models.SecurityError" %>
<%@ page import="org.uowd.sskrs.models.SoftwareWeaknessManager" %>
<%@ page import="org.uowd.sskrs.models.SoftwareWeakness" %>
<%@ page import="java.util.List"%>


<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>
<title>Manage Software Weaknesses</title>
</head>
<body style="background-color: #E6E6FA;">
	
	<a href="/sskrs/security-acquisition"><img align="middle" src="/sskrs/resources/images/uowd-logo.png" /></a> 
	<h1>Manage Software Weaknesses</h1>
	
	<div>
		<p><strong>Security Error:</strong> <c:out value="${securityError.description}" /></p>
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
	
	<form:form action="/sskrs/security-acquisition/msw/manage?action=add" method="POST" modelAttribute="softwareWeaknessManager">
		<form:hidden path="securityErrorId" />
		<table>
			<tbody>
				<tr>
					<td><form:radiobutton path="softwareWeaknessNewSoftwareWeakness" value="E" label="Select Existing Software Weakness" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:select path="softwareWeaknessId" style="width: 300px;">
							<form:option value="" selected="true">Please Select..</form:option>							
							<c:forEach var="sw" items="${softwareWeaknessList}">  
								<form:option value="${sw.id}" label="${sw.description}" title="${sw.description}" />  
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td><form:radiobutton path="softwareWeaknessNewSoftwareWeakness" value="N" label="Add New Software Weakness" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:input path="softwareWeaknessDescription" size="38" />
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
					<th>Software Weakness</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="myTable">
				<%
					SecurityError se = (SecurityError) request.getAttribute("securityError");
				
					List<SoftwareWeakness> swList = (ArrayList<SoftwareWeakness>) request.getAttribute("result");
					
					for(SoftwareWeakness sw : swList)
					{
						out.print("<tr>");
						out.print("<td>" + sw.getDescription() + "</td>");
						out.print("<td>");
						
						out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msw/manage?action=delete\" modelAttribute=\"softwareWeaknessManager\">");
						out.print("<input type=\"hidden\" id=\"securityErrorId\" name=\"securityErrorId\" value=\"" + se.getId() + "\" />");
						out.print("<input type=\"hidden\" id=\"softwareWeaknessId\" name=\"softwareWeaknessId\" value=\"" + sw.getId() + "\" />");						
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