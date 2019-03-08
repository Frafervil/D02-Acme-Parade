

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${permission }">
<form:form action="messageBox/actor/edit.do" modelAttribute="messageBox">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isSystemBox" />
	<form:hidden path="messages" />
	<form:hidden path="actor" />

	
	<acme:textbox code="messageBox.name" path="name"/>
	
	<acme:select code="messageBox.parentBox" path="parentBox"
		items="${parentBoxes}" itemLabel="name" id="parentBox"/>
		
	
	<acme:submit name="save" code="messageBox.save"/>
	
	<jstl:if test="${messageBox.id!=0}">
		<acme:submit name="delete" code="messageBox.delete"/>
	</jstl:if>
	
	<acme:cancel url="messageBox/actor/list.do" code="messageBox.cancel"/>

</form:form>

</jstl:if>
<jstl:if test="${!permission }">
<h3><spring:message code="messageBox.nopermission" /></h3>

</jstl:if>