<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tutor Registration</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <style>
        body {
            padding: 20px;
        }
        .form-container {
            max-width: 800px;
            margin: 0 auto;
        }
        .form-group {
            margin-bottom: 1.5rem;
        }
        .btn-primary {
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container form-container">
    <h2>Tutor Registration</h2>
    <form action="registerTutor" method="POST">
        <div class="form-group">
            <label for="bio">Общая информация</label>
            <textarea id="bio" name="bio" class="form-control" rows="3" required></textarea>
        </div>
        <select name="subjectId" class="form-control" required>
            <option value="">-- Выберите предмет --</option>
            <c:forEach items="${allSubjects}" var="subject">
                <option value="${subject.id}">
                        ${subject.name}
                </option>
            </c:forEach>
        </select>


        <div class="form-group">
            <label for="experience">Опыт преподавания (количество лет)</label>
            <input type="number" id="experience" name="experience" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="education">Образование</label>
            <input type="text" id="education" name="education" class="form-control" required>
            <small class="form-text text-muted">Пожалуйста, введите Вашу профессиональную квалификацию.</small>
        </div>

        <div class="form-group">
            <label for="rate">Часовая ставка</label>
            <input type="number" id="rate" name="rate" class="form-control" step="5" required>
            <small class="form-text text-muted">Введите Вашу часовую ставку.</small>
        </div>

        <div class="form-group">
            <label for="preferredFormat">Формат проведения занятий</label>
            <select id="preferredFormat" name="preferredFormat" class="form-control" required>
                <option value="ONLINE">Онлайн</option>
                <option value="OFFLINE">Оффлайн</option>
                <option value="BOTH">Оба</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>
