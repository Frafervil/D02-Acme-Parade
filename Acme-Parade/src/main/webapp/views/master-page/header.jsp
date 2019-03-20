<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl"  uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<a href="#"><img width="300px" src="${bannerWelcome }" alt="Acme Parade Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/list.do"><spring:message code="master.page.customer.list.brotherhoods" /></a></li>
					<li><a href="dashboard/administrator/display.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
					<li><a href="customisation/administrator/display.do"><spring:message code="master.page.administrator.customisation" /></a></li>	
					<li><a href="position/administrator/list.do"><spring:message code="master.page.administrator.position" /></a></li>	
					<li><a href="administrator/administrator/register.do"><spring:message code="master.page.administrator.register"/></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.terms" /></a>
				<ul>
					<li class="arrow"></li>
					<jstl:if test="${cookie['language'].getValue()=='en'}">
					<li><a href="terms/englishTerms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
					<jstl:if test="${cookie['language'].getValue()=='es'}">
					<li><a href="terms/terms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
				</ul>
			</li>
		</security:authorize>



		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message	code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/list.do"><spring:message code="master.page.customer.list.brotherhoods" /></a></li>
					<li><a href="member/list.do"><spring:message code="master.page.members" /></a></li>	
					<li><a href="request/brotherhood/list.do"><spring:message code="master.page.brotherhood.request" /></a></li>
					<li><a href="area/brotherhood/list.do"><spring:message code="master.page.brotherhood.area" /></a></li>					
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.terms" /></a>
				<ul>
					<li class="arrow"></li>
					<jstl:if test="${cookie['language'].getValue()=='en'}">
					<li><a href="terms/englishTerms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
					<jstl:if test="${cookie['language'].getValue()=='es'}">
					<li><a href="terms/terms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message	code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/list.do"><spring:message code="master.page.customer.list.brotherhoods" /></a></li>
					<li><a href="request/member/list.do"><spring:message code="master.page.member.request" /></a></li>
					<li><a href="enrolment/member/list.do"><spring:message code="master.page.member.brotherhood" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.terms" /></a>
				<ul>
					<li class="arrow"></li>
					<jstl:if test="${cookie['language'].getValue()=='en'}">
					<li><a href="terms/englishTerms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
					<jstl:if test="${cookie['language'].getValue()=='es'}">
					<li><a href="terms/terms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
				</ul>
			</li>
		</security:authorize>


		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message
						code="master.page.brotherhoods" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="brotherhood/list.do"><spring:message
								code="master.page.customer.list.brotherhoods" /></a></li>
					<li><a href="history/list.do"><spring:message
								code="master.page.list.histories" /></a></li>
				</ul></li>
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="member/register.do"><spring:message
								code="master.page.register.member" /></a></li>
					<li><a href="brotherhood/register.do"><spring:message
								code="master.page.register.brotherhood" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message code="master.page.terms" /></a>
				<ul>
					<li class="arrow"></li>
					<jstl:if test="${cookie['language'].getValue()=='en'}">
					<li><a href="terms/englishTerms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
					<jstl:if test="${cookie['language'].getValue()=='es'}">
					<li><a href="terms/terms.do"><spring:message
								code="master.page.terms" /></a></li>
					</jstl:if>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('BROTHERHOOD')">
					<li><a href="brotherhood/display.do"><spring:message code="master.page.brotherhood.display" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MEMBER')">
					<li><a href="member/display.do"><spring:message code="master.page.member.display" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
					<li><a href="administrator/viewProfile.do"><spring:message code="master.page.administrator.viewProfile" /></a></li>
					</security:authorize>
					<li><a href="messageBox/actor/list.do"><spring:message code="master.page.profile.messageboxes" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

