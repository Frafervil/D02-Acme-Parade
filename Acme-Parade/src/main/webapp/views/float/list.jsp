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

<display:table name="floats" id="row" pagesize="5" requestURI="${requestURI}" 
class="displaytag" keepStatus="true">

	<!-- Display -->
	<security:authorize access = "hasRole('BROTHERHOOD')">
	<jstl:if test = "${row.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<display:column>
		<a href="float/display.do?floatId=${row.id}"><spring:message code="float.display"/></a>
	</display:column>
	</jstl:if>
	</security:authorize>
	
	<!-- Title -->
	<spring:message code="float.title" var="titleHeader" />
	<display:column  property="title" title="${titleHeader}" />
	
	<!-- Description -->
	<spring:message code="float.description" var="descriptionHeader" />
	<display:column  property="description" title="${descriptionHeader}" />
	
</display:table>

<!-- Create float -->
<security:authorize access="hasRole('BROTHERHOOD')">
<jstl:if test = "${row.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<div>
		<a href="float/brotherhood/create.do"><spring:message
				code="float.create" /></a>
	</div>
	</jstl:if>
</security:authorize> 
