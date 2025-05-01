package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class messageDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public messageDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Message> findByChatId(Long chatId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Message m WHERE m.chat.id = :chatId ORDER BY m.sentAt";
        return session.createQuery(hql, Message.class)
                .setParameter("chatId", chatId)
                .getResultList();
    }

    @Transactional
    public Long save(Message message) {
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.save(message);
    }

}
