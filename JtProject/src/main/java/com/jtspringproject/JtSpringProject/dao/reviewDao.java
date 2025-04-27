package com.jtspringproject.JtSpringProject.dao;
import javax.transaction.Transactional;

import com.jtspringproject.JtSpringProject.models.Lesson;
import com.jtspringproject.JtSpringProject.models.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class reviewDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Review getReview(int id) {
        return sessionFactory.getCurrentSession().get(Review.class, id);
    }

    @Transactional
    public List<Review> getReviewsByTeacher(int teacherId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM reviews WHERE teacher.id = :teacherId", Review.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }

    @Transactional
    public List<Review> getReviewsByClient(int clientId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM reviews WHERE client.id = :clientId", Review.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }

    @Transactional
    public void saveReview(Review review) {
        Session session = sessionFactory.getCurrentSession();

        Review existingReview = (Review) session.createQuery(
                        "FROM reviews r WHERE r.client.id = :clientId AND r.teacher.id = :tutorId")
                .setParameter("clientId", review.getClient().getId())
                .setParameter("tutorId", review.getTeacher().getId())
                .uniqueResult();

        if (existingReview != null) {
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());
            session.update(existingReview);
        } else {
            session.persist(review);
        }
    }


    @Transactional
    public void updateReview(Review review) {
        sessionFactory.getCurrentSession().merge(review);
    }

    @Transactional
    public void deleteReview(int id) {
        Review review = getReview(id);
        if (review != null) {
            sessionFactory.getCurrentSession().delete(review);
        }
    }

    @Transactional
    public double getAverageRatingForTeacher(int teacherId) {
        Double result = (Double) sessionFactory.getCurrentSession()
                .createQuery("SELECT AVG(r.rating) FROM reviews r WHERE r.teacher.id = :teacherId")
                .setParameter("teacherId", teacherId)
                .uniqueResult();
        return result != null ? result : 0.0;
    }
}