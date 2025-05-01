<%@page import="java.sql.*" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          crossorigin="anonymous">
    <title>Профиль преподавателя | TutorSearch</title>
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
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
            color: rgba(255, 255, 255, 0.85) !important;
            font-weight: 500;
            padding: 8px 15px !important;
            margin: 0 5px;
            border-radius: 5px;
            transition: all 0.3s;
        }

        .nav-link:hover {
            color: white !important;
            background-color: rgba(255, 255, 255, 0.15);
            transform: translateY(-2px);
        }

        /* Profile section */
        .profile-container {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            margin-bottom: 30px;
        }

        .profile-header {
            border-bottom: 1px solid #eee;
            padding-bottom: 20px;
            margin-bottom: 20px;
        }

        .profile-img {
            border: 3px solid var(--accent-color);
            border-radius: 50%;
            object-fit: cover;
            width: 200px;
            height: 200px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .profile-info h2 {
            color: var(--dark-color);
            font-weight: 600;
            margin-bottom: 15px;
        }

        .profile-info p {
            margin-bottom: 10px;
            font-size: 16px;
        }

        .profile-info strong {
            color: var(--secondary-color);
            font-weight: 600;
        }

        /* Schedule table */
        .schedule-table {
            margin-top: 30px;
        }

        .schedule-table .table {
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .schedule-table th {
            background-color: var(--primary-color);
            color: white;
            font-weight: 500;
            border: none;
        }

        .schedule-table td {
            vertical-align: middle;
        }

        .btn-book {
            background-color: var(--success-color);
            border-color: var(--success-color);
            border-radius: 50px;
            padding: 8px 20px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .btn-book:hover {
            background-color: #3a9a33;
            border-color: #3a9a33;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(75, 181, 67, 0.3);
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

            .profile-img {
                width: 150px;
                height: 150px;
                margin-bottom: 20px;
            }
        }

        /* Modal styles */
        .modal-content {
            border-radius: 10px;
            border: none;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
        }

        .modal-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 10px 10px 0 0 !important;
        }

        .modal-title {
            font-weight: 600;
        }

        .booking-details {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-top: 15px;
        }

        .booking-details p {
            margin-bottom: 8px;
        }

        #confirmBookingBtn {
            background-color: var(--success-color);
            border-color: var(--success-color);
            border-radius: 50px;
            padding: 8px 20px;
            font-weight: 500;
        }

        #confirmBookingBtn:hover {
            background-color: #3a9a33;
            border-color: #3a9a33;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(75, 181, 67, 0.3);
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
                    <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-list mr-1"></i> Список
                        занятий</a></li>
                    <li class="nav-item"><a class="nav-link" href="/profileDisplay"><i class="fas fa-user mr-1"></i>
                        Профиль</a></li>
                    <li class="nav-item"><a class="nav-link" href="/logout"><i class="fas fa-sign-out-alt mr-1"></i>
                        Выйти</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <main class="main-content">
        <div class="container">
            <div class="profile-container">
                <div class="profile-header">
                    <div class="row align-items-center">
                        <div class="col-md-3 text-center">
                            <img src="${not empty photo ? photo : '/static/images/default-avatar.jpg'}"
                                 class="profile-img"
                                 alt="Фото преподавателя"
                                 onerror="this.src='/static/images/default-avatar.jpg'">
                        </div>
                        <div class="col-md-8 profile-info">
                            <h2><i class="fas fa-user-tie mr-2"></i>${tutor.user.name}</h2>

                            <p><strong><i class="fas fa-book-open mr-2"></i>Предметы:</strong> ${tutor.subject}</p>

                            <p><strong><i class="fas fa-coins mr-2"></i>Ставка:</strong> ${tutor.rate} $/час</p>

                            <p><strong><i class="fas fa-briefcase mr-2"></i>Опыт
                                преподавания:</strong> ${tutor.experience} год</p>

                            <p><strong><i
                                    class="fas fa-graduation-cap mr-2"></i>Образование:</strong> ${tutor.education}</p>
                        </div>
                    </div>
                </div>

                <div class="schedule-table">
                    <h3><i class="far fa-calendar-alt mr-2"></i>Доступное расписание</h3>
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th><i class="far fa-calendar mr-1"></i> День</th>
                            <th><i class="far fa-clock mr-1"></i> Начало</th>
                            <th><i class="fas fa-clock mr-1"></i> Окончание</th>
                            <th>Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${slots}" var="slot">
                            <c:if test="${slot.available}">
                                <tr>
                                    <td>
                                        <input type="date" class="form-control" name="date"
                                               value="${slot.date}" readonly>
                                    </td>
                                    <td>
                                        <input type="time" class="form-control" name="start_time"
                                               value="${slot.start_time}" readonly>
                                    </td>
                                    <td>
                                        <input type="time" class="form-control" name="end_time"
                                               value="${slot.end_time}" readonly>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-book" data-slot-id="${slot.id}">
                                            <i class="fas fa-calendar-plus mr-1"></i> Записаться
                                        </button>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="rating-reviews mb-4">
                    <h3><i class="fas fa-star mr-2"></i>Рейтинг и отзывы</h3>
                    <div class="d-flex align-items-center mb-2">
                        <div class="rating-stars">
                            <%--
                            <c:forEach begin="1" end="5" var="i">
                                <i class="fas fa-star ${i <= tutor.rating ? 'text-warning' : 'text-secondary'}"></i>
                            </c:forEach>
                            <span class="ml-2">${tutor.rating} (${reviewCount} отзывов)</span>
                            --%>
                        </div>
                    </div>

                    <div class="reviews-container">
                        <%--
                        <c:forEach items="${reviews}" var="review">
                            <div class="review-item p-3 mb-3 bg-light rounded">
                                <div class="d-flex justify-content-between">
                                    <strong>${review.studentName}</strong>
                                    <small class="text-muted">${review.date}</small>
                                </div>
                                <div class="rating-stars mb-2">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star ${i <= review.rating ? 'text-warning' : 'text-secondary'}"></i>
                                    </c:forEach>
                                </div>
                                <p>${review.comment}</p>
                            </div>
                        </c:forEach>
                        --%>
                        <button class="btn btn-outline-primary">Показать все отзывы</button>
                    </div>
                </div>
                <div class="stats-achievements mb-4">
                    <h3><i class="fas fa-chart-line mr-2"></i>Статистика</h3>
                    <div class="row text-center">
                        <div class="col-md-3">
                            <div class="stat-card p-3 bg-primary text-white rounded">
                               <%-- <h4>${tutor.lessonsGiven}+</h4> --%>
                                <p>Проведено занятий</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card p-3 bg-success text-white rounded">
                                <%--<h4>${tutor.studentsCount}+</h4> --%>
                                <p>Учеников</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card p-3 bg-info text-white rounded">
                                <%--
                                <h4>${tutor.responseRate}%</h4>
                                --%>
                                <p>Скорость ответа</p>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card p-3 bg-warning text-dark rounded">
                                <%--
                                <h4>${tutor.attendanceRate}%</h4> --%>
                                <p>Посещаемость</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="modal fade" id="confirmBookingModal" tabindex="-1" role="dialog"
             aria-labelledby="confirmBookingModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmBookingModalLabel">Подтверждение записи</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>Вы действительно хотите записаться на занятие к преподавателю
                            <strong>${tutor.user.name}</strong>?</p>
                        <div class="booking-details">
                            <p><strong>Дата:</strong> <span id="bookingDate"></span></p>
                            <p><strong>Время:</strong> <span id="bookingTime"></span></p>
                            <p><strong>Предмет:</strong> ${tutor.subject}</p>
                            <p><strong>Ставка:</strong> ${tutor.rate} $/час</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                        <button type="button" class="btn btn-primary" id="confirmBookingBtn">Подтвердить запись</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Модальное окно успешной брони -->
        <div class="modal fade" id="successBookingModal" tabindex="-1" role="dialog"
             aria-labelledby="successBookingModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successBookingModalLabel">Запись подтверждена</h5>
                        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body text-center">
                        <i class="fas fa-check-circle fa-5x text-success mb-4"></i>
                        <h4>Вы успешно записаны на занятие!</h4>
                        <div class="booking-details mt-4">
                            <p><strong>Преподаватель:</strong> ${tutor.user.name}</p>
                            <p><strong>Дата и время:</strong> <span id="successBookingDate"></span></p>
                            <p><strong>Предмет:</strong> ${tutor.subject}</p>
                            <p>Мы отправили детали записи на вашу электронную почту.</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Отлично!</button>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <footer>
        <div class="container">
            <p>&copy; 2025 Tutor Searching Platform. Все права защищены</p>
        </div>
    </footer>
</div>

<script src="https://code.jquery.com/jquery-3.4.1.min.js" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
<script>
    $(document).ready(function () {
        // Обработчик клика по кнопке "Записаться"
        $('.btn.btn-book').click(function (e) {
            e.preventDefault();

            var row = $(this).closest('tr');
            var date = row.find('input[name="date"]').val();
            var startTime = row.find('input[name="start_time"]').val();
            var endTime = row.find('input[name="end_time"]').val();
            var slotId = $(this).data('slotId');

            try {
                // Форматируем дату для отображения
                var dateObj = new Date(date);
                var formattedDate = dateObj.toLocaleDateString('ru-RU', {
                    day: 'numeric',
                    month: 'long',
                    year: 'numeric',
                    weekday: 'long'
                }) || date; // fallback если локализация не сработала

                // Устанавливаем данные в модальное окно
                $('#bookingDate').text(formattedDate);
                $('#bookingTime').text(startTime + ' - ' + endTime);

                // Сохраняем данные для успешного окна
                $('#successBookingDate').text(formattedDate + ', ' + startTime + ' - ' + endTime);

                // Сохраняем ID слота для подтверждения
                $('#confirmBookingBtn').data('slotId', slotId);

                // Показываем модальное окно подтверждения
                $('#confirmBookingModal').modal('show');
            } catch (e) {
                console.error('Error formatting date:', e);
                alert('Произошла ошибка при обработке даты');
            }
        });

        $('#confirmBookingBtn').click(function () {
            var slotId = $(this).data('slot-id');
            var $btn = $(this);

            $btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Обработка...');

            $.ajax({
                url: '/signUp',
                type: 'POST',
                data: {
                    slotId: slotId,
                    _csrf: "${_csrf.token}"
                },
                success: function (response) {
                    $('#confirmBookingModal').modal('hide');
                    if (response.redirect) {
                        window.location.href = response.redirect;
                    } else {
                        $('#successBookingModal').modal('show');
                    }
                },
                error: function (xhr) {
                    var errorMsg = 'Произошла ошибка при записи';
                    if (xhr.status === 401) {
                        window.location.href = '/login';
                        return;
                    }
                    if (xhr.responseJSON && xhr.responseJSON.error) {
                        errorMsg = xhr.responseJSON.error;
                    }
                    alert(errorMsg);
                },
                complete: function () {
                    $btn.prop('disabled', false).text('Подтвердить запись');
                }
            });
        });
    });
</script>
</body>
</html>
