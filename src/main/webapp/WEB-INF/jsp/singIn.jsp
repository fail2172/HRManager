<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.singIn" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.pleaseLogIn" var="pleaseSingIn"/>
<fmt:message bundle="${loc}" key="label.emailAddress" var="emailAddress"/>
<fmt:message bundle="${loc}" key="label.password" var="password"/>
<fmt:message bundle="${loc}" key="label.forTheFirstTimeWithUs" var="forTheFirstTimeWithUs"/>
<fmt:message bundle="${loc}" key="link.register" var="register"/>
<fmt:message bundle="${loc}" key="button.singIn" var="singInButton"/>
<fmt:message bundle="${loc}" key="error.LoginPassMessage" var="loginPassErrorMessage"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${title}</title>

    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/singin.css" rel="stylesheet">

</head>
<body class="text-center">
<main class="form-signin">
    <form action="<c:url value="/controller?command=singIn"/>" method="post">
        <img class="mb-4" src="../../svg/bootstrap-logo.svg" alt="" width="72" height="57">
        <h1 class="h3 mb-3 fw-normal">${pleaseSingIn}</h1>

        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" placeholder="${emailAddress}">
            <label for="email">${emailAddress}</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="password" name="password" placeholder="${password}">
            <label for="password">${password}</label>
        </div>

        <c:if test="${not empty requestScope.errorLoginPassMessage}">
            <div class="form-floating" role="alert">
                <div class="alert alert-danger" role="alert">
                        ${loginPassErrorMessage}
                </div>
            </div>
        </c:if>

        <div class="form-floating" role="alert">
            <div class="alert alert-secondary" role="alert">
                ${forTheFirstTimeWithUs}<br>
                <a href="<c:url value="/controller?command=singUp_page"/>" class="alert-link">${register}</a>.
            </div>
        </div>
        <button class="w-100 btn btn-lg btn-success" type="submit">${singInButton}</button>
        <p class="mt-5 mb-3 text-muted">&copy; 2021–2022</p>
    </form>
</main>
</body>
</html>
