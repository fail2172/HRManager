<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>Users page</h3>
<ul>
    <c:forEach var="user" items="${requestScope.users}">
        <li>${user.firstName}</li>
        <li>${user.secondName}</li>
        <li>${user.role}</li>
    </c:forEach>
</ul>
</body>
</html>
