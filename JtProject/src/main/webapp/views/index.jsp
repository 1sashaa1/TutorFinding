<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" crossorigin="anonymous"></script>
    <title>Tutor Searching Platform</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --accent-color: #4cc9f0;
            --light-color: #f8f9fa;
            --dark-color: #212529;
            --success-color: #4bb543;
        }

        html, body {
            height: 100%;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f7fa;
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

        /* Navbar styling */
        .navbar {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar-brand img {
            transition: transform 0.3s;
        }

        .navbar-brand:hover img {
            transform: scale(1.05);
        }

        .navbar h4 {
            color: white;
            margin: 0;
            padding: 0 15px;
            font-weight: 500;
        }

        .nav-link {
            color: rgba(255,255,255,0.85) !important;
            font-weight: 500;
            padding: 8px 15px !important;
            margin: 0 5px;
            border-radius: 5px;
            transition: all 0.3s;
        }

        .nav-link:hover {
            color: white !important;
            background-color: rgba(255,255,255,0.15);
            transform: translateY(-2px);
        }

        /* Search section */
        .search-section {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.05);
            margin-bottom: 30px;
        }

        .search-section h1 {
            color: var(--dark-color);
            font-weight: 600;
            margin-bottom: 20px;
        }

        /* Tutor cards */
        .tutor-card {
            transition: all 0.3s ease;
        }

        .tutor-card:hover {
            transform: translateY(-5px);
        }

        .card {
            border: none;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: all 0.3s;
        }

        .card:hover {
            box-shadow: 0 10px 25px rgba(0,0,0,0.12);
        }

        .card-body {
            padding: 25px;
        }

        .rounded-circle {
            border: 3px solid var(--accent-color);
            object-fit: cover;
            margin-bottom: 15px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }

        .card h4 {
            color: var(--dark-color);
            font-weight: 600;
            margin-bottom: 10px;
        }

        .card p {
            color: #6c757d;
            margin-bottom: 5px;
        }

        .btn-primary {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
            border-radius: 50px;
            padding: 8px 20px;
            font-weight: 500;
            margin-top: 15px;
            transition: all 0.3s;
        }

        .btn-primary:hover {
            background-color: var(--secondary-color);
            border-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(67, 97, 238, 0.3);
        }

        /* Form controls */
        .form-control {
            border-radius: 50px;
            padding: 10px 20px;
            border: 1px solid #e0e0e0;
            transition: all 0.3s;
        }

        .form-control:focus {
            border-color: var(--accent-color);
            box-shadow: 0 0 0 0.2rem rgba(76, 201, 240, 0.25);
        }

        /* Footer */
        footer {
            background: linear-gradient(135deg, var(--dark-color), #343a40);
            color: white;
            padding: 20px 0;
            text-align: center;
            margin-top: 40px;
        }

        footer p {
            margin: 0;
            font-size: 14px;
            opacity: 0.8;
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .navbar h4 {
                display: none;
            }

            .card-body {
                padding: 15px;
            }
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .tutor-card {
            animation: fadeIn 0.5s ease-out forwards;
            opacity: 0;
        }

        .tutor-card:nth-child(1) { animation-delay: 0.1s; }
        .tutor-card:nth-child(2) { animation-delay: 0.2s; }
        .tutor-card:nth-child(3) { animation-delay: 0.3s; }
        .tutor-card:nth-child(4) { animation-delay: 0.4s; }
        .tutor-card:nth-child(5) { animation-delay: 0.5s; }
        .tutor-card:nth-child(6) { animation-delay: 0.6s; }

        select.form-control {
            padding-top: 0.375rem;
            padding-bottom: 0.375rem;
            color: #6c757d; /* placeholder-style */
        }
        /* Анимация мерцания */
        @keyframes blink {
            0% { opacity: 1; }
            50% { opacity: 0.3; }
            100% { opacity: 1; }
        }

        .blink-annotation {
            animation: blink 1.5s infinite;
            font-size: 0.8rem;
            white-space: nowrap;
            z-index: 1000;
        }

        .profile-link {
            padding-right: 15px; /* Добавляем место для надписи */
        }
        .video-wrapper {
            max-width: 500px; /* Можно регулировать */
            margin: 0 auto;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            background: #fff; /* Цвет рамки */
            padding: 5px; /* Толщина рамки */
        }

        /* Контейнер для сохранения пропорций */
        .video-container {
            position: relative;
            padding-bottom: 56.25%; /* 16:9 соотношение */
            height: 0;
            overflow: hidden;
            background: #000; /* Фон на случай загрузки */
            border-radius: 6px; /* На 4px меньше, чем у wrapper */
        }

        /* Стили для самого iframe */
        .video-container iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
        }

        /* Эффекты при наведении */
        .video-wrapper:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            transition: all 0.3s ease;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container">
            <a class="navbar-brand" href="#"><img src="../static/images/logo.png" height="40" alt="TutorSearch"/></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <h4>Добро пожаловать, ${username}!</h4>
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item"><a class="nav-link" href="client_lessons"><i class="fas fa-list mr-1"></i> Список занятий</a></li>
                    <li class="nav-item">
                        <a class="nav-link profile-link position-relative" href="/clientProfileDisplay">
                            <i class="fas fa-user mr-1"></i>
                            Профиль
                            <span class="profile-warning"></span>
                        </a>
                    </li>
                    <li class="nav-item"><a class="nav-link" href="logout"><i class="fas fa-sign-out-alt mr-1"></i> Выйти</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <main class="main-content">
        <div class="container">


            <div class="tutorial-video mb-5">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="fas fa-play-circle mr-2"></i>Как подобрать идеального репетитора
                        </h4>
                    </div>
                    <div class="card-body p-0">
                        <div class="video-wrapper mb-4">
                            <div class="video-container">
                            <iframe src="https://www.youtube.com/embed/UZpVSRMpo0E"
                                    title="Как выбрать репетитора - пошаговая инструкция"
                                    frameborder="0"
                                    allowfullscreen>
                            </iframe>
                            </div>
                        </div>
                        <div class="p-3 bg-light">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="badge bg-secondary me-2"><i class="fas fa-clock"></i>11:18</span>
                                    <span class="badge bg-success"><i class="fas fa-eye"></i> Пошаговая инструкция</span>
                                </div>
                                <button class="btn btn-sm btn-outline-primary" onclick="showSteps()">
                                    <i class="fas fa-list-ol mr-1"></i> Текстовая версия
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="search-section">
                <h1><i class="fas fa-search mr-2"></i>Поиск репетиторов</h1>
                <div class="row mb-3">
                    <div class="col-md-4">
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text bg-white border-right-0"><i class="fas fa-user"></i></span>
                            </div>
                            <input type="text" id="searchName" class="form-control border-left-0" placeholder="Поиск по имени">
                        </div>
                    </div>
                    <div class="col-md-3" style="min-width: 200px;">
                        <div class="input-group">
                            <div class="input-group-prepend">
    <span class="input-group-text bg-white border-right-0">
      <i class="fas fa-book"></i>
    </span>
                            </div>
                            <select id="filterSubject" class="form-control border-left-0" required>
                                <option value="" selected>Выберите предмет</option>
                                <option value="Математика">Математика</option>
                                <option value="Физика">Физика</option>
                                <option value="Русский">Русский</option>
                                <option value="Английский">Английский</option>
                                <option value="Музыка">Музыка</option>
                                <option value="Менеджмент">Менеджмент</option>
                                <option value="История">История</option>
                                <option value="Биология">Биология</option>
                                <option value="Химия">Химия</option>
                                <option value="Алгебра">Алгебра</option>
                                <option value="Бухгалтерия">Бухгалтерия</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text bg-white border-right-0"><i class="fas fa-coins"></i></span>
                            </div>
                            <select id="filterRate" class="form-control border-left-0">
                                <option value="">Любая ставка</option>
                                <option value="low">8-38</option>
                                <option value="medium">38-80</option>
                                <option value="high">80+</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-">
                        <button class="btn btn-primary btn-block" style="margin-top: 0px;" onclick="filterTutors()">
                            <i class="fas fa-filter mr-1"></i> Применить
                        </button>
                    </div>
                </div>
            </div>


            <div id="stepsText" class="card mb-5 d-none">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Текстовая инструкция по подбору репетитора</h5>
                </div>
                <div class="card-body">
                    <ol class="list-group list-group-numbered">
                        <li class="list-group-item d-flex justify-content-between align-items-start">
                            <div class="ms-2 me-auto">
                                <div class="fw-bold">Определите цель</div>
                                Подготовка к экзамену, повышение успеваемости или изучение нового предмета
                            </div>
                            <span class="badge bg-primary rounded-pill">1</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-start">
                            <div class="ms-2 me-auto">
                                <div class="fw-bold">Выберите предмет</div>
                                Используйте фильтр по предметам для быстрого поиска
                            </div>
                            <span class="badge bg-primary rounded-pill">2</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-start">
                            <div class="ms-2 me-auto">
                                <div class="fw-bold">Просмотрите информацию и отзывы о преподавателе</div>
                                Обратите внимание на стиль преподавания каждого репетитора
                            </div>
                            <span class="badge bg-primary rounded-pill">3</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-start">
                            <div class="ms-2 me-auto">
                                <div class="fw-bold">Свяжитесь с понравившимся репетитором</div>
                                Нажмите "Просмотреть профиль" для просмотра полного профиля
                            </div>
                            <span class="badge bg-primary rounded-pill">4</span>
                        </li>
                    </ol>
                </div>
            </div>


            <div class="row" id="tutorList">
                <c:forEach var="tutor" items="${tutors}">
                    <div class="col-md-3 mb-4 tutor-card" data-name="${tutor.user.name}" data-subject="${tutor.subject}" data-rate="${tutor.rate}">
                        <div class="card h-100">
                            <div class="card-body text-center d-flex flex-column">
                                <img src="${not empty photos[tutor.id] ? photos[tutor.id] : contextPath.concat('/static/images/default-avatar.jpg')}"
                                     class="rounded-circle mx-auto" width="100" height="100" alt="Фото"
                                     onerror="this.src='/static/images/default-avatar.jpg'">
                                <h4 class="mt-3">${tutor.user.name}</h4>
                                <p class="text-muted"><i class="fas fa-book-open mr-2"></i>${tutor.subject}</p>
                                <p class="text-muted"><i class="fas fa-dollar-sign mr-2"></i>${tutor.rate}/час</p>
                                <a href="/getTutorInfo/${tutor.id}" class="btn btn-primary mt-auto">Просмотреть профиль</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
    <footer>
        <div class="container">
            <p>&copy; 2025 Tutor Searching Platform. Все права защищены</p>
        </div>
    </footer>
</div>
<script>
    function showSteps() {
        const stepsElement = document.getElementById('stepsText');
        stepsElement.classList.toggle('d-none');

        if (!stepsElement.classList.contains('d-none')) {
            stepsElement.scrollIntoView({ behavior: 'smooth' });
        }
    }
    function filterTutors() {
        let searchName = $('#searchName').val().toLowerCase();
        let filterSubject = $('#filterSubject').val();
        let filterRate = $('#filterRate').val();

        $('.tutor-card').each(function () {
            let name = $(this).data('name').toLowerCase();
            let subject = $(this).data('subject');
            let rate = parseFloat($(this).data('rate'));
            let show = true;

            if (searchName && !name.includes(searchName)) show = false;
            if (filterSubject && subject !== filterSubject) show = false;
            if (filterRate) {
                if (filterRate === 'low' && (rate < 8 || rate > 38)) show = false;
                if (filterRate === 'medium' && (rate < 38 || rate > 80)) show = false;
                if (filterRate === 'high' && rate < 80) show = false;
            }

            $(this).toggle(show);
        });
    }

    // Apply filter when Enter is pressed in search field
    $('#searchName').keypress(function(e) {
        if(e.which === 13) {
            filterTutors();
        }
    });

    document.addEventListener("DOMContentLoaded", function() {
        fetch('/check-profile')
            .then(response => {
                if (!response.ok) throw new Error('Network error');
                return response.json();
            })
            .then(data => {
                if (data.isEmpty) {
                    const warningContainer = document.querySelector('.profile-warning');
                    if (warningContainer) {
                        warningContainer.innerHTML = `
                        <span class="blink-annotation position-absolute start-0 bottom-100 mb-1 badge bg-warning text-dark">
                            Заполните профиль
                        </span>
                    `;
                    }
                }
            })
            .catch(error => console.error('Error:', error));
    });

</script>
</body>
</html>