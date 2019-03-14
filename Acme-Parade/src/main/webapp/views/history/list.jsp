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

<display:table name="histories" id="row" pagesize="5" requestURI="${requestURI}" 
class="displaytag" keepStatus="true">

	<!-- Display -->
	<display:column>
		<a href="history/display.do?historyId=${row.id}"><spring:message code="history.display"/></a>
	</display:column>
	
	<!-- Brotherhood title -->
	<spring:message code="brotherhood.title" var="titleHeader" />
	<display:column  property="brotherhood.title" title="${titleHeader}" />
	
</display:table>

<!-- Create history -->
<security:authorize access="hasRole('BROTHERHOOD')">
<jstl:if test = "${row.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<div>
		<a href="history/brotherhood/create.do"><spring:message code="history.create" /></a>
	</div>
	</jstl:if>
</security:authorize> 
