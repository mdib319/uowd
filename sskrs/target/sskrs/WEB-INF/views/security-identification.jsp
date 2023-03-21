<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>
<title>Security Identification Utility</title>
</head>
<body style="background-color: #E6E6FA;">
	
	<a href="/sskrs/s-scrum-utilities"><img align="middle" src="/sskrs/resources/images/uowd-logo.png" /></a> 
	<h1>Security Identification Utility</h1>
	
	<form action="/sskrs/s-scrum-utilities/security-identification" method="post">
		<table>
			<thead>
				<tr>
					<th colspan="2">Search Software Product Features Catalog</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><label>Paradigm</label></td>
					<td><%=request.getAttribute("paradigms")%></td>
				</tr>
				<tr>
					<td>Subject Area</td>
					<td><%=request.getAttribute("subjects")%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><%=request.getAttribute("applications")%></td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input id="submit" name="submit" type="submit" alt="Search" value="Search" /></td>
				</tr>
			</tbody>
		</table>		
	</form>	

	<c:if test="${not empty result}">
		<input id="myInput" type="text" placeholder="Search..">

		<br>
		<br>

		<div style="overflow-x:auto;">
		<table class="styled-table">
			<thead>
				<tr>
					<th>Software Feature</th>
					<th>Security Requirement</th>
					<th>Security Error</th>
					<th>Vulnerability</th>
				</tr>
			</thead>
			<tbody id="myTable">
				<%=request.getAttribute("result")%>
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