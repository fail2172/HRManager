<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
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
                <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap">
                    <use xlink:href="#bootstrap"></use>
                </svg>
            </a>

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <s:security level="2">
                    <li><a href="#" class="nav-link px-2 text-secondary">Home</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">Features</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">Pricing</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">FAQs</a></li>
                    <li><a href="#" class="nav-link px-2 text-white">About</a></li>
                </s:security>
            </ul>

            <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                <input type="search" class="form-control form-control-dark" placeholder="Search..."
                       aria-label="Search">
            </form>

            <s:authorized auth="false">
                <div class="text-end">
                    <a href="<c:url value="/controller?command=singIn_page"/>" type="button"
                       class="btn btn-outline-light me-2">Login</a>
                    <a href="<c:url value="/controller?command=singUp_page"/>" type="button"
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
    <div class="container-fluid">
        <div class="row">
            <div class="col-3">
                <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white" style="width: 380px;">
                    <div class="d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom">
                        <svg class="bi me-2" width="30" height="24">
                            <use xlink:href="#bootstrap"></use>
                        </svg>
                        <span class="fs-5 fw-semibold">Постоянная работа</span>
                    </div>
                    <form action="<c:url value="/controller?command=filterVacancies"/>" method="post">
                        <div class="list-group list-group-flush border-bottom scrollarea">
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">Города</strong>
                                    <small>Wed</small>
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
                                    <strong class="mb-1">Тип занятости</strong>
                                    <small>Wed</small>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <select class="form-select" id="employmentType" name="employmentType">
                                        <option selected></option>
                                        <option value="FULL_EMPLOYMENT">Полная занятость</option>
                                        <option value="PART_TIME_EMPLOYMENT">Частичная занятость</option>
                                        <option value="INTERNSHIP">Стажировка</option>
                                        <option value="PROJECT_WORK">Проектная работа</option>
                                        <option value="VOLUNTEERING">Волонтёрство</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">Опыт работы</strong>
                                    <small>Wed</small>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <select class="form-select" id="experience" name="experience">
                                        <option selected></option>
                                        <option value="1 3">От 1 до 3 лет</option>
                                        <option value="4 6">От 4 до 6 лет</option>
                                        <option value="6 100">от 6</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-group-item py-3 lh-tight" aria-current="true">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">Уровень дохода</strong>
                                    <small>Wed</small>
                                </div>
                                <div class="col-10 mb-1 small">
                                    <div class="input-group input-group-sm mb-3">
                                        <span class="input-group-text">От</span>
                                        <input type="text" class="form-control" id="incomeLevel" name="incomeLevel">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-12">
                                    <button class="w-100 btn btn-lg col-6 btn-dark" type="submit">Поиск</button>
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
                            <div class="list-group-flush border-bottom">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.vacancies}">
                                        <c:forEach var="vacancy" items="${sessionScope.vacancies}">
                                            <form method="post">
                                                <br>
                                                <div class="card" style="width: 44rem;">
                                                    <div class="card-header">
                                                        <h1>${vacancy.title}</h1>
                                                    </div>
                                                    <div class="list-group-item py-3 lh-tight">
                                                        <div class="d-flex w-100 align-items-center justify-content-between">
                                                            <strong class="mb-1">${vacancy.salary}</strong>
                                                        </div>
                                                        <div class="col-10 mb-1 small">
                                                            <small class="text-muted">${vacancy.employer.name}</small><br>
                                                            <small class="text-muted">${vacancy.city.name}</small><br>
                                                            <label class="text">${vacancy.description.get()}</label><br>
                                                        </div>
                                                        <div class="row">
                                                            <br>
                                                            <div class="col-4">
                                                                <button class="w-100 btn btn-success" type="submit">
                                                                    Откликнуться
                                                                </button>
                                                            </div>
                                                            <br>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="vacancy" items="${requestScope.vacancies}">
                                            <form method="post">
                                                <br>
                                                <div class="card" style="width: 44rem;">
                                                    <div class="card-header">
                                                        <h1>${vacancy.title}</h1>
                                                    </div>
                                                    <div class="list-group-item py-3 lh-tight">
                                                        <div class="d-flex w-100 align-items-center justify-content-between">
                                                            <strong class="mb-1">${vacancy.salary}</strong>
                                                        </div>
                                                        <div class="col-10 mb-1 small">
                                                            <small class="text-muted">${vacancy.employer.name}</small><br>
                                                            <small class="text-muted">${vacancy.city.name}</small><br>
                                                            <label class="text">${vacancy.description.get()}</label><br>
                                                        </div>
                                                        <div class="row">
                                                            <br>
                                                            <div class="col-4">
                                                                <button class="w-100 btn btn-success" type="submit">
                                                                    Откликнуться
                                                                </button>
                                                            </div>
                                                            <br>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
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

</body>
</html>