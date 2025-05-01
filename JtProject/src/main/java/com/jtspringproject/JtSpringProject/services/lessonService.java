package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtspringproject.JtSpringProject.dao.lessonDao;
import java.util.List;

@Service
public class lessonService {

    @Autowired
    private lessonDao lessonDao;

    public List<Lesson> getLessons() {
        return this.lessonDao.getLessons();
    }

    public Lesson addLesson(Lesson lesson) {
        return this.lessonDao.addLesson(lesson);
    }

    public Lesson getLesson(int id) {
        return this.lessonDao.getLesson(id);
    }

    public Lesson updateLesson(int id, Lesson lesson) {
        lesson.setId(id);
        return this.lessonDao.updateLesson(lesson);
    }

    public boolean deleteLesson(int id) {
        return this.lessonDao.deleteLesson(id);
    }

    public List<Lesson> findClientLessons(int id){
        return lessonDao.findClientLessons(id);
    }

    public boolean cancelLesson(int id){
        return  this.lessonDao.cancelLesson(id);
    }
}
