

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('MEMBER')">

<form:form action="request/member/edit.do" modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" />
	<form:hidden path="rejectionReason" />
	<form:hidden path="parade" />
	<form:hidden path="member" />
	
	
	<spring:message code="request.parade.title" />: <jstl:out value="${request.parade.title} "></jstl:out>
	<br />
	<br />	
	<form:label path="place">
		<spring:message code="request.parade.place" />:<br />
	</form:label>
	<spring:message code="request.parade.place.row" />: <form:input path="place.rowP" placeholder="${request.place.rowP }"/>
	<spring:message code="request.parade.place.column" />: <form:input path="place.columnP" placeholder="${request.place.columnP }"/>
	<form:errors cssClass="error" path="place" />
	<br />
	<br />
	<br />	
	<input type="submit" name="save"
		value="<spring:message code="request.save" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="request.cancel" />"
		onclick="javascript: relativeRedir('/request/member/list.do');" />
	<br />

	

</form:form>

</security:authorize>
<jstl:choose>
<jstl:when test="${(approve==false)}">
<security:authorize access="hasRole('BROTHERHOOD')">
<form:form action="request/brotherhood/reject.do" modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" />
	<form:hidden path="parade" />
	<form:hidden path="member" />
	<form:hidden path="place" />
	
	<form:label path="rejectionReason">
	<spring:message code="request.parade.rejection" />:
	</form:label>
	<br />
	<form:textarea path="rejectionReason" />
	<form:errors cssClass="error" path="rejectionReason" />
	<br />
	<br />
	
	<spring:message code="request.save" var="saveRequest"  />
	<spring:message code="request.cancel" var="cancelRequest"  />
	
	<input type="submit" name="save"
		value="${saveRequest}" />&nbsp; 


	<input type="button" name="cancel"
		value="${cancelRequest}"
		onclick="javascript: relativeRedir('request/brotherhood/list.do');" />
	<br />
</form:form>
</security:authorize>
</jstl:when>
<jstl:otherwise>
<security:authorize access="hasRole('BROTHERHOOD')">
<form:form action="request/brotherhood/approve.do" modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" />
	<form:hidden path="parade" />
	<form:hidden path="member" />
	<form:hidden path="place" />
	<form:hidden path="rejectionReason" />
	
	<form:label path="place">
		<spring:message code="request.parade.place" />:<br />
	</form:label>
	<spring:message code="request.parade.place.row" />: <form:input path="place.rowP" placeholder="${request.place.rowP }"/>
	<spring:message code="request.parade.place.column" />: <form:input path="place.columnP" placeholder="${request.place.columnP }"/>
	<form:errors cssClass="error" path="place" />
	<br />
	
	<spring:message code="request.save" var="saveRequest"  />
	<spring:message code="request.cancel" var="cancelRequest"  />
	
	<input type="submit" name="save"
		value="${saveRequest}" />&nbsp; 


	<input type="button" name="cancel"
		value="${cancelRequest}"
		onclick="javascript: relativeRedir('request/brotherhood/list.do');" />
	<br />
</form:form>
</security:authorize>
</jstl:otherwise>
</jstl:choose>