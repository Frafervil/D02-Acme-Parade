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

<form:form action="parade/brotherhood/edit.do" modelAttribute="parade">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		
		<acme:textbox code="parade.title" path="title"/>
		
		<acme:textarea code="parade.description" path="description"/>
		
		<acme:textbox code="parade.moment" path="moment" placeholder="dd/MM/yyyy HH:mm" />
		
		<acme:textbox code="parade.maxRow" path="maxRow"/>
		
		<acme:textbox code="parade.maxColumn" path="maxColumn"/>	
		
		<jstl:if test="${parade.isDraft == true}">
		
		<acme:submit name="saveDraft" code="parade.saveDraft"/>
		
		</jstl:if>
		
		<acme:submit name="saveFinal" code="parade.saveFinal"/>
		
		<jstl:if test="${parade.id != 0 }">
			<acme:submit name="delete" code="parade.delete"/>
		</jstl:if>
		
		<acme:cancel url="welcome/index" code="parade.cancel"/>
		
</form:form>
