<%--
 * 
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="members" id="row" pagesize="5" requestURI="${requestURI}" 
class="displaytag" keepStatus="true">

	<!-- Display -->
	<security:authorize access="hasRole('BROTHERHOOD')">
	<display:column>
		<a href="member/brotherhood/display.do?memberId=${row.id}"><spring:message code="member.display"/></a>
	</display:column>
	</security:authorize>
	<!-- Name -->
	<spring:message code="member.name" var="nameHeader" />
	<display:column  property="name" title="${nameHeader}" />
	
	<!-- Surname -->
	<spring:message code="member.surname" var="surnameHeader" />
	<display:column  property="surname" title="${surnameHeader}" />
	
	<!-- UserAccount -->
	<spring:message code="member.username" var="userAccountHeader" />
	<display:column  property="userAccount.username" title="${userAccountHeader}" />
	
</display:table>