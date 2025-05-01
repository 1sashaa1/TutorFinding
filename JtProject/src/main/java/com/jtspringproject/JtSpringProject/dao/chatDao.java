package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.dto.messageDto;
import com.jtspringproject.JtSpringProject.models.Chat;
import org.apache.logging.log4j.message.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class chatDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public chatDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Optional<Chat> findChatBetweenUsers(Long user1, Long user2) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Chat c WHERE (c.userId = :user1 AND c.teacherId = :user2) " +
                "OR (c.userId = :user2 AND c.teacherId = :user1)";

        try {
            Chat chat = session.createQuery(hql, Chat.class)
                    .setParameter("user1", user1)
                    .setParameter("user2", user2)
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(chat);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Long save(Chat chat) {
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.save(chat);
    }

    @Transactional
    public Chat findById(Long chatId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Chat.class, chatId);
    }
}