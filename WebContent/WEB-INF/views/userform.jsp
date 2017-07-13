<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
   <head><title><spring:message code="user.title"/></title></head>
<body>
  <a href="user?language=en">English </a> | <a href="user?language=de">German </a>
  <h3> <spring:message code="user.title"/></h3>
<table>	
  <form:form action="save" method="POST" commandName="user">
	<tr><td><spring:message code="user.name"/>:</td> <td><form:input  path="name"/> </td> </tr> 
	<tr> <td> <spring:message code="user.age"/> :</td> <td><form:input path="age"/> </td> </tr> 
	<tr> <td colspan=2>  
	   <spring:message code="user.submit" var="valSubmit"></spring:message> 
	   <input type="submit" value="${valSubmit}">
	</td></tr>
  </form:form>
 </table>
</body>
</html>
