<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Registration</title>
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
    <h2>Регистрация клиента</h2>
    <form action="/informationClient" method="POST">
        <div class="form-group">
            <label for="age">Age</label>
            <input type="number" id="age" name="age" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="level">Уровень</label>
            <select id="level" name="level" class="form-control" required>
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
            </select>
        </div>

        <div class="form-group">
            <label for="subjects">Subjects (JSON format)</label>
            <textarea id="subjects" name="subjects" class="form-control" rows="3" required></textarea>
            <small class="form-text text-muted">List your subjects as a JSON string, e.g. ["Math", "Science"]</small>
        </div>

        <div class="form-group">
            <label for="preferredFormat">Preferred Format</label>
            <select id="preferredFormat" name="preferredFormat" class="form-control" required>
                <option value="ONLINE">Online</option>
                <option value="OFFLINE">Offline</option>
                <option value="BOTH">Both</option>
            </select>
        </div>

        <div class="form-group">
            <label for="budget">Budget</label>
            <input type="number" id="budget" name="budget" class="form-control" step="0.01" required>
        </div>

        <div class="form-group">
            <label for="availableTimes">Available Times (JSON format)</label>
            <textarea id="availableTimes" name="availableTimes" class="form-control" rows="3" required></textarea>
            <small class="form-text text-muted">Provide your available times as a JSON string, e.g. [{"day": "Monday", "time": "10:00-12:00"}]</small>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>
