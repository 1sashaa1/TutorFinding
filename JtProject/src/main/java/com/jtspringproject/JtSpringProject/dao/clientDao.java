package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.Clients;
import com.jtspringproject.JtSpringProject.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class clientDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Transactional
    public Optional<Clients> findById(int id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Clients client = session.get(Clients.class, id);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            // Логирование ошибки
            System.err.println("Error finding client by id " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    @Transactional
    public  Clients saveClient(Clients client){
        this.sessionFactory.getCurrentSession().saveOrUpdate(client);
        System.out.println("User added" + client.getId());
        return client;
    }
}
