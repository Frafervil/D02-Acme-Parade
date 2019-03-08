<%--
 * edit.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%-- Formulario para la creación de mensajes nuevos, (escritura de un mensaje a un actor) --%>
<jstl:if test="${mensaje.id==0}">
<form:form action="${requestURI}" modelAttribute="mensaje">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isSpam" />
	<form:hidden path="sender" />
	<form:hidden path="moment" />
	<form:hidden path="messageBoxes" />
	
	
	<jstl:if test="${broadcast}">
	<form:hidden path="recipients" />
	
	</jstl:if>
	
	<%-- Mostrado para elegir en el input el destinatario del mensaje, cuando es un mensaje broadcast de un administrador este input no se muestra puesto que el destinatario son todos los actores del sistema--%>
	<jstl:if test="${!broadcast}">
	<form:label path="recipients">
		<spring:message code="message.recipient.userAccount" />:
	</form:label>
	<form:select multiple="true" path="recipients" >
		<form:options items="${recipients}" itemValue="id" itemLabel="userAccount.username" />
	</form:select>
	<form:errors cssClass="error" path="recipients" />
	<br>
	<br>
	</jstl:if>
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br>
	<br>
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br>
	<br>
	
	<form:label path="priority">
		<spring:message code="message.priority" />:
	</form:label>
	<form:radiobutton path="priority" value="HIGH"/><spring:message code="message.priority.high" />
	<form:radiobutton path="priority" value="NEUTRAL" checked="checked" /><spring:message code="message.priority.neutral" />
	<form:radiobutton path="priority" value="LOW"/><spring:message code="message.priority.low" />
	<br>
	<br>
	
	<form:label path="tags">
		<spring:message code="message.tags" />:
	</form:label>
	<form:textarea path="tags" />
	<form:errors cssClass="error" path="tags" />
	<br>
	<br>
	
	<jstl:if test="${mensaje.id==0}">
	<input type="submit" name="save"
		value="<spring:message code="message.send" />" />&nbsp; 
	</jstl:if>
	
	<input type="button" name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('/messageBox/actor/list.do');" />
	<br />

</form:form>
</jstl:if>


<jstl:if test="${permission }">

<%-- Formulario para la edición de mensajes que incluye: borrar un mensaje y moverlo a una caja de mensaje diferente --%>
<jstl:if test="${mensaje.id!=0}">
<form:form action="message/actor/edit.do" modelAttribute="mensaje">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isSpam" />
	<form:hidden path="sender" />
	<form:hidden path="moment" />
	<form:hidden path="recipients" />
	<form:hidden path="priority" />
	<form:hidden path="subject" />
	<form:hidden path="body" />
	<form:hidden path="tags" />
	
	<%-- El fieldset tiene todos los input disabled, es simplemente para que el usuario vea en la vista el mensaje que está editando --%>
	<fieldset>
	<legend><spring:message code="message.legend" /></legend>
	
	<form:label path="sender">
		<spring:message code="message.sender.userAccount" />:
	</form:label>
	<input type="text"  value="${mensaje.sender.userAccount.username}"  disabled />
	<br>
	<br>
	
	
	
	<form:label path="recipients">
		<spring:message code="message.recipient.userAccount" />:
	</form:label>
	<input type="text" value="${mensaje.recipients}" disabled />
	<br>
	<br>
	
	
	<form:label path="moment">
	<spring:message code="message.moment" />:
	</form:label>
	<input type="text" value="${mensaje.moment}"  disabled />
	<br>
	<br>
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<input type="text" value="${mensaje.subject }" disabled />
	<br>
	<br>
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<input type="text"  value="${mensaje.body}" disabled />
	<br>
	<br>
	
	<form:label path="tags">
		<spring:message code="message.tags" />:
	</form:label>
	<input type="text"  value="${mensaje.tags}" disabled />
	<br>
	<br>

	</fieldset>
	<br>
	<br>
	
	<form:label path="messageBoxes">
		<spring:message code="message.messageBox" />:
	</form:label>
	<form:select path="messageBoxes" >
	<form:options items="${messageBoxes}" itemLabel="name" itemValue="id" />
	</form:select>
	<form:errors cssClass="error" path="messageBoxes" />
	<br>
	<br>
	
	<input type="submit" name="move"
		value="<spring:message code="message.move" />" />&nbsp; 
	
	<input type="submit" name="delete"
			value="<spring:message code="message.delete" />"
			onclick="return confirm('<spring:message code="message.confirm.delete" />')" />&nbsp;
		
	<input type="button" name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('/messageBox/actor/list.do');" />
	<br />
	
</form:form>
</jstl:if>

</jstl:if>


<jstl:if test="${!permission }">
<h3><spring:message code="message.nopermission" /></h3>
</jstl:if>