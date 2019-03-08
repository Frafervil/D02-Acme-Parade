<%--
 * 
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>





<a href="message/actor/create.do"><spring:message code="messageBox.writeMessage" /></a>
<br/>

<security:authorize access="hasRole('ADMIN')">
<a href="message/administrator/create.do"><spring:message code="messageBox.broadCastMessage" /></a>
<br/>
<a href="message/administrator/warning.do"><spring:message code="messageBox.warning" /></a>
<br/>
</security:authorize>

<a href="messageBox/actor/create.do"><spring:message code="messageBox.createMessageBox" /></a>

<jstl:choose>
<jstl:when test="${empty currentMessageBox.name}">

<h3> <spring:message code="messageBox.listMessageBoxes" /></h3>

<display:table pagesize="7" class="displaytag" 
	name="messageBoxes" requestURI="messageBox/actor/list.do" id="messageBox">
	
	<display:column>
	<a href="messageBox/actor/list.do?messageBoxId=${messageBox.id}"><spring:message code="messageBox.show" /></a>
	</display:column>
	
	<spring:message code="messageBox.name" var="name" />
	<display:column property="name" title="${name}" sortable="true" />
	
	<display:column>
	<jstl:if test="${!messageBox.isSystemBox}">
	<a href="messageBox/actor/edit.do?messageBoxId=${messageBox.id}"><spring:message code="messageBox.editMessageBox" /></a>
	</jstl:if>
	</display:column>
	
</display:table>
</jstl:when>
<jstl:otherwise>

<h3> <spring:message code="messageBox.current.messageBox" /> : <jstl:out value="${currentMessageBox.name}" /> </h3>

<%-- La lista de mensajes de la caja de mensaje seleccionada: --%>

<h3> <spring:message code="messageBox.listMessages" /> ${currentMessageBox.name}</h3>

<jstl:if test="${empty messages}">
<spring:message code="messageBox.messages.empty" var="messagesEmpty"/>
<jstl:out value="${messagesEmpty}"></jstl:out>
</jstl:if>

<jstl:if test="${not empty messages}">
<display:table pagesize="5" class="displaytag" 
	name="messages" requestURI="messageBox/actor/list.do" id="message">
	
	<jstl:if test="${currentMessageBox.name != 'out box'}">
	
	<spring:message code="messageBox.message.sender" var="sender" />
	<display:column title="${sender}" sortable="true" >
		
		<%-- Caso normal, se muestra el username del sender --%>
		<jstl:if test="${!message.recipients.contains(message.sender)}">
			<jstl:out value="${message.sender.userAccount.username }"></jstl:out>
		</jstl:if>
		
		<%-- Caso para las notificaciones de cambio de status de application,
		se ha modelado que si el sender y el recipient son el mismo actor, 
		en la columna de sender debe aparecer SYSTEM para especificar que es un mensaje del sistema --%>
		
		<jstl:if test="${message.recipients.contains(message.sender)}">
			<spring:message code="messageBox.message.system" var ="system"/>
			<jstl:out value ="${system }"/>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${(currentMessageBox.name == 'out box')||(!currentMessageBox.isSystemBox)}">
	
		<spring:message code="messageBox.message.recipients" var="recipients" />
		<display:column title="${recipients}" sortable="true">
		
		<%-- Caso normal en el que es un mensaje de una actor a otro actor diferente --%>
		<jstl:if test="${!message.recipients.contains(message.sender) }">
		
		<jstl:forEach items="${message.recipients}" var="recipient">
		<jstl:out value = "${recipient.userAccount.username}"></jstl:out>
				</jstl:forEach>
		
		</jstl:if>
		
		
		<%-- Caso broadcast, seg�n hemos modelado cuando el sender es un administrador significa que es un mensaje broadcast que hay que guardar 
		 en el out box del admin que realiz� dicho broadcast --%>
		  
		<jstl:if test="${message.recipients.contains(message.sender) && (message.sender.userAccount.authorities[0].authority == 'ADMIN') }">
		<spring:message code="messageBox.message.broadcast" var="broadcast" />
		<jstl:out value="${broadcast}" />
		</jstl:if>
		
		</display:column>
	</jstl:if>
	
	<spring:message code="messageBox.message.moment" var="moment" />
	<display:column property="moment" title="${moment}" sortable="true" format = "{0,date,dd/MM/yyyy HH:mm}"/>
	
	<spring:message code="messageBox.message.subject" var="subject" />
	<display:column property="subject" title="${subject}" sortable="false" />
	
	<spring:message code="messageBox.message.body" var="body" />
	<display:column property="body" title="${body}" sortable="false" />
	
	<spring:message code="messageBox.message.priority" var="priority" />
	<display:column property="priority" title="${priority}" sortable="true" />
	
	<display:column>
	<a href="message/actor/edit.do?messageId=${message.id}"><spring:message code="messageBox.message.edit"/></a>
	</display:column>
	
</display:table>
</jstl:if>
<br>
<br>
<jstl:if test="${!currentMessageBox.isSystemBox }" >
	<input type="button" name="editMessageBox"
				value="<spring:message code="messageBox.editMessageBox" />"
				onclick="redirect: location.href = 'messageBox/actor/edit.do?messageBoxId=${currentMessageBox.id}';" />
</jstl:if>
</jstl:otherwise>
</jstl:choose>	