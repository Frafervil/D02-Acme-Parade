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

	<b><spring:message code="linkRecord.title" /></b>:
	<jstl:out value="${linkRecord.title}"/><br/>
	
	<b><spring:message code="linkRecord.description" /></b>:
	<jstl:out value="${linkRecord.description }"/><br/>	
	
	<b><spring:message code="linkRecord.linkBrotherhood" /></b>:
	<jstl:out value="${linkRecord.linkBrotherhood}"/><br/>

	<br />
	<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="linkRecord/brotherhood/edit.do?linkRecordId=${linkRecord.id}"><spring:message code="linkRecord.edit"/></a>
	<br />
	<a href="linkRecord/brotherhood/delete.do?linkRecordId=${linkRecord.id}"><spring:message code="linkRecord.delete"/></a>
	</security:authorize>