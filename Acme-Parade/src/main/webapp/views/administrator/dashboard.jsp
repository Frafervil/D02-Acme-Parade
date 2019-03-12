

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<security:authorize access="hasRole('ADMIN')">
	
	<h3><spring:message code="administrator.statistics" /></h3>
	
	<table class="displayStyle">
		<tr>
			<th colspan="5"><spring:message code="administrator.statistics" /></th>
		</tr>
		
		<tr>
			<th><spring:message code="administrator.metrics" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.minimum" /></th>
			<th><spring:message code="administrator.maximum" /></th>
			<th><spring:message code="administrator.std" /></th>
		</tr>
		
		<tr>
			<td><spring:message code="administrator.memberPerBrotherhood" /></td>
			<td><jstl:out value="${avgMemberPerBrotherhood }" /></td>
			<td><jstl:out value="${minMemberPerBrotherhood }" /></td>
			<td><jstl:out value="${maxMemberPerBrotherhood }" /></td>
			<td><jstl:out value="${stddevMemberPerBrotherhood }" /></td>
		</tr>
		
	</table>
	
	<h3><spring:message code="administrator.ratios" /></h3>
	
	<table class="displayStyle">
		<tr>
			<th colspan="2"><spring:message code="administrator.ratios" /></th>
		</tr>
		
		<tr>
			<th><spring:message code="administrator.metrics" /></th>
			<th><spring:message code="administrator.value" /></th>
		</tr>
		
		<tr>
			<td><spring:message code="administrator.ratioStatusPENDING" /></td>
			<td><jstl:out value="${ratioPendingRequest }" /></td>
		</tr>
		<tr>
			<td><spring:message code="administrator.ratioStatusAPPROVED" /></td>
			<td><jstl:out value="${ratioapprovedRequest }" /></td>
		</tr>
		<tr>
			<td><spring:message code="administrator.ratioStatusPENDING" /></td>
			<td><jstl:out value="${ratioRejectedRequest }" /></td>
		</tr>


	</table>
	
	<h3><spring:message code="administrator.largest.brotherhood" /></h3>
	<jstl:out value="${largestBrotherhood.title}"></jstl:out>
	
	<h3><spring:message code="administrator.smallest.brotherhood" /></h3>
	<jstl:out value="${smallestBroterhood.title}"></jstl:out>
	
	
	<h3><spring:message code="administrator.startingSoonParade" /></h3>
	
	<display:table pagesize="10" class="displaytag" 
	name="startingSoonParade" requestURI="dashboard/administrator/display.do" id="parade">
		
		<spring:message code="administrator.parade.title" var="title" />
		<display:column property="title" title="${title }" sortable="true"/>

		<display:column>
			<a href="parade/display.do?paradeId=${parade.id }"><spring:message code="administrator.display"  /></a>
		</display:column>
	</display:table>
	
	<h3><spring:message code="administrator.membersMostapproved" /></h3>
	
	<display:table pagesize="10" class="displaytag" 
	name="membersRequestapproved" requestURI="dashboard/administrator/display.do" id="member">
		
		<spring:message code="administrator.name" var="name" />
		<display:column property="name" title="${name }" sortable="true"/>

		<spring:message code="administrator.surname" var="surname" />
		<display:column property="surname" title="${surname }" sortable="true"/>
	</display:table>	
	
	<h3><spring:message code="administrator.histogramPositions" /></h3>
<jstl:out value="${positionStats}"></jstl:out>
<canvas id="myChart" width="500px"></canvas>

</security:authorize>
<script>
window.onload = function() {
var position = "<jstl:out value='${position}' />";
var count = "<jstl:out value='${count}' />";

var data = [];
var columnas =[];
console.log(data);
for(var i in position){
	columnas.push(position[i]); 
	data.push(count[i]);  
	
}
var title = "Title";
var ctx = document.getElementById('myChart').getContext('2d');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'bar',

    // The data for our dataset
    data: {
        labels: columnas,
        datasets: [{
            label: title,
            backgroundColor: 'rgb(87, 35, 100)',
            borderColor: 'rgb(87, 35, 100)',
            data: data,
        }]
    },

    // Configuration options go here
    options: {scales: {
        yAxes: [{
            ticks: {
                beginAtZero: true
            }
        }]
    }}
});
};
</script>