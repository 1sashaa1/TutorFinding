<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Админ-панель: Аналитика</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/chart.js@3.7.1/dist/chart.min.css">
    <style>
        .card-analytics {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: all 0.3s;
            margin-bottom: 20px;
        }
        .card-analytics:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .stat-number {
            font-size: 2.5rem;
            font-weight: bold;
        }
        .canvas-wrapper {
            position: relative;
            height: 300px;
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container-fluid mt-4">
    <h2 class="mb-4"><i class="fas fa-chart-line"></i> Аналитическая панель</h2>

    <!-- Фильтры периода -->
    <div class="card mb-4">
        <div class="card-body">
            <form class="row g-3">
                <div class="col-md-3">
                    <label class="form-label">Период</label>
                    <select class="form-select" name="period">
                        <option value="day" ${param.period == 'day' ? 'selected' : ''}>По дням</option>
                        <option value="week" ${param.period == 'week' ? 'selected' : ''}>По неделям</option>
                        <option value="month" ${param.period == 'month' ? 'selected' : ''}>По месяцам</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Дата начала</label>
                    <input type="date" class="form-control" name="startDate" value="${param.startDate}">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Дата окончания</label>
                    <input type="date" class="form-control" name="endDate" value="${param.endDate}">
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary">Применить</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Основные метрики -->
    <div class="row">
        <div class="col-md-3">
            <div class="card card-analytics text-white bg-primary">
                <div class="card-body">
                    <h5 class="card-title">Всего пользователей</h5>
                    <p class="stat-number">${totalUsers}</p>
                    <p class="card-text">
                        <i class="fas fa-arrow-up"></i>
                        <fmt:formatNumber value="${userGrowthPercent}" pattern="#.##"/>% за период
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card card-analytics text-white bg-success">
                <div class="card-body">
                    <h5 class="card-title">Активных репетиторов</h5>
                    <p class="stat-number">${activeTutors}</p>
                    <p class="card-text">${tutorsWithReviews} с отзывами</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card card-analytics text-white bg-info">
                <div class="card-body">
                    <h5 class="card-title">Средний рейтинг</h5>
                    <p class="stat-number"><fmt:formatNumber value="${avgRating}" pattern="#.##"/>/5</p>
                    <p class="card-text">${totalReviews} отзывов</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card card-analytics text-white bg-warning">
                <div class="card-body">
                    <h5 class="card-title">Среднее время на сайте</h5>
                    <p class="stat-number"><fmt:formatNumber value="${avgSessionTime}" pattern="#.##"/> мин</p>
                    <p class="card-text">${avgLogins} входов/неделю</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Графики -->
    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card card-analytics">
                <div class="card-header">
                    <h5>Регистрации пользователей</h5>
                </div>
                <div class="card-body">
                    <div class="canvas-wrapper">
                        <canvas id="registrationsChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card card-analytics">
                <div class="card-header">
                    <h5>Популярные предметы</h5>
                </div>
                <div class="card-body">
                    <div class="canvas-wrapper">
                        <canvas id="subjectsChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card card-analytics">
                <div class="card-header">
                    <h5>Активность пользователей</h5>
                </div>
                <div class="card-body">
                    <div class="canvas-wrapper">
                        <canvas id="activityChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card card-analytics">
                <div class="card-header">
                    <h5>Распределение рейтингов</h5>
                </div>
                <div class="card-body">
                    <div class="canvas-wrapper">
                        <canvas id="ratingsChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Таблицы с детализацией -->
    <div class="card mt-4">
        <div class="card-header">
            <h5>Детальная статистика по репетиторам</h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Репетитор</th>
                        <th>Предмет</th>
                        <th>Кол-во занятий</th>
                        <th>Средний рейтинг</th>
                        <th>Повторные ученики</th>
                        <th>Доход</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${tutorStats}" var="tutor">
                        <tr>
                            <td>${tutor.name}</td>
                            <td>${tutor.subject}</td>
                            <td>${tutor.lessonsCount}</td>
                            <td><fmt:formatNumber value="${tutor.avgRating}" pattern="#.##"/></td>
                            <td>${tutor.repeatStudents}%</td>
                            <td><fmt:formatNumber value="${tutor.income}" type="currency"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Скрипты -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.7.1/dist/chart.min.js"></script>
<script>
    // График регистраций
    new Chart(document.getElementById('registrationsChart'), {
        type: 'line',
        data: {
            labels: [<c:forEach items="${registrationData.labels}" var="label">"${label}",</c:forEach>],
            datasets: [
                {
                    label: 'Ученики',
                    data: [<c:forEach items="${registrationData.students}" var="val">${val},</c:forEach>],
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.1)',
                    tension: 0.1
                },
                {
                    label: 'Репетиторы',
                    data: [<c:forEach items="${registrationData.tutors}" var="val">${val},</c:forEach>],
                    borderColor: 'rgb(54, 162, 235)',
                    backgroundColor: 'rgba(54, 162, 235, 0.1)',
                    tension: 0.1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'top' }
            }
        }
    });

    // График популярных предметов
    new Chart(document.getElementById('subjectsChart'), {
        type: 'bar',
        data: {
            labels: [<c:forEach items="${popularSubjects.labels}" var="label">"${label}",</c:forEach>],
            datasets: [{
                label: 'Количество занятий',
                data: [<c:forEach items="${popularSubjects.values}" var="val">${val},</c:forEach>],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            }
        }
    });

    // График активности
    new Chart(document.getElementById('activityChart'), {
        type: 'radar',
        data: {
            labels: ['Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота', 'Воскресенье'],
            datasets: [{
                label: 'Активность (часы)',
                data: [<c:forEach items="${activityData}" var="val">${val},</c:forEach>],
                backgroundColor: 'rgba(255, 159, 64, 0.2)',
                borderColor: 'rgba(255, 159, 64, 1)',
                pointBackgroundColor: 'rgba(255, 159, 64, 1)'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // График рейтингов
    new Chart(document.getElementById('ratingsChart'), {
        type: 'pie',
        data: {
            labels: ['5 звезд', '4 звезды', '3 звезды', '2 звезды', '1 звезда'],
            datasets: [{
                data: [${ratingData[0]}, ${ratingData[1]}, ${ratingData[2]}, ${ratingData[3]}, ${ratingData[4]}],
                backgroundColor: [
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(255, 159, 64, 0.7)',
                    'rgba(255, 99, 132, 0.7)'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'right' }
            }
        }
    });
</script>
</body>
</html>