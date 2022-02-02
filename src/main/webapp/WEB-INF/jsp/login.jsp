<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log in</title>
</head>
<body>
<h3>Pleas log in</h3>
<form name="login-form" action="<c:url value="/controller?command=login"/>" method="post">
    <label for="login-input">Login:</label>
    <input id="login-input" type="text" name="login" value="">
    <br>
    <label for="password-input">Password:</label>
    <input id="password-input" type="password" name="password" value="">
    <br>
    ${requestScope.errorLoginPassMessage}
    <br/>
    <input type="submit" value="Log in">
</form>
</body>
</html>
