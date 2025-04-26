package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Tutors;
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
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Tutors where id = :tutorid");
        query.setParameter("tutorid", tutorid);
        return (Tutors) query.uniqueResult();
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
}
