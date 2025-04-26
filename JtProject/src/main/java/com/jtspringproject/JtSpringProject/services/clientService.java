package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class clientService {

    @Autowired
    private com.jtspringproject.JtSpringProject.dao.clientDao clientDao;

}
