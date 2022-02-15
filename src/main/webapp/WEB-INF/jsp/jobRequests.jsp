<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.jobRequestsPage" var="loc"/>
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
<fmt:message bundle="${loc}" key="label.accept" var="accept"/>
<fmt:message bundle="${loc}" key="label.reject" var="reject"/>

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
    <div class="container-fluid">
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <div class="container-fluid">
                    <div class="list-group-flush border-bottom">
                        <c:forEach var="jobRequest" items="${requestScope.jobRequests}">
                            <br>
                            <div class="card" style="width: 44rem;">
                                <div class="card-header">
                                    <h1>${jobRequest.vacancy.title}</h1>
                                </div>
                                <div class="list-group-item py-3 lh-tight">
                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                        <strong class="mb-1">${jobRequest.vacancy.salary}</strong>
                                        <small>
                                                ${jobRequest.account.user.firstName} ${jobRequest.account.user.secondName}
                                        </small>
                                    </div>
                                    <div class="col-10 mb-1 small">
                                        <small class="text-muted">${jobRequest.vacancy.employer.name}</small><br>
                                        <small class="text-muted">${jobRequest.vacancy.city.name}</small><br>
                                        <small class="text-muted">${jobRequest.vacancy.employment.name}</small><br>
                                        <c:if test="${jobRequest.vacancy.experience > 0}">
                                            <small class="text-muted">work experience from ${jobRequest.vacancy.experience} years</small><br>
                                        </c:if>
                                        <label class="text">${jobRequest.vacancy.description.get()}</label><br>
                                    </div>
                                    <div class="row">
                                        <br>
                                        <form action="<c:url value="/controller?command=goToInterviewCreationPage"/>"
                                              method="post">
                                            <div class="col-4 form-floating">
                                                <button class="w-100 btn btn-success" type="submit"
                                                        name="jobRequestId" value="${jobRequest.id}">
                                                    ${accept}
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                    <br>
                                    <div class="row">
                                        <br>
                                        <form action="<c:url value="/controller?command=rejectApplication"/>"
                                              method="post">
                                            <div class="col-4 form-floating">
                                                <button class="w-100 btn btn-danger" type="submit"
                                                        name="jobRequestId" value="${jobRequest.id}">
                                                    ${reject}
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <br>
                    </div>
                </div>
                <div class="col-3"></div>
            </div>
        </div>
    </div>
</main>

<div class="b-example-divider"></div>

<script src="../../js/bootstrap.bundle.min.js"></script>
<script src="../../js/locale.js"></script>

</body>
</html>
