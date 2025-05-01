package com.jtspringproject.JtSpringProject.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.jtspringproject.JtSpringProject.models.Clients;
import com.jtspringproject.JtSpringProject.models.Lesson;
import com.jtspringproject.JtSpringProject.models.LessonStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class lessonDao{

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Transactional
    public List<Lesson> getLessons() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from Lesson", Lesson.class)
                .list();
    }

    @Transactional
    public Lesson addLesson(Lesson lesson) {
        this.sessionFactory.getCurrentSession().save(lesson);
        return lesson;
    }

    @Transactional
    public Lesson getLesson(int id) {
        return this.sessionFactory.getCurrentSession().get(Lesson.class, id);
    }

    @Transactional
    public Lesson updateLesson(Lesson lesson) {
        this.sessionFactory.getCurrentSession().update(lesson);
        return lesson;
    }

    @Transactional
    public boolean deleteLesson(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Lesson lesson = session.get(Lesson.class, id);
        if (lesson != null) {
            session.delete(lesson);
            return true;
        }
        return false;
    }

    @Transactional
    public List<Lesson> findClientLessons(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "select l from Lesson l " +
                                "join fetch l.teacher t " +
                                "join fetch t.user u " +
                                "join fetch l.subject s " +
                                "join fetch l.scheduleSlot ssl " +
                                "where l.client.id = :clientId",
                        Lesson.class)
                .setParameter("clientId", id)
                .list();
    }

    @Transactional
    public boolean cancelLesson(int lessonId) {
        Session session = sessionFactory.getCurrentSession();
        Lesson lesson = session.get(Lesson.class, lessonId);

        if (lesson == null || lesson.getStatus() == LessonStatus.CANCELED) {
            return false;
        }

        lesson.setStatus(LessonStatus.CANCELED);

        BigDecimal rate = lesson.getTeacher().getRate();
        Clients client = lesson.getClient();
        client.setBudget(client.getBudget().add(rate));

        return true;
    }


}