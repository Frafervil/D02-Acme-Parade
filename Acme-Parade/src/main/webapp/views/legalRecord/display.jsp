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

	<b><spring:message code="legalRecord.title" /></b>:
	<jstl:out value="${legalRecord.title}"/><br/>
	
	<b><spring:message code="legalRecord.description" /></b>:
	<jstl:out value="${legalRecord.description }"/><br/>	
	
	<b><spring:message code="legalRecord.legalName" /></b>:
	<jstl:out value="${legalRecord.legalName}"/><br/>
	
	<b><spring:message code="legalRecord.vatNumber" /></b>:
	<jstl:out value="${legalRecord.vatNumber}"/><br/>
	
	<tr>
	<td> <strong> <spring:message code="legalRecord.applicableLaws" /> : </strong> </td>
	<td>

	<ul>
	<jstl:forEach items="${legalRecord.applicableLaws}" var="applicableLaw">

	<li> <jstl:out value="${applicableLaw}"> </jstl:out> </li>

	</jstl:forEach>
	</ul> 
 	</td>
	</tr>