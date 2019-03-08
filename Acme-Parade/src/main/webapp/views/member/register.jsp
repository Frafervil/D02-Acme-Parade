

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


<form:form action="member/register.do" modelAttribute="memberForm" >


	<form:hidden path="idMember"/>
	
	<acme:displayText dataLabel="Terms and conditions" code="member.confirmTerms"/>
		<br/>
		<br/>
	
	<fieldset>
    <legend><spring:message code="member.fieldset.personalInformation"/></legend>
	<acme:textbox code="member.name" path="name" placeholder="Homer"/>
	<acme:textbox code="member.middleName" path="middleName" placeholder="Jay"/>
	<acme:textbox code="member.surname" path="surname" placeholder="Simpson"/>
	<acme:textbox code="member.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
	<acme:textbox code="member.email" path="email" placeholder="homerjsimpson@email.com"/>
	<acme:textbox code="member.phone" path="phone" placeholder="+34 600 1234"/>
	<acme:textbox code="member.address" path="address" placeholder="123 Main St Anytown, Australia"/>
	</fieldset>
	<br/>
	
	<fieldset>
    <legend><spring:message code="member.fieldset.userAccount"/></legend>
	<acme:textbox code="member.username" path="username" placeholder="HomerS"/>
	
	<acme:password code="member.password" path="password"/>
	<acme:password code="member.passwordChecker" path="passwordChecker"/>
	
	</fieldset>
	<br/>
	
	<acme:checkbox code="member.confirmTerms" path="checkBox"/>
		
		<a href="terms/terms.do"><spring:message code="member.terms"/></a>
		<br/>
		<br/>
	
	<input type="submit" name="register" id="register"
		value="<spring:message code="member.save" />" >&nbsp; 
	
	<acme:cancel url="welcome/index.do" code="member.cancel"/>

</form:form>


<script type="text/javascript">
$("#register").on("click",function(){validatePhone("<spring:message code='member.confirmationPhone'/>","${countryCode}");});
</script>





