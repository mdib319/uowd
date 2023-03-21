<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.uowd.sskrs.models.SoftwareParadigm"%>
<%@ page import="java.util.List"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>
<title>Manage Software Paradigm</title>
</head>
<body style="background-color: #E6E6FA;">

	<a href="/sskrs/security-acquisition"><img align="middle"
		src="/sskrs/resources/images/uowd-logo.png" /></a>
	<h1>Manage Software Paradigm</h1>
	
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

	<div>
		<form:form method="POST"
			action="/sskrs/security-acquisition/msp?action=add"
			modelAttribute="softwareParadigm">
			<table>
				<tr>
					<td><form:label path="description">Name</form:label></td>
					<td><form:input path="description" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="submit" value="Add" /></td>
				</tr>
			</table>
		</form:form>
	</div>

	<div>
		<table border="1" class="styled-table">
			<%
				List<SoftwareParadigm> spList = (List<SoftwareParadigm>) request.getAttribute("softwareParadigmList");
				
				if(spList.size() > 0)
				{
					out.print("<thead><tr><th>Sequence #</th><th>Software Paradigm</th><th>Action</th></tr></thead>");
				}

				for (int i = 1; i <= spList.size(); i++) {
					
					SoftwareParadigm sp = spList.get(i - 1);
					
					out.print("<tbody>");

					out.print("<tr>");
					
					out.print("<td>" + i + "</td>");
					
					out.print("<td>" + sp.getDescription() + "</td>");
					
					out.print("<td>");
					
					out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msp?action=delete\" modelAttribute=\"softwareParadigm\">");
					
					out.print("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"" + sp.getId() + "\" />");
					
					out.print("<input type=\"hidden\" id=\"description\" name=\"description\" value=\"" + sp.getDescription() + "\" />");
					
					out.print("<input type=\"submit\" value=\"Delete\" />");
					
					out.print("</form>");
					
					out.print("</td>");
					
					out.print("</tr>");
					
					out.print("</tbody>");
				}
			%>
		</table>
	</div>

</body>
</html>