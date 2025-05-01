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

            <form action="/updateProfile" method="post" onsubmit="collectScheduleData()">
                <input type="hidden" name="tutorId" value="${tutor.id}">

                <div class="form-group">
                    <label><strong>Предмет:</strong></label>
                    <select name="subjectId" disabled class="form-control">
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
                    <input type="number" name="rate" class="form-control editable" value="${tutor.rate}" readonly>
                </div>
                <div class="form-group">
                    <label><strong>Опыт:</strong></label>
                    <input type="text" name="experience" class="form-control editable" value="${tutor.experience}" readonly>
                </div>
                <div class="form-group">
                    <label><strong>Образование:</strong></label>
                    <input type="text" name="education" class="form-control editable" value="${tutor.education}" readonly>
                </div>
                <div class="form-group">
                    <label><strong>Доступное время:</strong></label>
                    <table class="table" id="scheduleTable">
                        <thead>
                        <tr>
                            <th>Дата</th>
                            <th>Время начала</th>
                            <th>Время окончания</th>
                            <th>Статус</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${slots}" var="slot">
                            <tr>
                                <td>
                                    <input type="date" class="form-control" name="date"
                                           value="${slot.date}" ${slot.available ? '' : 'readonly'}>
                                </td>
                                <td>
                                    <input type="time" class="form-control" name="start_time"
                                           value="${slot.start_time}" ${slot.available ? '' : 'readonly'}>
                                </td>
                                <td>
                                    <input type="time" class="form-control" name="end_time"
                                           value="${slot.end_time}" ${slot.available ? '' : 'readonly'}>
                                </td>
                                <td>
                                        ${slot.available ? 'Свободен' : 'Занят'}
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
                </div>

                <!-- Это скрытое поле, которое будет хранить JSON -->
                <input type="hidden" name="availableTimes" id="availableTimes">

                <div class="form-group">
                    <label><strong>Формат занятий:</strong></label>
                    <select name="preferredFormat" disabled class="form-control editable" readonly>
                        <option value="ONLINE" ${tutor.preferredFormat == 'ONLINE' ? 'selected' : ''}>Онлайн</option>
                        <option value="OFFLINE" ${tutor.preferredFormat == 'OFFLINE' ? 'selected' : ''}>Оффлайн</option>
                        <option value="HYBRID" ${tutor.preferredFormat == 'HYBRID' ? 'selected' : ''}>Онлайн и оффлайн</option>
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

