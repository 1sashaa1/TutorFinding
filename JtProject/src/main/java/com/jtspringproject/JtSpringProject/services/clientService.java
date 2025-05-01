package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.Clients;
import com.jtspringproject.JtSpringProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.jtspringproject.JtSpringProject.dao.clientDao;
import java.util.List;
import java.util.Optional;

@Service
public class clientService {

    @Autowired
    private clientDao clientDao;

    public Optional<Clients> findByUser(User user) {
        if (user == null) {
            return Optional.empty();
        }
        return clientDao.findById(user.getId());
    }

    public Clients addClient(Clients client) {
        try {
            return this.clientDao.saveClient(client);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Add user error");
        }
    }
}
