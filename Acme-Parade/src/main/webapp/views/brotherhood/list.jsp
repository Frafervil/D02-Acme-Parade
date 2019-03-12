<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!-- Listing grid -->


<display:table name="brotherhoods" id="row" requestURI="brotherhood/list.do"
	pagesize="5" class="displaytag">

	<!-- Attributes -->

	<spring:message code="brotherhood.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="true" />
	<spring:message code="brotherhood.establishmentDate" var="establishmentDateHeader" />
	<display:column property="establishmentDate" title="${establishmentDateHeader}"
		sortable="true" />
	<!-- Action links -->

	<display:column>
	<a href="member/brotherhood/list.do?brotherhoodId=${row.id }"> <spring:message code="brotherhood.members" /></a>
	</display:column>
	
	<display:column>
	<a href="float/brotherhood/list.do?brotherhoodId=${row.id }"> <spring:message code="brotherhood.floats" /></a>
	</display:column>
	
	<display:column>
	<a href="parade/brotherhood/list.do?brotherhoodId=${row.id }"> <spring:message code="brotherhood.parades" /></a>
	</display:column>
	
	

</display:table>