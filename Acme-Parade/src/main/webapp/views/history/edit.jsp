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

<form:form action="history/brotherhood/edit.do" modelAttribute="history">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="brotherhood"/>
		<form:hidden path="periodRecords"/>
		<form:hidden path="legalRecords"/>
		<form:hidden path="linkRecords"/>
		<form:hidden path="miscellaneousRecords"/>

		<acme:textbox code="history.inceptionRecord.title" path="inceptionRecord.title"/>
		
		<acme:textarea code="history.inceptionRecord.description" path="inceptionRecord.description"/>
		
		<acme:textarea code="inceptionRecord.pictures" path="inceptionRecord.pictures"/>
		
		<acme:submit name="save" code="history.save"/>
				
		<acme:cancel url="welcome/index.do" code="history.cancel"/>
		
</form:form>