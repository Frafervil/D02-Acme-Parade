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

	<b><spring:message code="periodRecord.title" /></b>:
	<jstl:out value="${periodRecord.title}"/><br/>
	
	<b><spring:message code="periodRecord.description" /></b>:
	<jstl:out value="${periodRecord.description }"/><br/>	
	
	<jstl:if test="${periodRecord.pictures != null && (not empty periodRecord.pictures)}">
	<b><spring:message code="periodRecord.pictures" /></b>:
	<br/>
		<div class="row">
		<jstl:forEach items="${periodRecord.pictures}" var="picture" >
			<jstl:if test="${picture != null}">
				<div class="column">
					<img src="${picture }" style="width:100%">
				</div>
	        </jstl:if>
		</jstl:forEach>
		</div>
		<br/>
	</jstl:if>
	
	<b><spring:message code="periodRecord.startYear" /></b>:
	<jstl:out value="${periodRecord.startYear}"/><br/>	
	
	<b><spring:message code="periodRecord.endYear" /></b>:
	<jstl:out value="${periodRecord.endYear}"/><br/>
	<br />
	<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="periodRecord/brotherhood/edit.do?periodRecordId=${periodRecord.id}"><spring:message code="periodRecord.edit"/></a>
	<br />
	<a href="periodRecord/brotherhood/delete.do?periodRecordId=${periodRecord.id}"><spring:message code="periodRecord.delete"/></a>
	</security:authorize>