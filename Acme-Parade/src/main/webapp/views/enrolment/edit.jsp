<%--
 * edit.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="enrolment/brotherhood/edit.do?memberId=${param['memberId']}" modelAttribute="enrolment">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="enrolmentMoment" />
	<form:hidden path="dropOutMoment" />
	<form:hidden path="brotherhood" />
	<form:hidden path="member" />

	<jstl:if test="${cookie['language'].getValue()=='en'}">
		<div>
			<form:label path="position">
				<spring:message code="enrolment.position" />
			</form:label>
			<form:select id="position" path="position">
				<form:options items="${positions}" itemLabel="englishName" />
			</form:select>
			<form:errors path="position" cssClass="error" />
		</div>
	</jstl:if>
	<jstl:if test="${cookie['language'].getValue()=='es'}">
		<div>
			<form:label path="position">
				<spring:message code="enrolment.position" />
			</form:label>
			<form:select id="position" path="position">
				<form:options items="${positions}" itemLabel="spanishName" />
			</form:select>
			<form:errors path="position" cssClass="error" />
		</div>
	</jstl:if>

	<acme:submit name="save" code="enrolment.save" />

	<jstl:if test="${enrolment.id != 0 }">
		<acme:submit name="delete" code="enrolment.delete" />
	</jstl:if>

	<acme:cancel url="member/list.do" code="enrolment.cancel" />

</form:form>