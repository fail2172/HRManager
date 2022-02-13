<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.hrmanager.model.JobRequestStatus" %>
<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Headers · Bootstrap v5.0</title>

    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/headers.css" rel="stylesheet">

</head>
<body>

<svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
    <symbol id="bootstrap" viewBox="0 0 118 94">
        <title>Bootstrap</title>
        <path fill-rule="evenodd" clip-rule="evenodd"
              d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"></path>
    </symbol>
</svg>


<header class="p-3 bg-dark text-white border-bottom">
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a href="<c:url value="/"/>"
               class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <img class="mb-4" src="../../svg/whiteLogo.svg" alt="" width="40" height="40">
            </a>

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <s:security level="2">
                    <li><a href="<c:url value="/controller?command=jobRequestsPage"/>"
                           class="nav-link px-2 text-secondary">Home</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">Features</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">Pricing</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">FAQs</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">About</a></li>
                </s:security>
            </ul>

            <select class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3 bg-dark text-white" id="list" onchange="setLocale()">
                <option selected>Lang</option>
                <option value="ru_RU">Ru</option>
                <option value="en_US">En</option>
            </select>

            <form action="<c:url value="/controller?command=searchVacancies"/>" method="post"
                  class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                <input type="search" class="form-control form-control-dark" id="search" name="search"
                       placeholder="Search...">
            </form>

            <s:authorized auth="false">
                <div class="text-end">
                    <a href="<c:url value="/controller?command=singInPage"/>" type="button"
                       class="btn btn-outline-light me-2">Login</a>
                    <a href="<c:url value="/controller?command=singUpPage"/>" type="button"
                       class="btn btn-warning">Sign-up</a>
                </div>
            </s:authorized>
            <s:authorized auth="true">
                <div class="dropdown text-end">
                    <a href="#" class="d-block link-light text-decoration-none dropdown-toggle"
                       id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                        <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32"
                             class="rounded-circle">
                    </a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                        <li><a class="dropdown-item" href="#">New project...</a></li>
                        <li><a class="dropdown-item" href="#">Settings</a></li>
                        <li><a class="dropdown-item" href="#">Profile</a></li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li><a class="dropdown-item" href="<c:url value="/controller?command=singOut"/>">Sign out</a>
                        </li>
                    </ul>
                </div>
            </s:authorized>
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
                            <p class="fs-3 text-muted mb-3">login: </p>
                            <p class="fs-3 text-muted mb-3">email: </p>
                            <p class="fs-3 text-muted mb-3">first name: </p>
                            <p class="fs-3 text-muted mb-3">second name: </p>
                        </div>
                        <div class="col-6">
                            <p class="fs-3 mb-3">
                                ${sessionScope.sessionAccount.login}
                                <span class="badge rounded-pill bg-danger">Admin</span>
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
                               class="btn btn-lg btn-primary">Редактировать</a>
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
                                Заявления
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
                                Собеседования
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
