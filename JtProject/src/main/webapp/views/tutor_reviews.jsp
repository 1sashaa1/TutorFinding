<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Мои отзывы</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .review-card {
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .review-card:hover {
            transform: translateY(-5px);
        }
        .rating {
            color: #FFD700;
            font-size: 1.2rem;
        }
        .student-avatar {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            object-fit: cover;
        }
        .empty-reviews {
            text-align: center;
            padding: 50px;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-comments me-2"></i>Отзывы обо мне</h2>
        <a href="/tutor_inform" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left me-1"></i> Назад в профиль
        </a>
    </div>

    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Средний рейтинг</h5>
                    <div class="d-flex align-items-center">
                        <span class="display-4 me-3">${rating}</span>
                        <div class="rating">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= rating}">
                                        <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:when test="${i - 0.5 <= rating}">
                                        <i class="fas fa-star-half-alt"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Всего отзывов</h5>
                    <p class="display-4">${reviews.size()}</p>
                </div>
            </div>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty reviews}">
            <div class="row">
                <div class="col-md-12">
                    <h4 class="mb-3">Все отзывы</h4>
                    <c:forEach items="${reviews}" var="review">
                        <div class="card review-card mb-3">
                            <div class="card-body">
                                <div class="d-flex align-items-start mb-3">
                                            <div class="student-avatar bg-secondary text-white d-flex align-items-center justify-content-center me-3">
                                                <i class="fas fa-user"></i>
                                            </div>
                                    <div>
                                        <h5>${review.client.name}</h5>
                                        <div class="rating mb-2">
                                            <c:forEach begin="1" end="5" var="i">
                                                <c:choose>
                                                    <c:when test="${i <= review.rating}">
                                                        <i class="fas fa-star"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="far fa-star"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <p class="card-text">${review.comment}</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-reviews">
                <i class="far fa-comment-dots fa-4x mb-3"></i>
                <h4>Пока нет отзывов</h4>
                <p class="text-muted">Ваши ученики еще не оставили отзывов о вас.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>