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
<title>Manage Software Feature</title>
</head>
<body style="background-color: #E6E6FA;">

	<a href="/sskrs/security-acquisition"><img align="middle"
		src="/sskrs/resources/images/uowd-logo.png" /></a>
	<h1>Manage Software Feature</h1>
	
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
	
	<%
		String action = request.getParameter("action");
	%>

	<div>
		<%
			if(action.contentEquals("initEdit") || action.contentEquals("edit"))
			{
		%>
				<table>
					<tr>
						<td>
							<form:form method="POST" action="/sskrs/security-acquisition/msf?action=edit" modelAttribute="softwareFeature">
								
								<form:hidden path="id" />
								
								<table>
									<tr>
										<td><form:label path="description">Name</form:label></td>
										<td><form:input path="description" /></td>
									</tr>
									<tr>
										<td><form:label path="softwareParadigmId">Software Paradigm</form:label></td>
										<td>
											<form:select path="softwareParadigmId" name="softwareParadigmId">
												<form:option value="" selected="true">Please Select..</form:option>
												<form:options items="${softwareParadigmList}" itemValue="id" itemLabel="description" />
											</form:select>
										</td>
									</tr>
									<tr>
										<td><form:label path="subjectAreaId">Subject Area</form:label></td>
										<td>
											<form:select path="subjectAreaId" name="subjectAreaId">
												<form:option value="" selected="true">Please Select..</form:option>
												<form:options items="${subjectAreaList}" itemValue="id" itemLabel="description" />
											</form:select>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="right"><input type="submit" value="Cancel" onclick="$('#initForm').submit(); return false;" />&nbsp;<input type="submit" value="Edit" /></td>
									</tr>
								</table>
							</form:form>
						</td>
					</tr>
				</table>				
				
				<form id="initForm" method="POST" action="/sskrs/security-acquisition/msf?action=init"></form>
		<%
			}
			else
			{
		%>
				<form:form method="POST" action="/sskrs/security-acquisition/msf?action=add" modelAttribute="softwareFeature">
					<table>
						<tr>
							<td><form:label path="description">Name</form:label></td>
							<td><form:input path="description" /></td>
						</tr>
						<tr>
							<td><form:label path="softwareParadigmId">Software Paradigm</form:label></td>
							<td>
								<form:select path="softwareParadigmId" name="softwareParadigmId">
									<form:option value="" selected="true">Please Select..</form:option>
									<form:options items="${softwareParadigmList}" itemValue="id" itemLabel="description" />
								</form:select>
							</td>
						</tr>
						<tr>
							<td><form:label path="subjectAreaId">Subject Area</form:label></td>
							<td>
								<form:select path="subjectAreaId" name="subjectAreaId">
									<form:option value="" selected="true">Please Select..</form:option>
									<form:options items="${subjectAreaList}" itemValue="id" itemLabel="description" />
								</form:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right"><input type="submit" value="Add" /></td>
						</tr>
					</table>
				</form:form>
		<%
			}
		%>
	</div>

	<div>
		<table border="1" class="styled-table">
			<%
				List<SoftwareFeature> sfList = (List<SoftwareFeature>) request.getAttribute("softwareFeatureList");
				
				if(sfList.size() > 0)
				{
					out.print("<thead><tr><th>Sequence #</th><th>Software Feature</th><th>Software Paradigm</th><th>Subject Area</th><th>Action</th></tr></thead>");
				}

				for (int i = 1; i <= sfList.size(); i++) {
					
					SoftwareFeature sf = sfList.get(i - 1);
					
					out.print("<tbody>");

					out.print("<tr>");
					
					out.print("<td>" + i + "</td>");
					
					out.print("<td>" + sf.getDescription() + "</td>");
					
					out.print("<td>" + sf.getSoftwareParadigmDescription() + "</td>");
					
					out.print("<td>" + sf.getSubjectAreaIdDescription() + "</td>");
					
					out.print("<td>");
					
					out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msf?action=delete\" modelAttribute=\"softwareFeature\">");
					
					out.print("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"" + sf.getId() + "\" />");
					
					out.print("<input type=\"hidden\" id=\"description\" name=\"description\" value=\"" + sf.getDescription() + "\" />");
					
					out.print("<input type=\"submit\" value=\"Delete\" />");
					
					out.print("</form>");
					
					out.print("<form method=\"POST\" action=\"/sskrs/security-acquisition/msf?action=initEdit\" modelAttribute=\"softwareFeature\">");
					
					out.print("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"" + sf.getId() + "\" />");
					
					out.print("<input type=\"hidden\" id=\"description\" name=\"description\" value=\"" + sf.getDescription() + "\" />");
					
					out.print("<input type=\"hidden\" id=\"softwareParadigmId\" name=\"softwareParadigmId\" value=\"" + sf.getSoftwareParadigmId() + "\" />");
					
					out.print("<input type=\"hidden\" id=\"subjectAreaId\" name=\"subjectAreaId\" value=\"" + sf.getSubjectAreaId() + "\" />");
					
					out.print("<input type=\"submit\" value=\"Edit\" />");
					
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