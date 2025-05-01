<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Мои занятия</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .lesson-card {
            transition: all 0.3s ease;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .lesson-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .subject-badge {
            font-size: 0.9rem;
            padding: 5px 10px;
            border-radius: 20px;
        }

        .teacher-avatar {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 50%;
            border: 3px solid #fff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .filter-card {
            max-height: 0;
            overflow: hidden;
            transition: max-height 0.4s ease;
            padding: 0;
            margin: 0;
            border: none;
        }
        .filter-card.show {
            max-height: 500px;
            padding: 1rem;
            margin-bottom: 1rem !important;
            border: 1px solid rgba(0,0,0,.125);
        }
        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 0.5rem 1rem;
            margin: 0.25rem;
            border-radius: 50px;
            font-size: 0.85rem;
            font-weight: 500;
            letter-spacing: 0.3px;
            background-color: #f8f9fa; /* Светлый фон */
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .status-indicator {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            margin-right: 0.75rem;
            position: relative;
        }

        /* Анимация только для предстоящих */
        .upcoming .status-indicator {
            background-color: #0d6efd;
            animation: pulse 2s infinite;
        }

        .completed .status-indicator {
            background-color: #198754;
        }


        .cancelled .status-indicator {
            background-color: #dc3545;
        }

        @keyframes pulse {
            0% {
                transform: scale(0.95);
                box-shadow: 0 0 0 0 rgba(13, 110, 253, 0.7);
            }
            70% {
                transform: scale(1.1);
                box-shadow: 0 0 0 8px rgba(13, 110, 253, 0);
            }
            100% {
                transform: scale(0.95);
                box-shadow: 0 0 0 0 rgba(13, 110, 253, 0);
            }
        }
        .rating-stars {
            font-size: 24px;
            color: #ffc107;
            cursor: pointer;
        }

        .rating-stars i {
            margin-right: 5px;
            transition: all 0.2s;
        }

        .rating-stars i:hover {
            transform: scale(1.2);
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(5px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .chat-container div {
            animation: fadeIn 0.3s ease-out;
        }

        #chatMessages {
            height: 400px;
            overflow-y: auto;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 5px;
            margin-bottom: 15px;
        }

        .message-input {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .message-input textarea {
            flex-grow: 1;
            resize: none;
            min-height: 80px;
        }

        /* Гарантированные стили для модального окна */
        #chatModal .modal-content {
            overflow: hidden !important;
        }

        #chatMessages * {
            all: initial; /* Сброс наследуемых стилей */
        }

        #chatMessages div {
            display: block !important;
            visibility: visible !important;
        }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="display-5 fw-bold">
            <i class="fas fa-calendar-check me-2"></i>Мои занятия
        </h1>
        <div>
            <button class="btn btn-outline-primary me-2" id="filterButton">
                <i class="fas fa-filter me-1"></i>Фильтр
            </button>
            <button class="btn btn-primary">
                <i class="fas fa-plus me-1"></i>Новая запись
            </button>
            <button class="btn btn-primary">
                <i class="nav-item"><a class="nav-link" href="/"><i class="fas fa-sign-out-alt mr-1"></i>На главную</a></i>
            </button>

        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <h5 class="card-title">Предстоящие</h5>
                    <p class="display-4 mb-0">${upcomingLessonsCount}</p>
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
                    <p class="display-4 mb-0">${cancelledLessonsCount}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="filter-card card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><i class="fas fa-search me-2"></i>Поиск занятий</h5>
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <label class="form-label">Предмет</label>
                        <select class="form-select" id="subjectFilter">
                            <option>Все предметы</option>
                            <c:forEach items="${allSubjects}" var="subject">
                                <option>${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Дата</label>
                        <input type="date" class="form-control" id="dateFilter">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Статус</label>
                        <select class="form-select" id="statusFilter">
                            <option>Все статусы</option>
                            <option>Предстоящие</option>
                            <option>Завершённые</option>
                            <option>Отменённые</option>
                        </select>
                    </div>
                    <button onclick="filterLessons()" class="btn btn-primary w-100">Применить</button>
                </div>
            </div>
        </div>

        <div class="col-md-8" id="lessonsContainer">
            <c:choose>
                <c:when test="${not empty lessons}">
                    <c:forEach items="${lessons}" var="lesson" varStatus="status">
                        <div class="card lesson-card ${lesson.status == 'SCHEDULED' ? 'upcoming' : lesson.status == 'COMPLETED' ? 'completed' : 'cancelled'} mb-3" data-subject="${lesson.subject.name}"
                             data-date="${lesson.scheduleSlot.date}"
                             data-status="${lesson.status}">
                            <div class="card-body">
                        <span class="status-badge ${lesson.status == 'SCHEDULED' ? 'upcoming' : lesson.status == 'COMPLETED' ? 'completed' : 'cancelled'}">
                            <span class="status-indicator"></span>
                            <c:choose>
                                <c:when test="${lesson.status == 'SCHEDULED'}">Предстоящее</c:when>
                                <c:when test="${lesson.status == 'COMPLETED'}">Завершено</c:when>
                                <c:otherwise>Отменено</c:otherwise>
                            </c:choose>
                        </span>

                                <div class="row">
                                    <div class="col-md-2 text-center">
                                        <img src="${photos[status.index]}" class="teacher-avatar mb-2" alt="Фото преподавателя">
                                        <h6 class="mb-0" data-teacher-id="${lesson.teacher.user.id}">${lesson.teacher.user.name}</h6>
                                        <small class="text-muted">Преподаватель</small>
                                    </div>

                                    <div class="col-md-6">
                                        <h5 class="card-title">${lesson.subject.name}</h5>
                                        <div class="d-flex flex-wrap gap-2 mb-3">
                                            <span class="badge bg-light text-dark subject-badge">
                                                <i class="fas fa-book me-1"></i>${lesson.subject.name}
                                            </span>
                                            <span class="badge bg-light text-dark subject-badge">
                                         <i class="fas fa-clock me-1"></i>
                                        ${lesson.scheduleSlot.date},
                                        ${lesson.scheduleSlot.startTime} - ${lesson.scheduleSlot.endTime}
                                            </span>
                                            <span class="badge bg-light text-dark subject-badge">
                                                <i class="fas fa-coins me-1"></i>${lesson.teacher.rate} $/час
                                             </span>
                                        </div>

                                        <p class="card-text">
                                            <i class="fas fa-info-circle me-1"></i>
                                            <c:choose>
                                                <c:when test="${lesson.status == 'SCHEDULED'}">
                                                    Ваше занятие запланировано. Подготовьте необходимые материалы.
                                                </c:when>
                                                <c:when test="${lesson.status == 'COMPLETED'}">
                                                    Занятие завершено. Вы можете оставить отзыв.
                                                </c:when>
                                                <c:otherwise>
                                                    Занятие отменено. Вы можете записаться на другое время.
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>

                                    <div class="col-md-4 d-flex flex-column justify-content-between">
                                        <div class="d-grid gap-2">
                                            <c:if test="${lesson.status == 'SCHEDULED'}">
                                                <button class="btn btn-outline-danger btn-sm" data-bs-toggle="modal"
                                                        data-bs-target="#cancelLessonModal${lesson.id}">
                                                    <i class="fas fa-times me-1"></i>Отменить
                                                </button>
                                                <button class="btn btn-outline-secondary btn-sm">
                                                    <i class="fas fa-clock me-1"></i>Перенести
                                                </button>
                                            </c:if>
                                            <c:if test="${lesson.status == 'COMPLETED'}">
                                                <button class="btn btn-outline-success btn-sm"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#reviewModal${lesson.id}">
                                                    <i class="fas fa-star me-1"></i>Оставить отзыв
                                                </button>
                                            </c:if>
                                            <button class="btn btn-outline-primary btn-sm">
                                                <i class="fas fa-comment me-1"></i>Чат
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Модальное окно отмены занятия -->
                        <div class="modal fade" id="cancelLessonModal${lesson.id}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Отмена занятия</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Вы уверены, что хотите отменить занятие по ${lesson.subject.name} с ${lesson.teacher.user.name}?</p>
                                        <p>Дата:  ${lesson.scheduleSlot.date},
                                                ${lesson.scheduleSlot.startTime} - ${lesson.scheduleSlot.endTime}
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                                        <button type="button" class="btn btn-danger">Подтвердить отмену</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Модальное окно отзыва (должно быть в том же цикле, где создаются карточки) -->
                        <div class="modal fade" id="reviewModal${lesson.id}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Оставить отзыв</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form id="reviewForm${lesson.id}">
                                            <input type="hidden" name="lessonId" value="${lesson.id}">
                                            <div class="mb-3">
                                                <label class="form-label">Оценка</label>
                                                <div class="rating-stars">
                                                    <i class="far fa-star" data-rating="1"></i>
                                                    <i class="far fa-star" data-rating="2"></i>
                                                    <i class="far fa-star" data-rating="3"></i>
                                                    <i class="far fa-star" data-rating="4"></i>
                                                    <i class="far fa-star" data-rating="5"></i>
                                                    <input type="hidden" name="rating" id="ratingValue${lesson.id}" value="0">
                                                </div>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Комментарий</label>
                                                <textarea class="form-control" name="comment" rows="3"></textarea>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                                        <button type="button" class="btn btn-primary" onclick="submitReview(${lesson.id})">Отправить отзыв</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="card">7
                        <div class="card-body text-center py-5">
                            <i class="fas fa-calendar-times fa-5x text-muted mb-4"></i>
                            <h3>У вас пока нет запланированных занятий</h3>
                            <p class="text-muted">Найдите преподавателя и запишитесь на первое занятие!</p>
                            <a href="/tutors" class="btn btn-primary mt-3">
                                <i class="fas fa-search me-1"></i>Найти преподавателя
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="modal fade" id="chatModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Чат с преподавателем</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div id="chatMessages" class="chat-messages"></div>
                <div class="message-input">
                    <textarea id="messageInput" placeholder="Введите сообщение..."></textarea>
                    <button id="sendMessageBtn" class="btn btn-primary">Отправить</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Глобальные переменные для текущего чата
    let currentChatId = null;
    let currentTeacherId = null;
    let currentUserId = ${currentUser.id};
    let messageCheckInterval = null;

    // Обработчик открытия чата
    document.querySelectorAll('.btn-outline-primary.btn-sm').forEach(btn => {
        if (btn.innerHTML.includes('fa-comment')) {
            btn.addEventListener('click', async function() {
                const lessonCard = this.closest('.lesson-card');
                const teacherNameElement = lessonCard.querySelector('h6');
                if (!teacherNameElement) {
                    console.error('Элемент с именем преподавателя не найден!');
                    return;
                }

                const teacherId = teacherNameElement.dataset.teacherId;
                console.log('ID преподавателя:', teacherId); // Для отладки

                const response = await fetch(`/api/chat/getChatId/`+ currentUserId +`/`+ teacherId);
                if (!response.ok) throw new Error('Ошибка получения chatId');

                currentChatId = await response.json();
                currentTeacherId = teacherId;

                await loadChatMessages();

                const chatModalEl = document.getElementById('chatModal');
                const chatModal = new bootstrap.Modal(chatModalEl);

                chatModalEl.addEventListener('shown.bs.modal', function() {
                    console.log('Modal fully shown');
                    loadChatMessages();
                });

                chatModal.show();
                setTimeout(() => {
                    console.log('Modal classes:', document.getElementById('chatModal').className);
                    console.log('Display style:', document.getElementById('chatModal').style.display);
                }, 500);
                startMessageChecking();
            });
        }
    });

    async function loadChatMessages() {
        if (!currentChatId) {
            console.error('Chat ID не установлен');
            return;
        }
        try {
            const response = await fetch(`/api/chat/messages/` + currentChatId);
            if (!response.ok) throw new Error('Ошибка загрузки');

            const messages = await response.json();
            console.log(messages)
            if (!Array.isArray(messages)) {
                throw new Error('Некорректный формат сообщений');
            }
            renderMessages(messages);
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось загрузить сообщения');
        }
    }

    function renderMessages(messages) {
        const chatDiv = document.getElementById('chatMessages');
        if (!chatDiv) return;

        // Очистка чата
        chatDiv.innerHTML = '';

        if (!messages?.length) {
            chatDiv.textContent = 'Нет сообщений';
            return;
        }

        messages.forEach(msg => {
            const messageEl = document.createElement('div');

            const messageText = msg.message ? String(msg.message) : '';

            const header = document.createElement('div');
            header.style.display = 'flex';
            header.style.justifyContent = 'space-between';
            header.style.marginBottom = '5px';
            header.style.fontSize = '0.8em';

            const senderSpan = document.createElement('span');
            senderSpan.style.fontWeight = '500';
            senderSpan.textContent = msg.senderId === currentUserId ? 'Вы' : msg.senderName || 'Преподаватель';

            const timeSpan = document.createElement('span');
            timeSpan.style.opacity = '0.7';
            timeSpan.textContent = msg.sentAt ? new Date(msg.sentAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : '';

            header.appendChild(senderSpan);
            header.appendChild(timeSpan);

            const contentDiv = document.createElement('div');
            contentDiv.style.fontSize = '1em';
            contentDiv.style.lineHeight = '1.4';
            contentDiv.textContent = messageText; // Используем textContent

            messageEl.appendChild(header);
            messageEl.appendChild(contentDiv);

            messageEl.style.maxWidth = '80%';
            messageEl.style.padding = '10px 15px';
            messageEl.style.borderRadius = '18px';
            messageEl.style.backgroundColor = msg.senderId === currentUserId ? '#007bff' : '#e9ecef';
            messageEl.style.color = msg.senderId === currentUserId ? 'white' : 'black';
            messageEl.style.alignSelf = msg.senderId === currentUserId ? 'flex-end' : 'flex-start';
            messageEl.style.marginBottom = '10px';

            chatDiv.appendChild(messageEl);
        });

        // Прокрутка вниз
        setTimeout(() => {
            chatDiv.scrollTop = chatDiv.scrollHeight;
        }, 100);
    }


    document.getElementById('sendMessageBtn').addEventListener('click', async () => {
        const textarea = document.getElementById('messageInput');
        const message = textarea.value.trim();

        if (!message) return;

        try {
            const response = await fetch('/api/chat/send', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    chatId: currentChatId,
                    senderId: currentUserId,
                    receiverId: currentTeacherId,
                    message: message
                })
            });

            if (response.ok) {
                textarea.value = '';
                await loadChatMessages();
            }
        } catch (error) {
            console.error('Ошибка отправки:', error);
        }
    });

    // Проверка новых сообщений каждые 3 секунды
    function startMessageChecking() {
        if (messageCheckInterval) clearInterval(messageCheckInterval);
        loadChatMessages().catch(console.error);
        messageCheckInterval = setInterval(loadChatMessages, 3000);
    }

    // Остановка проверки при закрытии модального окна
    document.getElementById('chatModal').addEventListener('hidden.bs.modal', function() {
        if (messageCheckInterval) clearInterval(messageCheckInterval);
    });

    // Отправка по Enter
    document.getElementById('messageInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            document.getElementById('sendMessageBtn').click();
        }
    });

    // Активация всплывающих подсказок
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    document.addEventListener('DOMContentLoaded', function () {
        // Все кнопки будут обрабатываться здесь
        initButtons();
        initModals();
        initFilters();
    });

    function initButtons() {
        document.querySelector('.btn-primary .fa-plus').closest('.btn').addEventListener('click', () => {
            window.location.href = '/'; // Замените на ваш URL
        });
    }

    const filterButton = document.getElementById('filterButton');
    const filterCard = document.querySelector('.filter-card');

    filterButton.addEventListener('click', function() {
        filterCard.classList.toggle('show');

        // Меняем иконку
        const icon = this.querySelector('i');
        if (filterCard.classList.contains('show')) {
            icon.classList.remove('fa-filter');
            icon.classList.add('fa-times');
        } else {
            icon.classList.remove('fa-times');
            icon.classList.add('fa-filter');
        }
    });

    function initModals() {
        document.querySelectorAll('[data-bs-target^="#cancelLessonModal"]').forEach(btn => {
            btn.addEventListener('click', function () {
                const modalId = this.getAttribute('data-bs-target');
                const modalElement = document.querySelector(modalId);
                const modal = new bootstrap.Modal(modalElement);
                modal.show();

                const lessonId = modalId.replace('#cancelLessonModal', '');
                console.log(lessonId)
                const confirmBtn = modalElement.querySelector('.btn-danger');

                // Удаляем старые обработчики, если есть
                const newConfirmBtn = confirmBtn.cloneNode(true);
                confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

                newConfirmBtn.addEventListener('click', () => {
                    cancelLesson(lessonId);
                    modal.hide();
                });
            });
        });
    }

    function cancelLesson(lessonId) {
        fetch('/lessons/' + lessonId + '/cancel', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
        })
            .then(response => {
                if (!response.ok) throw new Error('Ошибка отмены');
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert('Занятие отменено!');
                    window.location.reload(); // Обновляем страницу
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Не удалось отменить занятие');
            });
    }

</script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    const statusMap = {
        "все статусы": "all",
        "предстоящие": "scheduled",
        "завершённые": "completed",
        "отменённые": "cancelled"
    };

    function filterLessons() {
        // Получаем значения фильтров
        let subjectFilter = $('#subjectFilter').val().toLowerCase().trim();
        let dateFilter = $('#dateFilter').val().trim();
        let statusFilterRaw = $('#statusFilter').val().toLowerCase().trim();
        let statusFilter = statusMap[statusFilterRaw] || "all";

        // Определяем, какие фильтры активны
        let isSubjectFilterActive = subjectFilter && subjectFilter !== "все предметы";
        let isDateFilterActive = dateFilter !== "";
        let isStatusFilterActive = statusFilter !== "all";

        console.log("Активные фильтры:", {
            subject: isSubjectFilterActive ? subjectFilter : "не активен",
            date: isDateFilterActive ? dateFilter : "не активна",
            status: isStatusFilterActive ? statusFilter : "не активен"
        });

        let shownCount = 0;
        let hiddenCount = 0;

        $('.lesson-card').each(function() {
            let $card = $(this);
            let cardSubject = ($card.data('subject') || '').toLowerCase();
            let cardDate = $card.data('date') || '';
            let cardStatus = ($card.data('status') || '').toLowerCase();

            // По умолчанию показываем карточку
            let showCard = true;

            // Применяем только активные фильтры
            if (isSubjectFilterActive && !cardSubject.includes(subjectFilter)) {
                showCard = false;
            }

            if (isDateFilterActive && cardDate !== dateFilter) {
                showCard = false;
            }

            if (isStatusFilterActive && cardStatus !== statusFilter) {
                showCard = false;
            }

            $card.toggle(showCard);

            if (showCard) {
                shownCount++;
                console.log("Показана карточка:", {subject: cardSubject, date: cardDate, status: cardStatus});
            } else {
                hiddenCount++;
            }
        });

        console.log('Итоги:', {показано: shownCount, скрыто: hiddenCount});

        if (shownCount === 0) {
            $('#noResultsMessage').show();
        } else {
            $('#noResultsMessage').hide();
        }
    }

    $(document).ready(function() {
        // Добавляем сообщение "Нет результатов", если его нет
        if ($('#noResultsMessage').length === 0) {
            $('#lessonsContainer').append(`
                <div id="noResultsMessage" class="card" style="display: none;">
                    <div class="card-body text-center py-5">
                        <i class="fas fa-calendar-times fa-5x text-muted mb-4"></i>
                        <h3>Занятий не найдено</h3>
                        <p class="text-muted">Попробуйте изменить параметры поиска</p>
                    </div>
                </div>
            `);
        }
    });

    // Инициализация звезд рейтинга
    function initRatingStars() {
        $('.rating-stars i').hover(function() {
            const rating = $(this).data('rating');
            $(this).parent().find('i').each(function() {
                if ($(this).data('rating') <= rating) {
                    $(this).removeClass('far').addClass('fas');
                } else {
                    $(this).removeClass('fas').addClass('far');
                }
            });
        });

        $('.rating-stars i').click(function() {
            const rating = $(this).data('rating');
            $(this).parent().find('input[type="hidden"]').val(rating);
        });
    }


    // Отправка отзыва
    function submitReview(lessonId) {
        const rating = $('#ratingValue' + lessonId).val();
        const comment = $('#reviewForm' + lessonId + ' textarea').val();

        $.ajax({
            url: '/reviews',
            type: 'POST',
            data: {
                lessonId: lessonId,
                rating: rating,
                comment: comment
            },
            success: function(response) {
                alert('Отзыв успешно отправлен!');
                $('#reviewModal' + lessonId).modal('hide');
            },
            error: function(xhr) {
                alert('Ошибка: ' + xhr.responseText);
            }
        });
    }

    // Инициализация при загрузке страницы
    $(document).ready(function() {
        initRatingStars();
    });
</script>

</body>
</html>