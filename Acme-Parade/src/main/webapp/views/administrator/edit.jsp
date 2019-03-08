<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="administrator/edit.do" modelAttribute="administrator">
	<form:hidden path="id" />
	<form:hidden path="version"/>

	<div class="ui equal width form">
		<div class="fields">
			<!-- Name -->
			<acme:textbox code="administrator.name" path="name" placeholder="Homer"/>
			<!-- MiddleName -->
			<acme:textbox code="administrator.middleName" path="middleName" placeholder="Jay"/>
			<!-- Surname -->
			<acme:textbox code="administrator.surname" path="surname" placeholder="Simpson"/>
		</div>
		<div class="fields">
			<!-- Email -->
			<acme:textbox code="administrator.email" path="email" placeholder="homerjsimpson@"/>
			<!-- Phone Number -->
			<acme:textbox code="administrator.phone" path="phone" placeholder="+34 600123456"/>
		</div>
		<div class="fields">
			<!-- Address -->
			<acme:textbox code="administrator.address" path="address" placeholder="123 Main St Anytown, Australia"/>
			<!-- Photo -->
			<acme:textbox code="administrator.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
		</div>
	</div>

	
	<input type="submit" name="save" id="save"
		value="<spring:message code="administrator.save" />" />
	
	<acme:cancel url="administrator/viewProfile.do" code="administrator.cancel"/>


</form:form>


<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='admin.confirmationPhone'/>","${countryCode}");});
</script>