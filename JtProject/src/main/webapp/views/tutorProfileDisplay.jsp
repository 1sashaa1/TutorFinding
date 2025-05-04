<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Профиль преподавателя</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <script>
        function enableEditing() {
            document.querySelector("select[name='subjectId']").disabled = false;
            document.querySelector("select[name='subjectId']").style.pointerEvents = 'auto';
            document.querySelector("select[name='subjectId']").style.backgroundColor = '';

            document.querySelector("select[name='preferredFormat']").disabled = false;
            document.querySelector("select[name='preferredFormat']").style.pointerEvents = 'auto';
            document.querySelector("select[name='preferredFormat']").style.backgroundColor = '';


            document.querySelectorAll(".editable").forEach(input => input.removeAttribute("readonly"));
            document.getElementById("saveBtn").style.display = "inline-block";
            document.getElementById("editBtn").style.display = "none";
        }
    </script>
</head>
<body>

<div class="container">
    <h2 class="mt-4">Профиль преподавателя</h2>

    <div class="card">
        <div class="card-body text-center">
            <img src="${photo}" alt="Фото преподавателя" class="rounded-circle" width="150" height="150">
            <form action="uploadPhoto" method="post" enctype="multipart/form-data" class="mt-3">
                <input class="btn btn-secondary mt-2" type="file" name="photo" required>
                <button type="submit" class="btn btn-success mt-2">Загрузить фото</button>
            </form>

            <h3 class="mt-3">${username}</h3>

            <form action="/updateProfile" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="tutorId" value="${tutor.id}">

                <div class="form-group">
                    <label><strong>Предмет:</strong></label>
                    <select name="subjectId" disabled class="form-control editable">
                        <option value="" readonly>Выберите предмет</option>
                        <c:forEach items="${allSubjects}" var="subject" >
                            <option value="${subject.id}"
                                ${not empty tutor.subject && tutor.subject.id == subject.id ? 'selected' : ''} readonly>
                                    ${subject.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label><strong>Ставка ($/час):</strong></label>
                    <input type="number" name="rate" id="rate" class="form-control editable"
                           value="${tutor.rate}" readonly
                           min="5" max="100" step="5"
                           onchange="validateRate(this)">
                    <div id="rateError" class="invalid-feedback" style="display: none;">
                        Ставка должна быть от $5 до $100 с шагом $5
                    </div>
                </div>

                <div class="form-group">
                    <label><strong>Опыт (лет):</strong></label>
                    <input type="number" name="experience" id="experience" class="form-control editable"
                           value="${tutor.experience}" readonly
                           min="0" max="60"
                           onchange="validateExperience(this)">
                    <div id="experienceError" class="invalid-feedback" style="display: none;">
                        Опыт должен быть от 0 до 60 лет
                    </div>
                </div>

                <div class="form-group">
                    <label><strong>Образование:</strong></label>
                    <input type="text" name="education" id="education" class="form-control editable"
                           value="${tutor.education}" readonly
                           maxlength="255"
                           onchange="validateEducation(this)">
                    <div id="educationError" class="invalid-feedback" style="display: none;">
                        Максимальная длина - 255 символов
                    </div>
                </div>

                <div class="form-group">
                    <label><strong>YouTube Channel ID:</strong></label>
                    <input type="text" name="youtubeChannelId" id="youtubeChannelId"
                           class="form-control editable" value="${tutor.youtubeChannelId}" readonly
                           title="Введите корректный YouTube Channel ID (24 символа, может содержать буквы, цифры, '-' и '_')"
                           onchange="validateYouTubeId(this)">
                    <small class="form-text text-muted">Пример: UC_x5XG1OV2P6uZZ5FSM9Ttw</small>
                    <div id="youtubeIdError" class="invalid-feedback" style="display: none;">
                        Неверный формат YouTube Channel ID
                    </div>
                </div>

                <div class="form-group">
                    <label><strong>Доступное время:</strong></label>
                    <table class="table" id="scheduleTable">
                        <thead>
                        <tr>
                            <th>Дата</th>
                            <th>Время начала</th>
                            <th>Время окончания</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${availableSlots}" var="slot">
                            <tr>
                                <td>
                                    <input type="date" class="form-control" name="date"
                                           value="${slot.date}" ${slot.available ? '' : 'readonly'}>
                                </td>
                                <td>
                                    <input type="time" class="form-control start-time" name="start_time"
                                           value="${slot.start_time}" ${slot.available ? '' : 'readonly'}
                                           onchange="validateTimeSlot(this)">
                                </td>
                                <td>
                                    <input type="time" class="form-control end-time" name="end_time"
                                           value="${slot.end_time}" ${slot.available ? '' : 'readonly'}
                                           onchange="validateTimeSlot(this)">
                                </td>
                                <td>
                                    <c:if test="${slot.available}">
                                        <button type="button" class="btn btn-danger"
                                                onclick="deleteRow(this)">Удалить</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <button type="button" class="btn btn-primary" onclick="addScheduleRow()">Добавить слот</button>
                    <div id="timeSlotError" class="invalid-feedback" style="display: none; color: red;">
                        Время окончания должно быть минимум на 1 час позже времени начала
                    </div>
                </div>

                <input type="hidden" name="availableTimes" id="availableTimes">

                <div class="form-group">
                    <label><strong>Формат занятий:</strong></label>
                    <select name="preferredFormat" disabled class="form-control editable" readonly>
                        <option value="ONLINE" ${tutor.preferredFormat == 'ONLINE' ? 'selected' : ''}>Онлайн</option>
                        <option value="OFFLINE" ${tutor.preferredFormat == 'OFFLINE' ? 'selected' : ''}>Оффлайн</option>
                        <option value="BOTH" ${tutor.preferredFormat == 'BOTH' ? 'selected' : ''}>Онлайн и оффлайн</option>
                    </select>
                </div>

                <button type="button" id="editBtn" class="btn btn-warning mt-2" onclick="enableEditing()">Редактировать</button>
                <button type="submit" id="saveBtn" class="btn btn-success mt-2" style="display: none;">Сохранить</button>
            </form>
        </div>
    </div>

    <a href="/tutor_inform" class="btn btn-primary mt-3">На главную</a>
</div>

</body>
</html>
    <script>
        function validateYouTubeId(input) {
            const youtubeId = input.value.trim();
            const errorElement = document.getElementById('youtubeIdError');

            // Регулярное выражение для проверки YouTube ID (24 символа, буквы, цифры, - и _)
            const youtubeIdRegex = /^[A-Za-z0-9_-]{24}$/;

            if (youtubeId === '') {
                // Поле пустое - убираем ошибку
                input.classList.remove('is-invalid');
                errorElement.style.display = 'none';
                return true;
            }

            if (!youtubeIdRegex.test(youtubeId)) {
                input.classList.add('is-invalid');
                errorElement.style.display = 'block';
                return false;
            } else {
                input.classList.remove('is-invalid');
                errorElement.style.display = 'none';
                return true;
            }
        }
        function validateRate(input) {
            const rate = parseFloat(input.value);
            const errorElement = document.getElementById('rateError');

            if (isNaN(rate) || rate < 5 || rate > 1000 || rate % 5 !== 0) {
                input.classList.add('is-invalid');
                errorElement.style.display = 'block';
                return false;
            } else {
                input.classList.remove('is-invalid');
                errorElement.style.display = 'none';
                return true;
            }
        }

        function validateExperience(input) {
            const experience = parseInt(input.value);
            const errorElement = document.getElementById('experienceError');

            if (isNaN(experience) || experience < 0 || experience > 60) {
                input.classList.add('is-invalid');
                errorElement.style.display = 'block';
                return false;
            } else {
                input.classList.remove('is-invalid');
                errorElement.style.display = 'none';
                return true;
            }
        }

        function validateEducation(input) {
            const errorElement = document.getElementById('educationError');

            if (input.value.length > 255) {
                input.classList.add('is-invalid');
                errorElement.style.display = 'block';
                return false;
            } else {
                input.classList.remove('is-invalid');
                errorElement.style.display = 'none';
                return true;
            }
        }

        function validateTimeSlot(input) {
            const row = input.closest('tr');
            const startTime = row.querySelector('.start-time').value;
            const endTime = row.querySelector('.end-time').value;
            const errorElement = document.getElementById('timeSlotError');

            if (startTime && endTime) {
                // Преобразуем "HH:MM" в timestamp (количество миллисекунд)
                const start = convertTimeToMs(startTime);
                const end = convertTimeToMs(endTime);
                const diffHours = (end - start) / (1000 * 60 * 60);

                if (diffHours !==1) {  // Проверяем, что разница 1 час
                    errorElement.style.display = 'block';
                    errorElement.textContent = "Время окончания должно быть на 1 час позже времени начала";
                    row.querySelector('.end-time').classList.add('is-invalid');
                    return false;
                } else {
                    errorElement.style.display = 'none';
                    row.querySelector('.end-time').classList.remove('is-invalid');
                    return true;
                }
            }
            return true;
        }

        // Вспомогательная функция для преобразования "HH:MM" в миллисекунды
        function convertTimeToMs(timeStr) {
            const [hours, minutes] = timeStr.split(':').map(Number);
            return hours * 60 * 60 * 1000 + minutes * 60 * 1000;
        }

        // Общая валидация формы
        function validateForm() {
            let isValid = true;

            // Проверяем все поля
            isValid = validateRate(document.getElementById('rate')) && isValid;
            isValid = validateExperience(document.getElementById('experience')) && isValid;
            isValid = validateEducation(document.getElementById('education')) && isValid;
            isValid = validateYouTubeId(document.getElementById('youtubeChannelId')) && isValid;

            // Проверяем временные слоты
            document.querySelectorAll('.start-time').forEach(input => {
                isValid = validateTimeSlot(input) && isValid;
            });

            if (!isValid) {
                alert('Пожалуйста, исправьте ошибки в форме перед отправкой.');
            }
            collectScheduleData()
            return isValid;
        }


        function addScheduleRow() {
            const table = document.getElementById('scheduleTable').getElementsByTagName('tbody')[0];
            const row = table.insertRow();
            row.innerHTML = `
        <td><input type="date" class="form-control" name="date" required></td>
        <td><input type="time" class="form-control" name="start_time" required></td>
        <td><input type="time" class="form-control" name="end_time" required></td>
        <td><button type="button" class="btn btn-danger" onclick="deleteRow(this)">Удалить</button></td>
    `;
        }

        function deleteRow(btn) {
            const row = btn.parentNode.parentNode;
            row.parentNode.removeChild(row);
        }

        function collectScheduleData() {
            const rows = document.querySelectorAll('#scheduleTable tbody tr');
            const schedule = [];
            rows.forEach(row => {
                const date = row.querySelector('input[name="date"]').value;
                const start = row.querySelector('input[name="start_time"]').value;
                const end = row.querySelector('input[name="end_time"]').value;
                if (date && start && end) {
                    schedule.push({
                        date: date,
                        start_time: start,
                        end_time: end
                    });
                }
            });
            document.getElementById('availableTimes').value = JSON.stringify(schedule);
        }
    </script>

