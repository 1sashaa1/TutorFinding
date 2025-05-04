<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" crossorigin="anonymous"></script>
    <title>Tutor Dashboard</title>
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }

        .wrapper {
            display: flex;
            flex-direction: column;
            min-height: 100%;
        }

        .main-content {
            flex: 1;
            padding: 20px 0;
        }

        .dashboard-card {
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }

        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }

        .stat-card {
            height: 100%;
        }

        .stat-value {
            font-size: 2.5rem;
            font-weight: bold;
        }

        .upcoming-lessons {
            max-height: 400px;
            overflow-y: auto;
        }

        footer {
            background-color: #f8f9fa;
            padding: 10px 0;
            text-align: center;
            margin-top: 20px;
        }

        .navbar-brand h4 {
            margin: 0;
            color: #333;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <h4>Tutor Dashboard</h4>
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto"></ul>
                <ul class="navbar-nav">
                    <li class="nav-item active">
                        <a class="nav-link" href="/tutor/schedule">Список занятий</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="/tutorProfileDisplay">Профиль</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="/logout">Выйти</a>
                    </li>
                </ul>
                <span class="navbar-text ml-2">
                    <i class="fas fa-user"></i> ${username}
                </span>
            </div>
        </div>
    </nav>

    <div class="main-content">
        <div class="container">
            <!-- Статистика -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card dashboard-card stat-card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title">Предстоящие занятия</h5>
                            <p class="stat-value text-primary">${upcomingLessonsCount}</p>
                            <a href="/tutor/schedule" class="btn btn-primary">Перейти</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card dashboard-card stat-card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title">Всего студентов</h5>
                            <p class="stat-value text-success">${totalStudents}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card dashboard-card stat-card bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title">Рейтинг</h5>
                            <p class="stat-value text-warning">${rating}</p>
                            <a href="/tutor/reviews" class="btn btn-warning">Отзывы</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Основной контент -->
            <div class="row">
                <!-- Ближайшие занятия -->
                <div class="col-md-6">
                    <div class="card dashboard-card">
                        <div class="card-header bg-primary text-white">
                            <h5><i class="fas fa-calendar-alt"></i> Ближайшие занятия</h5>
                        </div>
                        <div class="card-body upcoming-lessons">
                            <c:choose>
                                <c:when test="${not empty upcomingLessons}">
                                    <div class="list-group">
                                        <c:forEach items="${upcomingLessons}" var="lesson">
                                            <a href="/lesson/${lesson.id}" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h6 class="mb-1">${lesson.studentName} - ${lesson.subject}</h6>
                                                    <small>
                                                        <fmt:formatDate value="${lesson.date}" pattern="dd.MM.yy"/>
                                                    </small>
                                                </div>
                                                <p class="mb-1">
                                                    <fmt:formatDate value="${lesson.startTime}" pattern="HH:mm"/> -
                                                    <fmt:formatDate value="${lesson.endTime}" pattern="HH:mm"/>
                                                </p>
                                                <small class="text-muted">${lesson.status}</small>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">Нет предстоящих занятий</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="card-footer">
                            <a href="/tutor/schedule" class="btn btn-outline-primary btn-block">Все занятия</a>
                        </div>
                    </div>
                </div>

                <!-- Последние уведомления -->
                <div class="col-md-6">
                    <div class="card dashboard-card">
                        <div class="card-header bg-info text-white">
                            <h5><i class="fas fa-bell"></i> Уведомления</h5>
                        </div>
                        <div class="card-body upcoming-lessons">
                            <c:choose>
                                <c:when test="${not empty notifications}">
                                    <div class="list-group">
                                        <c:forEach items="${notifications}" var="notification">
                                            <a href="#" class="list-group-item list-group-item-action">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h6 class="mb-1">${notification.title}</h6>
                                                    <small>
                                                        <fmt:formatDate value="${notification.date}" pattern="dd.MM.yy"/>
                                                    </small>
                                                </div>
                                                <p class="mb-1">${notification.message}</p>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">Нет новых уведомлений</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="card-footer">
                            <a href="/tutor/notifications" class="btn btn-outline-info btn-block">Все уведомления</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Быстрые действия -->
            <div class="row mt-4">
                <div class="col-md-12">
                    <div class="card dashboard-card">
                        <div class="card-header bg-success text-white">
                            <h5><i class="fas fa-bolt"></i> Быстрые действия</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-around">
                                <a href="/tutorProfileDisplay" class="btn btn-success">
                                    <i class="fas fa-user-edit"></i> Редактировать профиль
                                </a>
                                <a href="/tutor/materials" class="btn btn-success">
                                    <i class="fas fa-book"></i> Учебные материалы
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <div class="container">
            <p>&copy; 2025 Tutor Searching. All rights reserved</p>
        </div>
    </footer>
</div>
</body>
</html>