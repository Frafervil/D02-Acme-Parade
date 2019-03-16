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

	<b><spring:message code="history.brotherhood.title" /></b>:
	<jstl:out value="${history.brotherhood.title}"/><br/>

<!-- Inception record -->

	<b><spring:message code="history.inceptionRecord.title" /></b>:
	<jstl:out value="${history.inceptionRecord.title}"/><br/>
	
	<b><spring:message code="history.inceptionRecord.description" /></b>:
	<jstl:out value="${history.inceptionRecord.description}"/><br/>
	
	<jstl:if test="${history.inceptionRecord.pictures != null && (not empty history.inceptionRecord.pictures)}">
	<b><spring:message code="inceptionRecord.pictures" /></b>:
	<br/>
		<jstl:forEach items="${history.inceptionRecord.pictures}" var="picture" >
			<jstl:if test="${picture != null}">
	        	<acme:image src="${picture}" cssClass="external-image-landscape"/>
	        </jstl:if>
		</jstl:forEach>
		<br/>
	</jstl:if>
	
	<jstl:if test="${history.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
		<acme:button url="inceptionRecord/brotherhood/edit.do?inceptionRecordId=${history.inceptionRecord.id}" code="inceptionRecord.edit"/>
	</jstl:if>
	
	<!-- Period records -->
	
	<h3> <spring:message code="history.periodRecords" /> </h3>
	<jstl:choose>
	<jstl:when test="${not empty periodRecords}">
	<display:table pagesize="5" class="displaytag" name="periodRecords" requestURI="history/display.do" id="periodRecord">
			
			<display:column>
			<a href="periodRecord/display.do?periodRecordId=${periodRecord.id}"><spring:message code="periodRecord.display"/></a>
			</display:column>
			
			<spring:message code="periodRecord.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="periodRecord.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
	</display:table>
	</jstl:when>

	<jstl:otherwise>
	<spring:message code="history.periodRecord.empty" /> 
	</jstl:otherwise>
	</jstl:choose>
	<jstl:if test="${history.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
		<acme:button url="periodRecord/brotherhood/create.do" code="periodRecord.create"/>
	</jstl:if>
	
	<!-- Legal records -->
	
	<h3> <spring:message code="history.legalRecords" /> </h3>
	<jstl:choose>
	<jstl:when test="${not empty legalRecords}">
	<display:table pagesize="5" class="displaytag" name="legalRecords" requestURI="history/display.do" id="legalRecord">
			
			<display:column>
			<a href="legalRecord/display.do?legalRecordId=${legalRecord.id}"><spring:message code="legalRecord.display"/></a>
			</display:column>
			
			<spring:message code="legalRecord.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="legalRecord.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
	</display:table>
	</jstl:when>

	<jstl:otherwise>
	<spring:message code="history.legalRecord.empty" /> 
	</jstl:otherwise>
	</jstl:choose>
	<jstl:if test="${history.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
		<acme:button url="legalRecord/brotherhood/create.do" code="legalRecord.create"/>
	</jstl:if>
	
	<!-- Link records -->
	
	<h3> <spring:message code="history.linkRecords" /> </h3>
	<jstl:choose>
	<jstl:when test="${not empty linkRecords}">
	<display:table pagesize="5" class="displaytag" name="linkRecords" requestURI="history/display.do" id="linkRecord">
			
			<display:column>
			<a href="linkRecord/display.do?linkRecordId=${linkRecord.id}"><spring:message code="linkRecord.display"/></a>
			</display:column>
			
			<spring:message code="linkRecord.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="linkRecord.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
	</display:table>
	</jstl:when>

	<jstl:otherwise>
	<spring:message code="history.linkRecord.empty" /> 
	</jstl:otherwise>
	</jstl:choose>
	<jstl:if test="${history.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
		<acme:button url="linkRecord/brotherhood/create.do" code="linkRecord.create"/>
	</jstl:if>
	
	<!-- Miscellaneous records -->
	
	<h3> <spring:message code="history.miscellaneousRecords" /> </h3>
	<jstl:choose>
	<jstl:when test="${not empty miscellaneousRecords}">
	<display:table pagesize="5" class="displaytag" name="miscellaneousRecords" requestURI="history/display.do" id="miscellaneousRecord">
			
			<display:column>
			<a href="miscellaneousRecord/display.do?miscellaneousRecordId=${miscellaneousRecord.id}"><spring:message code="miscellaneousRecord.display"/></a>
			</display:column>
			
			<spring:message code="miscellaneousRecord.title" var="title" />
			<display:column property="title" title="${title}" sortable="true"/>
	
			<spring:message code="miscellaneousRecord.description" var="description" />
			<display:column property="description" title="${description}" sortable="true"/>
			
	</display:table>
	</jstl:when>

	<jstl:otherwise>
	<spring:message code="history.miscellaneousRecord.empty" /> 
	</jstl:otherwise>
	</jstl:choose>
	<jstl:if test="${history.brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
		<acme:button url="miscellaneousRecord/brotherhood/create.do" code="miscellaneousRecord.create"/>
	</jstl:if>