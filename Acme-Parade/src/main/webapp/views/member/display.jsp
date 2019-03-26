<%--
 * display.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="content">
	<img src="${member.photo}" class="ui mini rounded image">
</div>

<table class="ui celled table">
	<tbody>
		<tr>
			<td><spring:message code="member.name" />
			<td data-label="name"><jstl:out value="${member.name}" /></td>
		</tr>
		<tr>
			<td><spring:message code="member.middleName" />
			<td data-label="MiddleName"><jstl:out value="${member.middleName}" /></td>
		</tr>
		<tr>
			<td><spring:message code="member.surname" />
			<td data-label="surname"><jstl:out value="${member.surname}" /></td>
		</tr>
		<tr>
			<td><spring:message code="member.email" />
			<td data-label="email"><jstl:out value="${member.email}" /></td>
		</tr>
		<tr>
			<td><spring:message code="member.phone" />
			<td data-label="phone"><jstl:out value="${member.phone}" /></td>
		</tr>
		<tr>
			<td><spring:message code="member.address" />
			<td data-label="address"><jstl:out value="${member.address}" /></td>
		</tr>
	</tbody>
</table>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="member/edit.do" code="member.edit"/>
	<br>
	<br>
	</security:authorize>

	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:choose>
		<jstl:when test="${enrolment == null}">
		<a href="enrolment/brotherhood/create.do?memberId=${member.id}"><spring:message code="member.enrol"/></a><br/>
		</jstl:when>
		<jstl:otherwise>
		<a href="enrolment/brotherhood/edit.do?memberId=${member.id}"><spring:message code="enrolment.edit"/></a><br/>
		</jstl:otherwise>
	</jstl:choose>
	</security:authorize>

<jstl:if test="${member.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>

<br/>
<br/>
<jstl:if test="${member.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="member/delete.do" code="actor.delete"/>
</jstl:if>