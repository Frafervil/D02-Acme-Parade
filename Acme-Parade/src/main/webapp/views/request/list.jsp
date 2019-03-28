<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
	
	<security:authorize access="hasRole('MEMBER')">
	
	<form action="request/member/list.do" method="get">
	
	<input type="radio" name="requestStatus" value="0" checked> <spring:message code="request.status.all" />
	<input type="radio" name="requestStatus" value="1"> <spring:message code="request.status.approved" />
	<input type="radio" name="requestStatus" value="2">  <spring:message code="request.status.pending" />
	<input type="radio" name="requestStatus" value="3">  <spring:message code="request.status.rejected" />
	<br />
	<spring:message code="request.status.choose" var="choose"/>
	<input type="submit" value="${choose}">
	</form>

	</security:authorize>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	
	<form action="request/brotherhood/list.do" method="get">
	
	<input type="radio" name="requestStatus" value="0" checked> <spring:message code="request.status.all" />
	<input type="radio" name="requestStatus" value="1"> <spring:message code="request.status.approved" />
	<input type="radio" name="requestStatus" value="2">  <spring:message code="request.status.pending" />
	<input type="radio" name="requestStatus" value="3">  <spring:message code="request.status.rejected" />
	<br />
	<spring:message code="request.status.choose" var="choose"/>
	<input type="submit" value="${choose}">
	</form>
	
	
	
	</security:authorize>


<!-- Listing grid -->

<display:table name="requests" id="row"
	requestURI="${requestURI}" pagesize="5"
	class="displaytag">
	

	<!-- Action links -->



		<spring:message code="request.display" var="displayHeader" />
		<display:column title="${displayHeader}">
			<security:authorize access="hasRole('MEMBER')">
		
				<a href="request/member/display.do?requestId=${row.id}" >
						<spring:message code="request.display" />
				</a>					
			</security:authorize>
			<security:authorize access="hasRole('BROTHERHOOD')">
		
				<a href="request/brotherhood/display.do?requestId=${row.id}" >
						<spring:message code="request.display" />
				</a>					
			</security:authorize>	
		</display:column>
		
		
				






	<!-- Attributes -->

	<spring:message code="request.parade.title"
	var="titleHeader" />
	<display:column property="parade.title" title="${titleHeader}"
		sortable="true"/>



	

	
	<jstl:choose>
	<jstl:when test="${row.status == 'APPROVED'}">
	<jstl:set var="background" value="greenCell" /> 
	</jstl:when>
	
	
	<jstl:when test = "${row.status == 'REJECTED'}">
	<jstl:set var="background" value="orangeCell" /> 
	</jstl:when>
	
	<jstl:when test = "${row.status == 'PENDING'}">
	<jstl:set var="background" value="greyCell" /> 
	</jstl:when>
	
	<jstl:otherwise>
	<jstl:set var="background" value="whiteCell" /> 
	</jstl:otherwise>
	
	</jstl:choose>
	


	<spring:message code="request.status" var="statusHeader" />
	
	<display:column class="${background}" title="${statusHeader}" sortable="true"> 
	<jstl:choose>
	<jstl:when test="${empty row.rejectionReason}">
	<span> <jstl:out value="${row.status}" /> </span>
	</jstl:when>
	<jstl:otherwise>
	<span> <jstl:out value="REJECTED"/> </span>
	<div> <jstl:out value="${row.rejectionReason}"/> </div>
	</jstl:otherwise>
	</jstl:choose>
	
	</display:column>

	

	<security:authorize access="hasRole('BROTHERHOOD')">
	<spring:message code="request.member"
		var="memberHeader" />
		
	<display:column title="${memberHeader}" sortable="false" >
	<div><a href="member/display.do?memberId=${row.member.id}"> <jstl:out value="${row.member.userAccount.username}"> </jstl:out>  </a>  </div>
	</display:column>
	</security:authorize>
	
	


</display:table>

<br/>
<br/>
<security:authorize access="hasRole('MEMBER')">
<h3><spring:message code="request.parade.list" /></h3>
<display:table name="parades" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
	<spring:message code="request.parade.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true"/>

		<display:column>
		
				<a href="request/member/create.do?paradeId=${row.id}" >
						<spring:message code="request.create" />
				</a>					
				
		</display:column>
		
</display:table>	
</security:authorize>


