package com.jtspringproject.JtSpringProject.dao;

import com.jtspringproject.JtSpringProject.models.ScheduleSlot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class scheduleDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Сохранение или обновление слота расписания
    @Transactional
    public void saveSlot(ScheduleSlot slot) {
        System.out.println("TUTOR: " + slot.getTutor());
        this.sessionFactory.getCurrentSession().saveOrUpdate(slot);
        System.out.println("Schedule Slot added/updated: " + slot.getId());
    }

    // Получение слота по ID
    @Transactional
    public ScheduleSlot getSlotById(Long id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from ScheduleSlot where id = :id");
        query.setParameter("id", id);
        return (ScheduleSlot) query.uniqueResult();
    }

    // Получение всех слотов расписания
    @Transactional
    public List<ScheduleSlot> getAllSlots() {
        return this.sessionFactory.getCurrentSession().createQuery("from ScheduleSlot").list();
    }

    // Получение слотов по преподавателю
    @Transactional(readOnly = true)
    public List<ScheduleSlot> findByTutor(int tutorId) {
        System.out.println("Searching slots for tutorId: " + tutorId);
        List<ScheduleSlot> result = sessionFactory.getCurrentSession()
                .createQuery("FROM ScheduleSlot WHERE tutor.id = :tutorId ORDER BY date, start_time", ScheduleSlot.class)
                .setParameter("tutorId", tutorId)
                .getResultList();
        System.out.println("Found " + result.size() + " slots");
        return result;
    }

    // Удаление слота расписания по ID
    @Transactional
    public void deleteSlot(Long id) {
        Query query = sessionFactory.getCurrentSession().createQuery("delete from ScheduleSlot where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        System.out.println("Schedule Slot deleted with id: " + id);
    }
    @Transactional
    public void deleteSlotsByTutor(int tutorId) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM ScheduleSlot WHERE tutor.id = :tutorId");
        query.setParameter("tutorId", tutorId);
        query.executeUpdate();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<ScheduleSlot> findAvailableSlotsByTutor(int tutorId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM ScheduleSlot s WHERE s.tutor.id = :tutorId AND s.available = true";
        return session.createQuery(hql)
                .setParameter("tutorId", tutorId)
                .list();
    }

    @Transactional
    public void delete(ScheduleSlot slot) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(slot);
    }

}
