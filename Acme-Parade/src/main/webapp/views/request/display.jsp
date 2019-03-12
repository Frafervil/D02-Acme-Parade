<%--
 * 
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
 
 <%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


	<b><spring:message code="request.parade.title" /></b>:
	<jstl:out value="${request.parade.title}"/><br/>
	<br>	
	<b><spring:message code="request.parade.member" /></b>:
	<jstl:out value="${request.member.name}"/><br/>
	<br>
	<b><spring:message code="request.parade.status" /></b>:
	<jstl:out value="${request.status}"/><br/>
	
	<jstl:if test="${request.status == 'REJECTED'}">
		<b><spring:message code="request.parade.rejection" /></b>:
		<jstl:out value="${request.rejectionReason}"/><br/>
	</jstl:if>
	
	<jstl:if test="${request.status == 'APPROVED'}">
		<b><spring:message code="request.parade.place" /></b>:
		<br/>
		<jstl:out value="Row: ${request.place.rowP}"/><br/>
		<jstl:out value="Column: ${request.place.columnP}"/><br/>
		
	</jstl:if>
	
	<security:authorize access="hasRole('MEMBER')">
	
	<jstl:if test="${request.status == 'PENDING'}">
		<a href="request/member/delete.do?requestId=${request.id}" ><spring:message code="request.delete" /></a><br/>			

	</jstl:if>
	</security:authorize>
	<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:if test="${request.status == 'PENDING'}">
	<br/>
		<a href="request/brotherhood/reject.do?requestId=${request.id}" ><spring:message code="request.reject" /></a><br/>			
		<a href="request/brotherhood/approve.do?requestId=${request.id}" ><spring:message code="request.approve" /></a>								
		
	</jstl:if>
	</security:authorize>	
	