package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Subject;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class subjectDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Найти предмет по ID
    @Transactional(readOnly = true)
    public Subject findById(int id) {
        return sessionFactory.getCurrentSession().get(Subject.class, id);
    }

    // Сохранить предмет
    @Transactional
    public void save(Subject subject) {
        sessionFactory.getCurrentSession().save(subject);
    }

    // Обновить предмет
    @Transactional
    public void update(Subject subject) {
        sessionFactory.getCurrentSession().update(subject);
    }

    // Удалить предмет
    @Transactional
    public void delete(int id) {
        Subject subject = findById(id);
        if (subject != null) {
            sessionFactory.getCurrentSession().delete(subject);
        }
    }

    // Получить все предметы
    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        Query<Subject> query = sessionFactory.getCurrentSession()
                .createQuery("FROM Subject", Subject.class);
        return query.getResultList();
    }

    // Поиск по названию (если надо)
    @Transactional(readOnly = true)
    public Subject findByName(String name) {
        Query<Subject> query = sessionFactory.getCurrentSession()
                .createQuery("FROM Subject WHERE name = :name", Subject.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }
}
