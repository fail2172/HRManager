<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.mainPage" var="loc"/>
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
<fmt:message bundle="${loc}" key="label.fulTimeJob" var="fulTimeJob"/>
<fmt:message bundle="${loc}" key="label.cities" var="cities"/>
<fmt:message bundle="${loc}" key="label.typeOfEmployment" var="typeOfEmployment"/>
<fmt:message bundle="${loc}" key="label.fullEmployment" var="fullEmployment"/>
<fmt:message bundle="${loc}" key="label.PartTimeEmployment" var="PartTimeEmployment"/>
<fmt:message bundle="${loc}" key="label.internship" var="internship"/>
<fmt:message bundle="${loc}" key="label.projectWork" var="projectWork"/>
<fmt:message bundle="${loc}" key="label.volunteering" var="volunteering"/>
<fmt:message bundle="${loc}" key="label.experience" var="experience"/>
<fmt:message bundle="${loc}" key="label.1To3Years" var="from1To3Years"/>
<fmt:message bundle="${loc}" key="label.4To6Years" var="from4To6Years"/>
<fmt:message bundle="${loc}" key="label.from6" var="from6"/>
<fmt:message bundle="${loc}" key="label.incomeLevel" var="incomeLevel"/>
<fmt:message bundle="${loc}" key="label.from" var="from"/>
<fmt:message bundle="${loc}" key="label.filter" var="filter"/>
<fmt:message bundle="${loc}" key="label.add" var="add"/>
<fmt:message bundle="${loc}" key="label.respond" var="respond"/>
<fmt:message bundle="${loc}" key="label.delete" var="delete"/>

<!doctype html>
<html lang="ru">
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

            <s:authorized auth="false">
                <div class="text-end">
                    <a href="<c:url value="/controller?command=singInPage"/>" type="button"
                       class="btn btn-outline-light me-2">${singIn}</a>
                    <a href="<c:url value="/controller?command=singUpPage"/>" type="button"
                       class="btn btn-warning">${singUp}</a>
                </div>
            </s:authorized>
            <s:authorized auth="true">
                <div class="dropdown text-end">
                    <a href="#" class="d-block link-light text-decoration-none dropdown-toggle"
                       id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                    </a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                        <li>
                            <a class="dropdown-item" href="<c:url value="/controller?command=personalAreaPage"/>">${profile}</a>
                        </li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li>
                            <a class="dropdown-item" href="<c:url value="/controller?command=singOut"/>">${singOut}</a>
                        </li>
                    </ul>
                </div>
            </s:authorized>
        </div>
    </div>
</header>

<div class="b-example-divider"></div>

<main>
    <div class="container-fluid">
        <div class="row">
            <div class="col-3">
                <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white" style="width: 380px;">
                    <div class="d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom">
                        <span class="fs-5 fw-semibold">${fulTimeJob}</span>
                    </div>
                    <form action="<c:url value="/controller?command=filterVacancies"/>" method="post">
                        <div class="list-group list-group-flush border-bottom scrollarea">
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${cities}</strong>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <select class="form-select" id="cityVacancies" name="cityVacancies">
                                        <option selected></option>
                                        <c:forEach var="city" items="${requestScope.cities}">
                                            <option>${city.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${typeOfEmployment}</strong>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <select class="form-select" id="employmentType" name="employmentType">
                                        <option selected></option>
                                        <option value="FULL_EMPLOYMENT">${fullEmployment}</option>
                                        <option value="PART_TIME_EMPLOYMENT">${PartTimeEmployment}</option>
                                        <option value="INTERNSHIP">${internship}</option>
                                        <option value="PROJECT_WORK">${projectWork}</option>
                                        <option value="VOLUNTEERING">${volunteering}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${experience}</strong>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <select class="form-select" id="experience" name="experience">
                                        <option selected></option>
                                        <option value="1 3">${from1To3Years}</option>
                                        <option value="4 6">${from4To6Years}</option>
                                        <option value="6 100">${from6}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${incomeLevel} $</strong>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <div class="input-group input-group-sm mb-3">
                                        <span class="input-group-text">${from}</span>
                                        <input type="text" class="form-control" id="incomeLevel" name="incomeLevel">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-12">
                                    <button class="w-100 btn btn-lg col-6 btn-dark" type="submit">${filter}</button>
                                </div>
                            </div>
                        </div>
                        <br>
                        <br>
                    </form>
                </div>
            </div>
            <div class="col-9">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-2"></div>
                        <div class="col-8">
                            <s:security level="2">
                                <br>
                                <div class="text-end">
                                    <a href="<c:url value="/controller?command=vacancyCreationPage"/>" type="button"
                                       class="btn btn-success">${add}</a>
                                </div>
                            </s:security>
                            <c:forEach var="vacancy" items="${requestScope.vacancies}">
                                <br>
                                <div class="card" style="width: 44rem;">
                                    <div class="card-header">
                                        <h1>${vacancy.title}</h1>
                                    </div>
                                    <div class="list-group-item py-3 lh-tight">
                                        <div class="d-flex w-100 align-items-center justify-content-between">
                                            <strong class="mb-1">${vacancy.salary} $</strong>
                                            <small>${vacancy.date}</small>
                                        </div>
                                        <div class="col-10 mb-1 small">
                                            <small class="text-muted">${vacancy.employer.name}</small><br>
                                            <small class="text-muted">${vacancy.city.name}</small><br>
                                            <small class="text-muted">${vacancy.employment.name}</small><br>
                                            <c:if test="${vacancy.experience > 0}">
                                                <small class="text-muted">work experience from ${vacancy.experience} years</small><br>
                                            </c:if>
                                            <label class="text">${vacancy.description.get()}</label><br>
                                        </div>
                                        <form action="<c:url value="/controller?command=applyVacancy"/>"
                                              method="post">
                                            <div class="col-4 form-floating">
                                                <button class="w-100 btn btn-success" type="submit"
                                                        name="vacancyId" value="${vacancy.id}">
                                                    ${respond}
                                                </button>
                                            </div>
                                        </form>
                                        <s:security level="2">
                                            <br>
                                            <form action="<c:url value="/controller?command=deleteVacancy"/>"
                                                  method="post">
                                                <div class="col-4 form-floating">
                                                    <button class="w-100 btn btn-danger" type="submit"
                                                            name="vacancyId" value="${vacancy.id}">
                                                        ${delete}
                                                    </button>
                                                </div>
                                            </form>
                                        </s:security>
                                    </div>
                                </div>
                            </c:forEach>
                            <br>
                        </div>
                        <br>
                    </div>
                    <div class="col-2"></div>
                </div>
            </div>
        </div>
    </div>
    </div>
</main>

<div class="b-example-divider"></div>

<script src="../../js/bootstrap.bundle.min.js"></script>
<script src="../../js/locale.js"></script>

</body>
</html>