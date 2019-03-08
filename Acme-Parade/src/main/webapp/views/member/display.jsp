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

<table class="ui celled table">
	<thead>
		<tr>
			<img src="${member.photo}" class="ui mini rounded image">
			<div class="content">
				<spring:message code="member.profile.title" />
				${member.name}
			</div>

		</tr>
	</thead>
	<tbody>
		<tr>
			<td><spring:message code="member.name" />
			<td data-label="name">${member.name}</td>
		</tr>
		<tr>
			<td><spring:message code="member.middleName" />
			<td data-label="MiddleName">${member.middleName}</td>
		</tr>
		<tr>
			<td><spring:message code="member.surname" />
			<td data-label="surname">${member.surname}</td>
		</tr>
		<tr>
			<td><spring:message code="member.email" />
			<td data-label="email">${member.email}</td>
		</tr>
		<tr>
			<td><spring:message code="member.phone" />
			<td data-label="phone">${member.phone}</td>
		</tr>
		<tr>
			<td><spring:message code="member.address" />
			<td data-label="address">${member.address}</td>
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
		<a href="enrolment/brotherhood/edit.do?enrolmentId=${enrolment.id}"><spring:message code="enrolment.edit"/></a><br/>
		</jstl:otherwise>
	</jstl:choose>
	</security:authorize>

