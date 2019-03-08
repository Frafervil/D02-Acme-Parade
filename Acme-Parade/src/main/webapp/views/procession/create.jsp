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

<form:form action="procession/brotherhood/create.do" modelAttribute="procession">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		
		<acme:textbox code="procession.title" path="title" placeholder="El critsto"/>
		
		<acme:textarea code="procession.description" path="description"/>
		
		<acme:textbox code="procession.moment" path="moment" placeholder="dd/MM/yyyy HH:mm" />
		
		<acme:textbox code="procession.maxRow" path="maxRow"/>
		
		<acme:textbox code="procession.maxColumn" path="maxColumn"/>	
		
		
		<acme:submit name="save" code="procession.saveFinal"/>
		
		
		<acme:cancel url="welcome/index.do" code="procession.cancel"/>
		
</form:form>
