package com.jtspringproject.JtSpringProject.services;
import com.jtspringproject.JtSpringProject.dao.reviewDao;
import com.jtspringproject.JtSpringProject.models.Lesson;
import com.jtspringproject.JtSpringProject.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtspringproject.JtSpringProject.dao.lessonDao;

import javax.transaction.Transactional;
import java.util.List;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class reviewService {

    @Autowired
    private reviewDao reviewDAO;

    @Autowired
    private userService userService;

    @Autowired
    private lessonService lessonService;

    @Transactional
    public Review getReview(int id) {
        return reviewDAO.getReview(id);
    }

    @Transactional
    public List<Review> getReviewsByTeacher(int teacherId) {
        return reviewDAO.getReviewsByTeacher(teacherId);
    }

    @Transactional
    public List<Review> getReviewsByClient(int clientId) {
        return reviewDAO.getReviewsByClient(clientId);
    }

    @Transactional
    public Review createReview(int lessonId, int rating, String comment, int clientId) {
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);

        Lesson lesson = lessonService.getLesson(lessonId);
        review.setClient(userService.getUser(clientId));
        review.setTeacher(lesson.getTeacher());

        reviewDAO.saveReview(review);
        return review;
    }

    @Transactional
    public Review updateReview(int id, int rating, String comment) {
        Review review = reviewDAO.getReview(id);
        if (review != null) {
            review.setRating(rating);
            review.setComment(comment);
            reviewDAO.updateReview(review);
        }
        return review;
    }

    @Transactional
    public void deleteReview(int id) {
        reviewDAO.deleteReview(id);
    }

    @Transactional
    public double getAverageTeacherRating(int teacherId) {
        return reviewDAO.getAverageRatingForTeacher(teacherId);
    }
    @Transactional
    public double getAverageRating() {
        Double avg = reviewDAO.getAverageRating();
        return avg != null ? avg : 0.0;
    }
    @Transactional
    public Map<Integer, Long> getRatingDistribution() {
        return reviewDAO.getRatingDistribution();
    }
    @Transactional
    public int getTotalReviewsCount() {
        return reviewDAO.getTotalReviewsCount();
    }
    @Transactional
    public long getTutorsWithReviewsCount() {
        return reviewDAO.getTutorsWithReviewsCount();
    }

}