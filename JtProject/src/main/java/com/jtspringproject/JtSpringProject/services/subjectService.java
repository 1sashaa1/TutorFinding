package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.jtspringproject.JtSpringProject.dao.subjectDao;

@Service
public class subjectService {

    @Autowired
    private subjectDao subjectDao;

    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return subjectDao.findAll();
    }

    @Transactional(readOnly = true)
    public Subject getSubjectById(int id) {
        return subjectDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Subject getSubjectByName(String name) {
        return subjectDao.findByName(name);
    }

    @Transactional
    public void saveSubject(Subject subject) {
        try {
            subjectDao.save(subject);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Ошибка при сохранении предмета", e);
        }
    }

    @Transactional
    public void updateSubject(Subject subject) {
        subjectDao.update(subject);
    }

    @Transactional
    public void deleteSubject(int id) {
        subjectDao.delete(id);
    }

    @Transactional
    public Subject findById(int id) {
        return subjectDao.findById(id);
    }
}

