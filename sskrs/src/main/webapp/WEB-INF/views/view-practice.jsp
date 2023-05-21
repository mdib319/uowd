<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/sskrs/resources/css/all.css" />
<script src="/sskrs/resources/js/jquery-3.6.0.min.js"></script>

<link rel="stylesheet"
	href="/sskrs/resources/summernote/summernote-lite.css" />
<script src="/sskrs/resources/summernote/summernote-lite.js"></script>

<title>View Practice Details</title>
</head>
<body style="background-color: #E6E6FA;">
	<a><img align="middle"
		src="/sskrs/resources/images/uowd-logo.png" /></a>
	<h1>View Practice Details</h1>

	<c:choose>
		<c:when test="${param.type eq 'construction' }">
			<div>
				<span><b>Construction Practice:</b></span> <c:out value="${description}"></c:out>
			</div>
			
			<hr />

			<%
				Object tempCpList = request.getAttribute("cpList");

						if (tempCpList != null && tempCpList instanceof List<?>) {
							List<HashMap<String, Object>> cpList = (List<HashMap<String, Object>>) tempCpList;
							
							if(!cpList.isEmpty())
							{
								out.println("<h2>Abstract</h2>");
								
								out.println("<ul>");
	
								for (HashMap<String, Object> cpMap : cpList) {
									out.println("<li><p><i>" + (String) cpMap.get("CONSTRUCTION PRACTICE") + "</i>"
											+ " is used to construct " + "<i>" + (String) cpMap.get("SOFTWARE FEATURE") + "</i>"
											+ " and is followed to satisfy security requirement <i>" + (String) cpMap.get("SECURITY REQUIREMENT") + "</i>"
											+ " in order to mitigate security error <i>" + (String) cpMap.get("SECURITY ERROR") + "</i>"
											+ " which causes software weakness <i>" + (String) cpMap.get("SOFTWARE WEAKNESS") + "</i></p></li>");								
								}
								
								out.println("</ul>");
							}
						}

						out.println("<hr />");

						Object tempPList = request.getAttribute("pList");

						if (tempPList != null && tempPList instanceof List<?>) {
							List<HashMap<String, Object>> pList = (List<HashMap<String, Object>>) tempPList;
							
							if(!pList.isEmpty())
							{
								out.println("<h2>Description</h2>");
								
								int counter = 1;

								for (HashMap<String, Object> pMap : pList) {
									
									out.println("<h3>Method #" + counter + "</h3>");
									
									out.println("<ul>");
									
									if(pMap.get("STRATEGY") != null)
									{
										out.println("<li><p><b>Strategy Followed:</b> " + (String) pMap.get("STRATEGY") + "</p></li>");
									}
									
									if(pMap.get("METHOD") != null)
									{
										out.println("<li><p><b>Method Details:</b>"); 
										
										if(pMap.get("LANGUAGE") != null)
										{
											out.println("<ul><li><p><b>Related Language:</b>" + (String) pMap.get("LANGUAGE") + "</p></li></ul>");
										}
										
										if(pMap.get("MECHANISM") != null)
										{
											out.println("<ul><li><p><b>Method Mechanism:</b>" + (String) pMap.get("MECHANISM") + "</p>");
											
											if(pMap.get("SECURITY TOOL") != null)
											{
												out.println("<ul><li><p><b>Security tool utilized:</b>" + (String) pMap.get("SECURITY TOOL") + "</p></li></ul>");
											}
																										
											out.println("</li></ul>");
										}
										
										out.println("<iframe style=\"height:300px;width:100%; background: #FFFFFF;\" frameborder=\"0\" srcdoc=\"" + (String) pMap.get("METHOD") + "\">"
										+ "</iframe></p></li>");
									}
									
									out.println("</ul>");
									
									out.println("<hr style=\"width:50%\" />");
									
									counter ++;
								}
							}
						}
			%>
		</c:when>
		<c:otherwise>
			<div>
				<span><b>Verification Practice:</b></span> <c:out value="${description}"></c:out>
			</div>
			
			<hr />

			<%
				Object tempVpList = request.getAttribute("vpList");

						if (tempVpList != null && tempVpList instanceof List<?>) {
							List<HashMap<String, Object>> vpList = (List<HashMap<String, Object>>) tempVpList;
							
							if(!vpList.isEmpty())
							{
								out.println("<h2>Abstract</h2>");
								
								out.println("<ul>");
	
								for (HashMap<String, Object> vpMap : vpList) {
									out.println("<li><p><i>" + (String) vpMap.get("VERIFICATION PRACTICE") + "</i>"
											+ " is used to verify if the security requirement " + "<i>" + (String) vpMap.get("SECURITY REQUIREMENT") + "</i>"
											+ " that is associated with software feature <i>" + (String) vpMap.get("SOFTWARE FEATURE") + "</i>"
											+ " is satisfied and used to spot the security error <i>" + (String) vpMap.get("SECURITY ERROR") + "</i>"
											+ " which causes software weakness <i>" + (String) vpMap.get("SOFTWARE WEAKNESS") + "</i></p></li>");								
								}
								
								out.println("</ul>");
							}
						}

						out.println("<hr />");

						Object tempVList = request.getAttribute("vList");

						if (tempVList != null && tempVList instanceof List<?>) {
							List<HashMap<String, Object>> vList = (List<HashMap<String, Object>>) tempVList;
							
							if(!vList.isEmpty())
							{
								out.println("<h2>Description</h2>");
								
								int counter = 1;

								for (HashMap<String, Object> vMap : vList) {
									
									out.println("<h3>Method #" + counter +"</h3>");
									
									out.println("<ul>");
									
									if(vMap.get("APPROACH") != null)
									{
										out.println("<li><p><b>Approach Followed:</b> " + (String) vMap.get("APPROACH") + "</p></li>");
									}
									
									if(vMap.get("TECHNIQUE") != null)
									{
										out.println("<li><p><b>Technique Details:</b>"); 
										
										if(vMap.get("SECURITY TOOL") != null)
										{
											out.println("<ul><li><p><b>Security tool used:</b>" + (String) vMap.get("SECURITY TOOL") + "</p></li></ul>");
										}
										
										out.println("<pre style=\"background: #FFFFFF;\"><html><body>" + (String) vMap.get("TECHNIQUE") + "</body></html></pre></p></li>");
									}
									
									out.println("</ul>");
									
									out.println("<hr style=\"width:50%\" />");
								
									counter ++;
								}
							}
						}
			%>
		</c:otherwise>
	</c:choose>
</body>
</html>