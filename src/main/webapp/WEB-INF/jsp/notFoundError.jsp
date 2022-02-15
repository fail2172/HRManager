<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.notFoundError" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.message" var="message"/>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <title>${title}</title>

    <link href="../../css/bootstrap.min.css" rel="stylesheet">

</head>
<body class="text-center">
<main>
    <br>
    <br>
    <br>
    <br>
    <div class="container-fluid">
        <div class="row">
            <div class="col-4"></div>
            <div class="col-4"><h4>404: ${message}</h4>
                <img class="mb-4" src="../../svg/sadCat.svg" alt="" width="300" height="300">
            </div>
            <div class="col-4"></div>
        </div>
    </div>
    </div>
</main>
</body>
</html>
