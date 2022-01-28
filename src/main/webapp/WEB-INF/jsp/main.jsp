<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>Main Page</h3>
<h1>World hello</h1>
<p>send from jsp</p>
<a href="<c:url value="/controller?command=user_page"/>">user_page</a>
</body>
</html>
