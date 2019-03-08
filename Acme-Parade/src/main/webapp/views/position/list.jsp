<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!-- Listing grid -->


<display:table name="positions" id="row" requestURI="position/administrator/list.do" pagesize="5" class="displaytag">


	<!-- Action links -->

	<display:column>
		<a href="position/administrator/edit.do?positionId=${row.id}"> <spring:message
						code="position.edit" /></a>
	</display:column>
	
	<display:column>
		<a href="position/administrator/delete.do?positionId=${row.id}"> <spring:message
						code="position.delete" /></a>
	</display:column>


	<!-- Attributes -->

	<spring:message code="position.name" var="nameHeader" />
	<jstl:if test="${cookie['language'].getValue()=='en'}">
	
	<display:column property="englishName" title="${nameHeader}" sortable="true" />
	</jstl:if>
	<jstl:if test="${cookie['language'].getValue()=='es'}">
	
	<display:column property="spanishName" title="${nameHeader}" sortable="true" />
	</jstl:if>


</display:table>


<div>
	<a href="position/administrator/create.do"> <spring:message
			code="position.create" />
	</a>
</div>




