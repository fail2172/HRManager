<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.interviewCreationPage" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.home" var="home"/>
<fmt:message bundle="${loc}" key="label.jobRequests" var="jobRequests"/>
<fmt:message bundle="${loc}" key="label.users" var="users"/>
<fmt:message bundle="${loc}" key="label.lang" var="lang"/>
<fmt:message bundle="${loc}" key="label.search" var="search"/>
<fmt:message bundle="${loc}" key="label.singIn" var="singIn"/>
<fmt:message bundle="${loc}" key="label.singUp" var="singUp"/>
<fmt:message bundle="${loc}" key="label.profile" var="profile"/>
<fmt:message bundle="${loc}" key="label.singOut" var="singOut"/>
<fmt:message bundle="${loc}" key="label.city" var="city"/>
<fmt:message bundle="${loc}" key="label.street" var="street"/>
<fmt:message bundle="${loc}" key="label.house" var="house"/>
<fmt:message bundle="${loc}" key="label.flat" var="flat"/>
<fmt:message bundle="${loc}" key="label.create" var="create"/>

<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>

    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/headers.css" rel="stylesheet">

</head>
<body>

<header class="p-3 bg-dark text-white border-bottom">
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="<c:url value="/"/>" class="nav-link px-2 text-white">${home}</a></li>
                <s:security level="1">
                    <li><a href="<c:url value="/controller?command=jobRequestsPage"/>"
                           class="nav-link px-2 text-white">${jobRequests}</a></li>
                </s:security>
                <s:security level="2">
                    <li><a href="<c:url value="/controller?command=usersPage"/>"
                           class="nav-link px-2 text-white">${users}</a></li>
                </s:security>
            </ul>

            <select class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3 bg-dark text-white" id="list" onchange="setLocale()">
                <option selected>${lang}</option>
                <option value="ru_RU">Ru</option>
                <option value="en_US">En</option>
                <option value="ar_AR">Ar</option>
                <option value="zh_ZH">ZH</option>
            </select>

            <form action="<c:url value="/controller?command=searchVacancies"/>" method="post"
                  class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                <input type="search" class="form-control form-control-dark" id="search" name="search"
                       placeholder="${search}...">
            </form>

            <div class="dropdown text-end">
                <a href="#" class="d-block link-light text-decoration-none dropdown-toggle"
                   id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                </a>
                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                    <li>
                        <a class="dropdown-item"
                           href="<c:url value="/controller?command=personalAreaPage"/>">${profile}</a>
                    </li>
                    <li>
                        <hr class="dropdown-divider">
                    </li>
                    <li>
                        <a class="dropdown-item" href="<c:url value="/controller?command=singOut"/>">${singOut}</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</header>

<div class="b-example-divider"></div>

<main>
    <br>
    <br>
    <div class="container-fluid">
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <form class="row g-3" action="<c:url value="/controller?command=creteInterview"/>" method="post">
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="city" name="city" placeholder="${city}">
                            <label for="city">${city}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="street" name="street" placeholder="${street}">
                            <label for="street">${street}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="house" name="house" placeholder="${house}">
                            <label for="house">${house}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="flat" name="flat" placeholder="${flat}">
                            <label for="flat">${flat}</label>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-floating">
                            <input class="btn-lg" type="date" id="date" name="date">
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-floating">
                            <input class="btn-lg" type="time" id="time" name="time">
                        </div>
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-lg btn-primary">${create}</button>
                    </div>
                </form>
                <br>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="form-floating" role="alert">
                        <div class="alert alert-danger" role="alert">
                                ${sessionScope.errorMessage}
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
</main>

<script src="../../js/bootstrap.bundle.min.js"></script>
<script src="../../js/locale.js"></script>

</body>
</html>