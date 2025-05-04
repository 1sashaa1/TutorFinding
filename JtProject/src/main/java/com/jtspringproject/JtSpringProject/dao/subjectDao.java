package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Subject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public Map<String, Long> getPopularSubjects(int limit) {
        Session session = sessionFactory.getCurrentSession();

        List<Object[]> results = session.createQuery(
                        "SELECT s.name, COUNT(l.id) " +
                                "FROM Lesson l JOIN l.subject s " +
                                "GROUP BY s.name " +
                                "ORDER BY COUNT(l.id) DESC",
                        Object[].class
                )
                .setMaxResults(limit)
                .getResultList();

        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }
}
