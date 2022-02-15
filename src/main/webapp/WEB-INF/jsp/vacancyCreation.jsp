<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.vacancyCreationPage" var="loc"/>
<fmt:message bundle="${loc}" key="label.title" var="title"/>
<fmt:message bundle="${loc}" key="label.home" var="home"/>
<fmt:message bundle="${loc}" key="label.jobRequests" var="jobRequests"/>
<fmt:message bundle="${loc}" key="label.users" var="users"/>
<fmt:message bundle="${loc}" key="label.lang" var="lang"/>
<fmt:message bundle="${loc}" key="label.search" var="search"/>
<fmt:message bundle="${loc}" key="label.profile" var="profile"/>
<fmt:message bundle="${loc}" key="label.singOut" var="singOut"/>
<fmt:message bundle="${loc}" key="label.vacancyTitle" var="vacancyTitle"/>
<fmt:message bundle="${loc}" key="label.employer" var="employer"/>
<fmt:message bundle="${loc}" key="label.city" var="city"/>
<fmt:message bundle="${loc}" key="label.incomeLevel" var="incomeLevel"/>
<fmt:message bundle="${loc}" key="label.typeOfEmployment" var="typeOfEmployment"/>
<fmt:message bundle="${loc}" key="label.fullEmployment" var="fullEmployment"/>
<fmt:message bundle="${loc}" key="label.partTimeEmployment" var="partTimeEmployment"/>
<fmt:message bundle="${loc}" key="label.internship" var="internship"/>
<fmt:message bundle="${loc}" key="label.projectWork" var="projectWork"/>
<fmt:message bundle="${loc}" key="label.volunteering" var="volunteering"/>
<fmt:message bundle="${loc}" key="label.experience" var="experience"/>
<fmt:message bundle="${loc}" key="label.description" var="description"/>
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
                <form class="row g-3" action="<c:url value="/controller?command=vacancyCreation"/>" method="post">
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="title" name="title"
                                   placeholder="${vacancyTitle}">
                            <label for="title">${vacancyTitle}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="employer" name="employer"
                                   placeholder="${employer}">
                            <label for="employer">${employer}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="city" name="city" placeholder="${city}">
                            <label for="city">${city}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <input type="number" class="form-control" id="salary" name="salary"
                                   placeholder="${incomeLevel}">
                            <label for="salary">${incomeLevel}</label>
                        </div>
                    </div>
                    <div class="col-12">
                        <select class="form-select" id="employmentType" name="employmentType">
                            <option selected value="null">${typeOfEmployment}</option>
                            <option value="FULL_EMPLOYMENT">${fullEmployment}</option>
                            <option value="PART_TIME_EMPLOYMENT">${partTimeEmployment}</option>
                            <option value="INTERNSHIP">${internship}</option>
                            <option value="PROJECT_WORK">${projectWork}</option>
                            <option value="VOLUNTEERING">${volunteering}</option>
                        </select>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <div class="input-group mb-3">
                                <span class="input-group-text">${experience}:</span>
                                <input type="text" class="form-control" name="experience" aria-describedby="experience">
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-floating">
                            <textarea class="form-control" id="description" name="description" placeholder="${description}"
                                      style="height: 100px"></textarea>
                            <label for="description">${description}</label>
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
                                ${errorMessage}
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
</main>

<div class="b-example-divider"></div>

<script src="../../js/bootstrap.bundle.min.js"></script>
<script src="../../js/locale.js"></script>

</body>
</html>
