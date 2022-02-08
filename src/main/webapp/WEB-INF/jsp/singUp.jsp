<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.singUp" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.registration" var="registration"/>
<fmt:message bundle="${loc}" key="label.login" var="login"/>
<fmt:message bundle="${loc}" key="label.emailAddress" var="email"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstName"/>
<fmt:message bundle="${loc}" key="label.secondName" var="secondName"/>
<fmt:message bundle="${loc}" key="label.password" var="password"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="repeatPassword"/>
<fmt:message bundle="${loc}" key="button.singUp" var="singUp"/>
<fmt:message bundle="${loc}" key="error.passwordMismatch" var="passwordMismatch"/>
<fmt:message bundle="${loc}" key="error.passwordLength" var="passwordLength"/>
<fmt:message bundle="${loc}" key="error.emailBusy" var="emailBusy"/>
<fmt:message bundle="${loc}" key="error.loginBusy" var="loginBusy"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.84.0">
    <title>${title}</title>

    <%--    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">--%>


    <!-- Bootstrap core CSS -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet">

    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>


    <!-- Custom styles for this template -->
    <%--    <link href="../../css/sticky-footer-navbar.css" rel="stylesheet">--%>
    <link href="../../css/singin.css" rel="stylesheet">
</head>
<body class="text-center">

<main class="form-signin">
    <form action="<c:url value="/controller?command=singUp"/>" method="post">
        <img class="mb-4" src="../../svg/bootstrap-logo.svg" alt="" width="72" height="57">
        <h1 class="h3 mb-3 fw-normal">${registration}</h1>

        <div class="form-floating">
            <input type="text" class="form-control" id="login" name="login" placeholder="${login}">
            <label for="login">${login}</label>
        </div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" name="email" placeholder="${email}">
            <label for="email">${email}</label>
        </div>
        <div class="form-floating">
            <input type="text" class="form-control" id="firstName" name="firstName" placeholder="${firstName}">
            <label for="firstName">${firstName}</label>
        </div>
        <div class="form-floating">
            <input type="text" class="form-control" id="secondName" name="secondName" placeholder="${secondName}">
            <label for="secondName">${secondName}</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="password" name="password" placeholder="${password}">
            <label for="password">${password}</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="repeatPassword" name="repeatPassword"
                   placeholder="${repeatPassword}">
            <label for="repeatPassword">${repeatPassword}</label>
        </div>

        <c:if test="${not empty requestScope.errorRegistrationMessage}">
            <c:if test="${requestScope.errorRegistrationMessage eq 'password length'}">
                <div class="form-floating" role="alert">
                    <div class="alert alert-danger" role="alert">
                            ${passwordLength}
                    </div>
                </div>
            </c:if>
            <c:if test="${requestScope.errorRegistrationMessage eq 'password mismatch'}">
                <div class="form-floating" role="alert">
                    <div class="alert alert-danger" role="alert">
                            ${passwordMismatch}
                    </div>
                </div>
            </c:if>
            <c:if test="${requestScope.errorRegistrationMessage eq 'login busy'}">
                <div class="form-floating" role="alert">
                    <div class="alert alert-danger" role="alert">
                            ${loginBusy}
                    </div>
                </div>
            </c:if>
            <c:if test="${requestScope.errorRegistrationMessage eq 'email busy'}">
                <div class="form-floating" role="alert">
                    <div class="alert alert-danger" role="alert">
                            ${emailBusy}
                    </div>
                </div>
            </c:if>
        </c:if>

        <button class="w-100 btn btn-lg btn-dark" type="submit">${singUp}</button>
        <p class="mt-5 mb-3 text-muted">&copy; 2021–2022</p>
    </form>
</main>

</body>
</html>

