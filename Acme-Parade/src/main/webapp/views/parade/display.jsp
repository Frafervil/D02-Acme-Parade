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

		<b><spring:message code="parade.title" /></b>:
		<jstl:out value="${parade.title}"/><br/>
	
		<b><spring:message code="parade.description" /></b>:
		<jstl:out value="${parade.description }"/><br/>	
	
		<b><spring:message code="parade.moment" /></b>:
		<jstl:out value="${parade.moment }"/><br/>	
	
		<b><spring:message code="parade.ticker" /></b>:
		<jstl:out value="${parade.ticker }"/><br/>
		
		<jstl:if test="${parade.isDraft == true}">
		
		<b><spring:message code="parade.isDraft" /></b>:
		<spring:message code="parade.isDraft.true" /><br/>	
		
		</jstl:if>
		
		<jstl:if test="${parade.isDraft == false}">
		
		<b><spring:message code="parade.isDraft" /></b>:
		<spring:message code="parade.isDraft.false" /><br/>		
		
		</jstl:if>
		
		<b><spring:message code="parade.maxRow" /></b>:
		<jstl:out value="${parade.maxRow }"/><br/>
		
		<b><spring:message code="parade.maxColumn" /></b>:
		<jstl:out value="${parade.maxColumn }"/><br/>
		
		<!-- Floats -->
<h3> <spring:message code="parade.floats" /> </h3>
<jstl:choose>
	<jstl:when test="${not empty floats}">
		<display:table pagesize="5" class="displaytag" name="floats" requestURI="parade/display.do" id="floats">

			<spring:message code="parade.float.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="parade.float.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
		</display:table>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="parade.floats.empty" /> 
	</jstl:otherwise>
</jstl:choose>
	
		<!-- Links de editar, listar y borrar -->
		<a href="parade/brotherhood/edit.do?paradeId=${parade.id}"><spring:message code="parade.edit"/></a><br/>
	
	
		