package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Payment;
import com.jtspringproject.JtSpringProject.models.PaymentStatus;
import com.jtspringproject.JtSpringProject.models.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class paymentDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Payment save(Payment payment) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(payment);
        return payment;
    }

    @Transactional
    public Payment findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Payment.class, id);
    }

    @Transactional
    public List<Payment> findByClientId(Long clientId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Payment> query = session.createQuery(
                "FROM Payment WHERE client.id = :clientId ORDER BY paymentDate DESC",
                Payment.class
        );
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Transactional
    public void updateStatus(Long paymentId, PaymentStatus status) {
        Session session = sessionFactory.getCurrentSession();
        Payment payment = session.get(Payment.class, paymentId);
        if (payment != null) {
            payment.setStatus(status);
            session.update(payment);
        }
    }
}
