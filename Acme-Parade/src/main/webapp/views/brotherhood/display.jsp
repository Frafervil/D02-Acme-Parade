<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3> <jstl:out value="${brotherhood.title}"> </jstl:out> </h3>
<br />

<table class="ui celled table">
	<thead>
		<tr>
			<img src="${brotherhood.photo}" class="ui mini rounded image" >
			<div class="content">
				<spring:message code="brotherhood.profile.title" />
				<jstl:out value="${brotherhood.name}" />
			</div>

		</tr>
	</thead>
	<tbody>
		<tr>
			<td><spring:message code="brotherhood.name" />
			<td data-label="name"><jstl:out value="${brotherhood.name}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.middleName" />
			<td data-label="MiddleName"><jstl:out value="${brotherhood.middleName}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.surname" />
			<td data-label="surname"><jstl:out value="${brotherhood.surname}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.email" />
			<td data-label="email"><jstl:out value="${brotherhood.email}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.phone" />
			<td data-label="phone"><jstl:out value="${brotherhood.phone}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.address" />
			<td data-label="address"><jstl:out value="${brotherhood.address}" /></td>
		</tr>
		<tr>
			<td><spring:message code="brotherhood.area" />
			<td data-label="area"><jstl:out value="${brotherhood.area.name}" /></td>
		</tr>
	</tbody>
</table>


<br />
<strong> <spring:message code="brotherhood.establishmentDate" /> : </strong> 
<jstl:out value="${brotherhood.establishmentDate}"> </jstl:out>
<br />

<strong> <spring:message code="brotherhood.pictures" /> : </strong>

<ul>
<jstl:forEach items="${brotherhood.pictures}" var="pictures"><img src='<jstl:out value="${pictures}"></jstl:out>'>
<br />
</jstl:forEach>
</ul>



<!-- Members -->
<h3> <spring:message code="brotherhood.members" /> </h3>
<jstl:choose>
<jstl:when test="${not empty members}">
<display:table pagesize="5" class="displaytag" name="members" requestURI="brotherhood/display.do" id="members">
		<spring:message code="brotherhood.members.name" var="name" />
		<display:column property="name" title="${name}" sortable="true"/>
	
		<spring:message code="brotherhood.members.surname" var="surname" />
		<display:column property="surname" title="${surname}" sortable="true"/>
			
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="brotherhood.members.empty" /> 
</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('MEMBER')">
	<jstl:if test="${enrolment != null}">
		<a href="brotherhood/member/dropOut.do?brotherhoodId=${brotherhood.id}"><spring:message code="brotherhood.dropOut"/></a><br/>
	</jstl:if>
</security:authorize>

<!-- Parades -->
<h3> <spring:message code="brotherhood.parades" /> </h3>
<jstl:choose>
<jstl:when test="${not empty parades}">
<display:table pagesize="5" class="displaytag" name="parades" requestURI="brotherhood/display.do" id="parades">
		<spring:message code="brotherhood.parades.title" var="title" />
		<display:column property="title" title="${title}" sortable="true"/>
	
		<spring:message code="brotherhood.parades.moment" var="moment" />
		<display:column property="moment" title="${moment}" sortable="true"/>
			
</display:table>
</jstl:when>

<jstl:otherwise>
<spring:message code="brotherhood.parades.empty" /> 
</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('BROTHERHOOD')">
<acme:button url="parade/brotherhood/create.do" code="parade.create"/>
</security:authorize>

<!-- Floats -->
<h3> <spring:message code="brotherhood.floats" /> </h3>
<jstl:choose>
<jstl:when test="${not empty floats}">
<display:table pagesize="5" class="displaytag" name="floats" requestURI="brotherhood/display.do" id="floats">
		<spring:message code="brotherhood.floats.title" var="title" />
		<display:column property="title" title="${title}" sortable="true"/>
	
		<spring:message code="brotherhood.floats.description" var="description" />
		<display:column property="description" title="${description}" sortable="true"/>
			
</display:table>

</jstl:when>

<jstl:otherwise>
<spring:message code="brotherhood.floats.empty" /> 
</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('BROTHERHOOD')">
<acme:button url="float/brotherhood/create.do" code="float.create"/>
</security:authorize>

<jstl:if test="${brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<security:authorize access="hasRole('BROTHERHOOD')">
<br/>
<br/>
<input type="button" name="save" class="ui button"
	value="<spring:message code="brotherhood.edit" />"
	onclick="javascript: relativeRedir('brotherhood/edit.do');" />
	
</security:authorize>
</jstl:if>
<br/>
<br/>
<jstl:if test="${brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>
	
<br/>
<br/>
<jstl:if test="${brotherhood.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="brotherhood/delete.do" code="actor.delete"/>
</jstl:if>