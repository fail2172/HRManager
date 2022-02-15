<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.hrmanager.model.JobRequestStatus" %>
<%@ page import="com.epam.jwd.hrmanager.model.Role" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.personalAreaPage" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.home" var="home"/>
<fmt:message bundle="${loc}" key="label.jobRequests" var="jobRequests"/>
<fmt:message bundle="${loc}" key="label.users" var="users"/>
<fmt:message bundle="${loc}" key="label.lang" var="lang"/>
<fmt:message bundle="${loc}" key="label.search" var="search"/>
<fmt:message bundle="${loc}" key="label.profile" var="profile"/>
<fmt:message bundle="${loc}" key="label.singOut" var="singOut"/>
<fmt:message bundle="${loc}" key="label.login" var="login"/>
<fmt:message bundle="${loc}" key="label.email" var="email"/>
<fmt:message bundle="${loc}" key="label.firstName" var="firstName"/>
<fmt:message bundle="${loc}" key="label.secondName" var="secondName"/>
<fmt:message bundle="${loc}" key="label.edit" var="edit"/>
<fmt:message bundle="${loc}" key="label.admin" var="admin"/>
<fmt:message bundle="${loc}" key="label.manager" var="manager"/>
<fmt:message bundle="${loc}" key="label.aspirant" var="aspirant"/>
<fmt:message bundle="${loc}" key="label.statements" var="statements"/>
<fmt:message bundle="${loc}" key="label.interviews" var="interviews"/>


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
            <div class="col-1"></div>
            <div class="col-6">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-6">
                            <p class="fs-3 text-muted mb-3">${login}: </p>
                            <p class="fs-3 text-muted mb-3">${email}: </p>
                            <p class="fs-3 text-muted mb-3">${firstName}: </p>
                            <p class="fs-3 text-muted mb-3">${secondName}: </p>
                        </div>
                        <div class="col-6">
                            <p class="fs-3 mb-3">
                                ${sessionScope.sessionAccount.login}
                                <c:if test="${sessionScope.sessionAccount.role eq Role.ADMINISTRATOR}">
                                    <span class="badge rounded-pill bg-danger">${admin}</span>
                                </c:if>
                                <c:if test="${sessionScope.sessionAccount.role eq Role.MANAGER}">
                                    <span class="badge rounded-pill bg-primary">${manager}</span>
                                </c:if>
                                <c:if test="${sessionScope.sessionAccount.role eq Role.ASPIRANT}">
                                    <span class="badge rounded-pill bg-success">${aspirant}</span>
                                </c:if>
                            </p>
                            <p class="fs-3 mb-3">${sessionScope.sessionAccount.email}</p>
                            <p class="fs-3 mb-3">${sessionScope.sessionAccount.user.firstName}</p>
                            <p class="fs-3 mb-3">${sessionScope.sessionAccount.user.secondName}</p>
                        </div>
                    </div>
                </div>
                <br>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-3"></div>
                        <div class="col-6">
                            <a href="<c:url value="/controller?command=editProfilePage"/>" type="button"
                               class="btn btn-lg btn-primary">${edit}</a>
                        </div>
                        <div class="col-3"></div>
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="accordion accordion-flush" id="accordionFlushExample">
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="flush-headingOne">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#flush-collapseOne" aria-expanded="false"
                                    aria-controls="flush-collapseOne">
                                ${statements}
                            </button>
                        </h2>
                        <div id="flush-collapseOne" class="accordion-collapse collapse"
                             aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushExample">
                            <div class="accordion-body">
                                <div class="container-fluid">
                                    <c:forEach var="jobRequest" items="${requestScope.jobRequests}">
                                        <c:if test="${jobRequest.status eq JobRequestStatus.FIELD}">
                                            <div class="card bg-primary" style="width: 25rem;">
                                                <div class="card-header">
                                                    <div type="text">${jobRequest.vacancy.title}</div>
                                                </div>
                                                <div class="list-group-item py-3 lh-tight">
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1">${jobRequest.vacancy.salary}</strong>
                                                    </div>
                                                    <div class="col-10 mb-1 small">
                                                        <small class="text-muted">${jobRequest.vacancy.employer.name}</small><br>
                                                        <small class="text-muted">${jobRequest.vacancy.city.name}</small><br>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                        </c:if>
                                        <c:if test="${jobRequest.status eq JobRequestStatus.APPROVED}">
                                            <div class="card bg-success" style="width: 25rem;">
                                                <div class="card-header">
                                                    <div type="text">${jobRequest.vacancy.title}</div>
                                                </div>
                                                <div class="list-group-item py-3 lh-tight">
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1">${jobRequest.vacancy.salary}</strong>
                                                    </div>
                                                    <div class="col-10 mb-1 small">
                                                        <small class="text-muted">${jobRequest.vacancy.employer.name}</small><br>
                                                        <small class="text-muted">${jobRequest.vacancy.city.name}</small><br>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                        </c:if>
                                        <c:if test="${jobRequest.status eq JobRequestStatus.DENIED}">
                                            <div class="card bg-danger" style="width: 25rem;">
                                                <div class="card-header">
                                                    <div type="text">${jobRequest.vacancy.title}</div>
                                                </div>
                                                <div class="list-group-item py-3 lh-tight">
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1">${jobRequest.vacancy.salary}</strong>
                                                    </div>
                                                    <div class="col-10 mb-1 small">
                                                        <small class="text-muted">${jobRequest.vacancy.employer.name}</small><br>
                                                        <small class="text-muted">${jobRequest.vacancy.city.name}</small><br>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="flush-headingTwo">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#flush-collapseTwo" aria-expanded="false"
                                    aria-controls="flush-collapseTwo">
                                ${interviews}
                            </button>
                        </h2>
                        <div id="flush-collapseTwo" class="accordion-collapse collapse"
                             aria-labelledby="flush-headingTwo" data-bs-parent="#accordionFlushExample">
                            <div class="accordion-body">
                                <c:forEach var="interview" items="${requestScope.interviews}">
                                    <div class="card" style="width: 25rem;">
                                        <div class="card-header">
                                            <div type="text">${interview.vacancy.title}</div>
                                        </div>
                                        <div class="list-group-item py-3 lh-tight">
                                            <div class="d-flex w-100 align-items-center justify-content-between">
                                                <strong class="mb-1">${interview.date}</strong>
                                                <strong class="mb-1">${interview.time}</strong>
                                            </div>
                                            <div class="col-10 mb-1 small">
                                                <small class="text-muted">${interview.address.city.name}</small><br>
                                                <small class="text-muted">${interview.address.street.name}</small><br>
                                                <small class="text-muted">${interview.address.houseNumber}</small><br>
                                            </div>
                                        </div>
                                    </div>
                                    <br>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
    <br>
    <br>
</main>

<script src="../../js/bootstrap.bundle.min.js"></script>
<script src="../../js/locale.js"></script>

</body>
</html>
