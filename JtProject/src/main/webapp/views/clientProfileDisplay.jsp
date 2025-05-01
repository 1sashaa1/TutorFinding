<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Заполнение профиля клиента</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
        }
        .required-field::after {
            content: " *";
            color: red;
        }
        .form-section {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
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

    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light">
    <div class="container" style=" margin-left: 300px;">
        <a class="navbar-brand" href="#"><img src="../static/images/logo.png" height="40" alt="TutorSearch"/></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <h4>Добро пожаловать, ${username}!</h4>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a class="nav-link" href="client_lessons"><i class="fas fa-list mr-1"></i> Список занятий</a></li>
                <li class="nav-item">
                    <a class="nav-link profile-link position-relative" href="/">
                        <i class="fas fa-user mr-1"></i>
                        На главную
                        <span class="profile-warning"></span>
                    </a>
                </li>
                <li class="nav-item"><a class="nav-link" href="logout"><i class="fas fa-sign-out-alt mr-1"></i> Выйти</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <h2 class="text-center mb-4">Заполните ваш профиль</h2>

            <form action="save-client-profile" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="client_id" value="${client.id}">

                <!-- Основная информация -->
                <div class="form-section">
                    <h4 class="mb-3">Основная информация</h4>

                    <div class="row mb-3">
                        <label for="age" class="col-sm-3 col-form-label required-field">Возраст</label>
                        <div class="col-sm-9">
                            <input type="number" class="form-control" id="age" name="age"
                                   value="${client.age}" min="1" max="120" required>
                            <div class="invalid-feedback">
                                Пожалуйста, укажите ваш возраст
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label required-field">Уровень</label>
                        <div class="col-sm-9">
                            <select class="form-select" name="level" required>
                                <option value="">Выберите уровень</option>
                                <option value="BEGINNER" ${client.level == 'BEGINNER' ? 'selected' : ''}>Начинающий</option>
                                <option value="INTERMEDIATE" ${client.level == 'INTERMEDIATE' ? 'selected' : ''}>Средний</option>
                                <option value="ADVANCED" ${client.level == 'ADVANCED' ? 'selected' : ''}>Продвинутый</option>
                            </select>
                            <div class="invalid-feedback">
                                Пожалуйста, выберите ваш уровень
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Предпочтения -->
                <div class="form-section">
                    <h4 class="mb-3">Предпочтения</h4>

                    <div class="row mb-3">
                        <label for="subject" class="col-sm-3 col-form-label required-field">Предмет</label>
                        <div class="col-sm-9">
                            <select class="form-select" id="subject" name="subjectId" required>
                                <option value="">Выберите предмет</option>
                                <c:forEach items="${allSubjects}" var="subject">
                                    <option value="${subject.id}"
                                        ${client.subject != null && client.subject.id == subject.id ? 'selected' : ''}>
                                            ${subject.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="invalid-feedback">
                                Пожалуйста, выберите предмет
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label required-field">Формат занятий</label>
                        <div class="col-sm-9">
                            <select class="form-select" name="preferredFormat" required>
                                <option value="">Выберите формат</option>
                                <option value="ONLINE" ${client.preferred_format == 'ONLINE' ? 'selected' : ''}>Онлайн</option>
                                <option value="OFFLINE" ${client.preferred_format == 'OFFLINE' ? 'selected' : ''}>Оффлайн</option>
                                <option value="HYBRID" ${client.preferred_format == 'HYBRID' ? 'selected' : ''}>Оба формата</option>
                            </select>
                            <div class="invalid-feedback">
                                Пожалуйста, выберите предпочитаемый формат
                            </div>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label for="budget" class="col-sm-3 col-form-label">Бюджет</label>
                        <div class="col-sm-9">
                            <div class="input-group">
                                <input type="number" class="form-control" id="budget" name="budget"
                                       value="${client.budget}" min="0" step="0.01">
                                <span class="input-group-text">$</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                    <button type="submit" class="btn btn-primary btn-lg">Сохранить профиль</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Валидация формы
    (function() {
        'use strict'

        const forms = document.querySelectorAll('.needs-validation')

        Array.from(forms).forEach(function(form) {
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
    })()
</script>
</body>
</html>