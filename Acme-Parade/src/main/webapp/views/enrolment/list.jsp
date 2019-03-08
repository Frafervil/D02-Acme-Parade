<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="enrolments" id="row" pagesize="5" requestURI="${requestURI}" 
class="displaytag">
	
	<!-- Attributes -->
	
	<display:column>
	<a href="brotherhood/display.do?brotherhoodId=${row.brotherhood.id }"> <jstl:out value="${row.brotherhood.title}"/></a>
	</display:column>
	
	<spring:message code="enrolment.enrolmentMoment" var="enrolmentMomentHeader" />
	<display:column property="enrolmentMoment" title="${enrolmentMomentHeader }"/>
		
	<spring:message code="enrolment.dropOutMoment" var="dropOutMomentHeader" />
	<display:column property="dropOutMoment" title="${dropOutMomentHeader }"/>
		
</display:table>