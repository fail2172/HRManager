<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/tld/customTafLib" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.84.0">
    <title>Headers · Bootstrap v5.0</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/headers/">

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

<div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white" style="width: 380px;">
    <a href="/" class="d-flex align-items-center flex-shrink-0 p-3 link-dark text-decoration-none border-bottom">
        <svg class="bi me-2" width="30" height="24"><use xlink:href="#bootstrap"/></svg>
        <span class="fs-5 fw-semibold">List group</span>
    </a>
    <div class="list-group list-group-flush border-bottom scrollarea">
        <a href="#" class="list-group-item list-group-item-action active py-3 lh-tight" aria-current="true">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small>Wed</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Tues</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Mon</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>

        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight" aria-current="true">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Wed</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Tues</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Mon</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight" aria-current="true">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Wed</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Tues</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Mon</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight" aria-current="true">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Wed</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Tues</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
        <a href="#" class="list-group-item list-group-item-action py-3 lh-tight">
            <div class="d-flex w-100 align-items-center justify-content-between">
                <strong class="mb-1">List group item heading</strong>
                <small class="text-muted">Mon</small>
            </div>
            <div class="col-10 mb-1 small">Some placeholder content in a paragraph below the heading and date.</div>
        </a>
    </div>
</div>

<script src="../../js/bootstrap.bundle.min.js"></script>

</body>
</html>

