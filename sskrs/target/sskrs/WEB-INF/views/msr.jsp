<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.uowd.sskrs.models.SoftwareFeature"%>
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
	
	<form:form action="/sskrs/security-acquisition/msr" method="POST" modelAttribute="softwareFeature">
		<table>
			<thead>
				<tr>
					<th colspan="2" align="left">Search Software Features Catalog</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><form:label path="softwareParadigmId">Software Paradigm</form:label></td>
					<td>
						<form:select path="softwareParadigmId" name="softwareParadigmId" style="width: 200px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<form:options items="${softwareParadigmList}" itemValue="id" itemLabel="description" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td><form:label path="subjectAreaId">Subject Area</form:label></td>
					<td>
						<form:select path="subjectAreaId" name="subjectAreaId" style="width: 200px;">
							<form:option value="" selected="true">Please Select..</form:option>
							<form:options items="${subjectAreaList}" itemValue="id" itemLabel="description" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="submit" value="Search" /></td>
				</tr>
			</tbody>
		</table>		
	</form:form>	

	<c:if test="${not empty result}">
		<input id="myInput" type="text" placeholder="Search..">

		<br>
		<br>

		<div style="overflow-x:auto;">
		<table class="styled-table">
			<thead>
				<tr>
					<th>Software Feature</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="myTable">
				<%
					List<SoftwareFeature> sfList = (ArrayList<SoftwareFeature>) request.getAttribute("result");
					
					for(SoftwareFeature sf : sfList)
					{
						out.print("<tr>");
						out.print("<td>" + sf.getDescription() + "</td>");
						out.print("<td>");						
						out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msr/manage\" modelAttribute=\"softwareFeature\" target=\"_blank\">");						
						out.print("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"" + sf.getId() + "\" />");						
						out.print("<input type=\"hidden\" id=\"softwareParadigmId\" name=\"softwareParadigmId\" value=\"" + sf.getSoftwareParadigmId() + "\" />");						
						out.print("<input type=\"hidden\" id=\"subjectAreaId\" name=\"subjectAreaId\" value=\"" + sf.getSubjectAreaId() + "\" />");						
						out.print("<input type=\"submit\" value=\"Manage Security Requirements\" />");						
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