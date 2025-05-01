<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мое расписание</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .lesson-card {
            border-left: 4px solid #0d6efd;
            margin-bottom: 15px;
            transition: all 0.3s;
        }
        .lesson-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .status-badge {
            font-size: 0.8rem;
        }
        .avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            object-fit: cover;
        }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="display-5 fw-bold">
            <i class="fas fa-calendar-check me-2"></i>Мое расписание
        </h1>
        <div>
            <a href="/tutor_inform" class="btn btn-outline-secondary">
                <i class="fas fa-home me-1"></i>На главную
            </a>
        </div>
    </div>

    <!-- Статистика -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <h5 class="card-title">Предстоящие</h5>
                    <p class="display-4 mb-0">${scheduledLessonsCount}</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <h5 class="card-title">Завершённые</h5>
                    <p class="display-4 mb-0">${completedLessonsCount}</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-warning text-dark">
                <div class="card-body">
                    <h5 class="card-title">Отменённые</h5>
                    <p class="display-4 mb-0">${canceledLessonsCount}</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Фильтры -->
    <div class="card mb-4">
        <div class="card-header bg-light">
            <h5 class="mb-0"><i class="fas fa-filter me-2"></i>Фильтры</h5>
        </div>
        <div class="card-body">
            <form action="/tutor/schedule" method="get" class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">Статус</label>
                    <select name="status" class="form-select">
                        <option value="">Все статусы</option>
                        <option value="SCHEDULED" ${param.status == 'SCHEDULED' ? 'selected' : ''}>Запланировано</option>
                        <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>Проведено</option>
                        <option value="CANCELED" ${param.status == 'CANCELED' ? 'selected' : ''}>Отменено</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Ученик</label>
                    <select name="studentId" class="form-select">
                        <option value="">Все ученики</option>
                        <c:forEach items="${lessons}" var="lesson">
                            <option value="${lesson.client.id}" ${param.studentId == lesson.client.id ? 'selected' : ''}>
                                    ${lesson.client.user.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Предмет</label>
                    <select name="subjectId" class="form-select">
                        <option value="">Все предметы</option>
                        <c:forEach items="${lessons}" var="lesson">
                            <option value="${lesson.subject.id}" ${param.subjectId == lesson.subject.id ? 'selected' : ''}>
                                    ${lesson.subject.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Дата от</label>
                    <input type="date" name="startDate" class="form-control" value="${param.startDate}">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Дата до</label>
                    <input type="date" name="endDate" class="form-control" value="${param.endDate}">
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Применить</button>
                    <a href="/tutor/schedule" class="btn btn-outline-secondary">Сбросить</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Список занятий -->
    <div class="card">
        <div class="card-header bg-light">
            <h5 class="mb-0"><i class="fas fa-list me-2"></i>Занятия</h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty lessons}">
                    <c:forEach items="${lessons}" var="lesson">
                        <div class="card lesson-card mb-3">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                <span class="badge
                                    ${lesson.status == 'SCHEDULED' ? 'bg-primary' :
                                     lesson.status == 'COMPLETED' ? 'bg-success' : 'bg-warning'}">
                                        ${lesson.status}
                                </span>
                                    <small class="text-muted">
                                            ${formattedDate}
                                    </small>
                                </div>

                                <div class="row mt-3">
                                    <div class="col-md-2 text-center">
                                        <h6>${lesson.client.user.name}</h6>
                                        <small class="text-muted">Ученик</small>
                                    </div>

                                    <div class="col-md-6">
                                        <h5>${lesson.subject.name}</h5>
                                        <div class="mb-3">
                                            <p class="mb-1">
                                                <i class="fas fa-clock me-1"></i>
                                                    ${lesson.scheduleSlot.startTime} - ${lesson.scheduleSlot.endTime}
                                            </p>
                                        </div>
                                    </div>

                                    <div class="col-md-4">
                                        <div class="d-grid gap-2">
                                            <c:if test="${lesson.status == 'SCHEDULED'}">
                                                <button type="button"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#completeModal${lesson.id}"
                                                        class="btn btn-outline-success btn-sm w-100">
                                                    <i class="fas fa-check me-1"></i> Завершить
                                                </button>

                                                <button type="button" class="btn btn-outline-danger btn-sm"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#cancelModal${lesson.id}">
                                                    <i class="fas fa-times me-1"></i> Отменить
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Модальное окно завершения -->
                        <div class="modal fade" id="completeModal${lesson.id}" tabindex="-1" aria-labelledby="completeModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="completeModalLabel">Завершение занятия</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <form action="/tutor/lessons/${lesson.id}/complete" method="post">
                                        <div class="modal-body">
                                            <p>Вы уверены, что хотите завершить занятие с ${lesson.client.user.name}?</p>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                                            <button type="submit" class="btn btn-success">Подтвердить завершение</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Модальное окно отмены -->
                        <div class="modal fade" id="cancelModal${lesson.id}" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="cancelModalLabel">Отмена занятия</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <form action="/tutor/lessons/${lesson.id}/cancel" method="post">
                                        <div class="modal-body">
                                            <p>Вы уверены, что хотите отменить занятие с ${lesson.client.user.name}?</p>
                                            <div class="mb-3">
                                                <label class="form-label">Причина отмены</label>
                                                <textarea name="reason" class="form-control" rows="3" required></textarea>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                                            <button type="submit" class="btn btn-danger">Подтвердить отмену</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5">
                        <i class="fas fa-calendar-times fa-5x text-muted mb-4"></i>
                        <h3>Нет занятий по выбранным критериям</h3>
                        <p class="text-muted">Измените параметры фильтра или создайте новое занятие</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function completeLesson(lessonId) {
        const url = `/tutor/lessons/${lessonId}/complete`;

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            },
            body: JSON.stringify({ id: lessonId })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Просто обновляем страницу
                    location.reload();
                } else {
                    alert('Ошибка: ' + (data.message || 'Не удалось завершить занятие'));
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка сети');
            });
    }
    // Функция для отмены занятия
    function cancelLesson(lessonId) {
        const reason = document.getElementById(`cancelReason${lessonId}`).value;
        if (!reason) {
            alert('Пожалуйста, укажите причину отмены.');
            return;
        }

        const url = `/tutor/lessons/${lessonId}/cancel`;
        const data = {
            reason: reason
        };
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Занятие отменено');
                    const modal = new bootstrap.Modal(document.getElementById(`cancelModal${lessonId}`));
                    modal.hide();
                    setTimeout(() => {
                        window.location.replace(data.redirectUrl);
                    }, 500);
                } else {
                    alert('Ошибка при отмене занятия');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка при отмене занятия');
            });
    }
</script>

</body>
</html>