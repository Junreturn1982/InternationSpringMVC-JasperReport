<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
   <head><title><spring:message code="user.title"/></title></head>
<body>
  <a href="user?language=en">English </a> | <a href="user?language=de">German </a>
  <br/><br/>
  <a href="./" target="_blank">Print pdf</a>
  <h3> <spring:message code="user.success"/></h3>
</body>
</html>
