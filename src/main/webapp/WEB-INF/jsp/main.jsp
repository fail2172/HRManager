<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.jwd.hrmanager.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty sessionScope.account}">
        <h3>Hello, ${sessionScope.account.login}</h3>
        <c:if test="${sessionScope.account.role eq Role.ADMINISTRATOR}">
            <a href="<c:url value="/controller?command=user_page"/>">user_page</a>
            <br>
        </c:if>
        <a href="<c:url value="/controller?command=logout"/>">logout</a>
    </c:when>
    <c:otherwise>
        <a href="<c:url value="/controller?command=login_page"/>">login</a>
    </c:otherwise>
</c:choose>
</body>
</html>
