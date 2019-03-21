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

<form:form action="legalRecord/brotherhood/edit.do" modelAttribute="legalRecord">
		<form:hidden path="id"/>
		<form:hidden path="version"/>

		<acme:textbox code="legalRecord.title" path="title"/>
		
		<acme:textarea code="legalRecord.description" path="description"/>
		
		<acme:textbox code="legalRecord.legalName" path="legalName"/>
		
		<acme:textbox code="legalRecord.vatNumber" path="vatNumber"/>
		
		<acme:textarea code="legalRecord.applicableLaws" path="applicableLaws"/>
		
		<acme:submit name="save" code="legalRecord.save"/>
				
		<acme:cancel url="history/brotherhood/display.do" code="legalRecord.cancel"/>
		
</form:form>