<%--
 * edit.jsp
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

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="float/brotherhood/edit.do" modelAttribute="float">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<acme:textbox code="float.title" path="title"/>
		
		<acme:textarea code="float.description" path="description"/>
		
		<spring:message code = "float.pictures.placeholder" var="picturePlaceholder"/>
		<acme:textarea code="float.pictures" path="pictures"/>
		
		<acme:select code="float.procession" path="procession"
		items="${processions}" itemLabel="title" id="procession"/>
		
		<acme:submit name="create" code="float.save" id="create"/>
		
		<acme:submit name="delete" code="float.delete" />
		
		<acme:cancel url="float/brotherhood/list.do" code="float.cancel"/>
		
</form:form>