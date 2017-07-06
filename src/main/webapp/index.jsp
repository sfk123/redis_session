<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
<h2>Hello World!</h2>
<form action="login">
    <input type="text" name="username">
    <input type="password" name="password">
    <input type="submit" value="提交">
</form>
<%=session.getId()%>
<script>
    <c:if test="${not empty message}">
        alert("${message}");
    </c:if>
</script>
</body>
</html>
