<%--
 * register.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
		

	<form:form action="brotherhood/register.do" modelAttribute="brotherhoodForm">
		
		<form:hidden path="id" />
		
		<acme:displayText dataLabel="Terms and conditions" code="brotherhood.confirmTerms"/>
		<br/>
		<br/>
		<fieldset>
    	<legend><spring:message code="brotherhood.fieldset.personalInformation"/></legend>
		<acme:textbox code="brotherhood.name" path="name" placeholder="Homer"/>
		<acme:textbox code="brotherhood.middleName" path="middleName" placeholder="Jay"/>
		<acme:textbox code="brotherhood.surname" path="surname" placeholder="Simpson"/>
		<acme:textbox code="brotherhood.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
		<acme:textbox code="brotherhood.email" path="email" placeholder="homerjsimpson@email.com"/>
		<acme:textbox code="brotherhood.phone" path="phone" placeholder="+34 600 1234"/>
		<acme:textbox code="brotherhood.address" path="address" placeholder="123 Main St Anytown, Australia"/>
		<acme:textbox code="brotherhood.title" path="title" placeholder="La Pasion"/>
		
		
		<div>
			<form:label path="area">
		<spring:message code="brotherhood.name" />
	</form:label>	
	<form:select id="area" path="area">
		<form:options items="${areas}" itemLabel="name" />
	</form:select>
	<form:errors path="area" cssClass="error" />
		</div>
		</fieldset>
		<fieldset>
    	<legend><spring:message code="brotherhood.fieldset.userAccount"/></legend>
		<acme:textbox code="brotherhood.username" path="username" placeholder="HomerS"/>
		
		<acme:password code="brotherhood.password" path="password"/>
		<acme:password code="brotherhood.passwordChecker" path="passwordChecker"/>
		
		</fieldset>
		<br/>
		
		<acme:checkbox code="brotherhood.confirmTerms" path="checkBox"/>
		
		<jstl:if test="${cookie['language'].getValue()=='en'}">
			<a href="terms/englishTerms.do"><spring:message code="brotherhood.terms"/></a>
			<br/>
			<br/>	
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='es'}">
			<a href="terms/terms.do"><spring:message code="brotherhood.terms"/></a>
		</jstl:if>
		
		<input type="submit" name="register" id="register"
		value="<spring:message code="brotherhood.save" />" >&nbsp; 
		
		<acme:cancel url="welcome/index.do" code="brotherhood.cancel"/>
	</form:form>
	
	
	<script type="text/javascript">
	$("#register").on("click",function(){validatePhone("<spring:message code='brotherhood.confirmationPhone'/>","${countryCode}");}); 
</script>

	



