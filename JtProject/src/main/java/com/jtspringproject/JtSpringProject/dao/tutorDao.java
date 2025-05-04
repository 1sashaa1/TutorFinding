package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Review;
import com.jtspringproject.JtSpringProject.models.Tutors;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class tutorDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Transactional
    public void saveTutor(Tutors tutor) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(tutor);
        System.out.println("User added: " + tutor.getUser().getUsername());
    }
    @Transactional
    public boolean tutorExists(int tutorid) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Tutors where id = :tutorid");
        query.setParameter("tutorid",tutorid);
        return !query.getResultList().isEmpty();
    }
    @Transactional
    public Tutors getTutorId(int tutorid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Tutors where id = :tutorid");
        query.setParameter("tutorid", tutorid);
        Tutors tutor = (Tutors) query.uniqueResult();

        if (tutor != null) {
            Hibernate.initialize(tutor.getReviews());
            for (Review review : tutor.getReviews()) {
                Hibernate.initialize(review.getClient());
            }
        }

        return tutor;
    }



    @Transactional
    public List<Tutors> getTutors() {
        return this.sessionFactory.getCurrentSession().createQuery("from Tutors ").list();
    }
    @Transactional
    public Tutors findByUserUsername(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Tutors where id = :id");
        query.setParameter("id",id);
        return (Tutors) query.uniqueResult();
    }

    @Transactional
    public  Tutors updateTutor(Tutors tutor){
        this.sessionFactory.getCurrentSession().saveOrUpdate(tutor);
        System.out.println("User updated" + tutor.getUser().getUsername());
        return tutor;
    }
    @Transactional
    public Tutors findById(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Tutors where id = :id");
        query.setParameter("id",id);
        return (Tutors) query.uniqueResult();
    }

    @Transactional
    public long countActiveTutors() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(t.id) FROM Tutors t WHERE t.rate != NULL ";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult();
        }
    }
}
