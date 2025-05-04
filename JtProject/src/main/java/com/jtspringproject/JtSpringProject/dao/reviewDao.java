package com.jtspringproject.JtSpringProject.dao;
import javax.transaction.Transactional;

import com.jtspringproject.JtSpringProject.models.Lesson;
import com.jtspringproject.JtSpringProject.models.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                .createQuery("FROM reviews r JOIN FETCH r.client WHERE r.teacher.id = :teacherId", Review.class)
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

    @Transactional
    public Double getAverageRating() {
        Session session = sessionFactory.getCurrentSession();
        try {
            return session.createQuery(
                            "SELECT AVG(r.rating) FROM reviews r WHERE r.rating IS NOT NULL",
                            Double.class)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating average rating", e);
        }
    }

    @Transactional
    public Map<Integer, Long> getRatingDistribution() {
        Session session = sessionFactory.getCurrentSession();
        try {
            List<Object[]> results = session.createQuery(
                            "SELECT r.rating, COUNT(r) FROM reviews r GROUP BY r.rating ORDER BY r.rating DESC",
                            Object[].class)
                    .getResultList();

            Map<Integer, Long> distribution = new LinkedHashMap<>();
            results.forEach(res -> distribution.put((Integer)res[0], (Long)res[1]));
            return distribution;
        } catch (Exception e) {
            throw new RuntimeException("Error getting rating distribution", e);
        }
    }

    @Transactional
    public int getTotalReviewsCount() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(r.id) FROM reviews r";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult().intValue();
        }
    }

    @Transactional
    public long getTutorsWithReviewsCount() {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT COUNT(DISTINCT r.teacher.id) FROM reviews r";
        return session.createQuery(hql, Long.class).uniqueResult();
    }


}