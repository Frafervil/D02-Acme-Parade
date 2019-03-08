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

		<b><spring:message code="procession.title" /></b>:
		<jstl:out value="${procession.title}"/><br/>
	
		<b><spring:message code="procession.description" /></b>:
		<jstl:out value="${procession.description }"/><br/>	
	
		<b><spring:message code="procession.moment" /></b>:
		<jstl:out value="${procession.moment }"/><br/>	
	
		<b><spring:message code="procession.ticker" /></b>:
		<jstl:out value="${procession.ticker }"/><br/>
		
		<jstl:if test="${procession.isDraft == true}">
		
		<b><spring:message code="procession.isDraft" /></b>:
		<spring:message code="procession.isDraft.true" /><br/>	
		
		</jstl:if>
		
		<jstl:if test="${procession.isDraft == false}">
		
		<b><spring:message code="procession.isDraft" /></b>:
		<spring:message code="procession.isDraft.false" /><br/>		
		
		</jstl:if>
		
		<b><spring:message code="procession.maxRow" /></b>:
		<jstl:out value="${procession.maxRow }"/><br/>
		
		<b><spring:message code="procession.maxColumn" /></b>:
		<jstl:out value="${procession.maxColumn }"/><br/>
		
		<!-- Floats -->
<h3> <spring:message code="procession.floats" /> </h3>
<jstl:choose>
	<jstl:when test="${not empty floats}">
		<display:table pagesize="5" class="displaytag" name="floats" requestURI="procession/display.do" id="floats">

			<spring:message code="procession.float.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="procession.float.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
		</display:table>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="procession.floats.empty" /> 
	</jstl:otherwise>
</jstl:choose>
	
		<!-- Links de editar, listar y borrar -->
		<a href="procession/brotherhood/edit.do?processionId=${procession.id}"><spring:message code="procession.edit"/></a><br/>
	
	
		