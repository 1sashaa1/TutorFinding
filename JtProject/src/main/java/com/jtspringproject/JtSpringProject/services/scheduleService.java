package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dao.scheduleDao;
import com.jtspringproject.JtSpringProject.models.ScheduleSlot;
import com.jtspringproject.JtSpringProject.models.Tutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class scheduleService {

    @Autowired
    private scheduleDao scheduleSlotDao;
    @Autowired
    private com.jtspringproject.JtSpringProject.dao.lessonDao lessonDao;

    public List<ScheduleSlot> getAllSlots() {
        return scheduleSlotDao.getAllSlots();
    }

    public List<ScheduleSlot> getSlotsByTutor(Tutors tutor) {
        int tutorid = tutor.getId();
        System.out.println("tutorid" +tutorid);
        return scheduleSlotDao.findByTutor(tutorid);
    }


    public ScheduleSlot getSlotById(Long id) {
        return scheduleSlotDao.getSlotById(id);
    }

    public void deleteSlot(Long id) {
        scheduleSlotDao.deleteSlot(id);
    }
    public void deleteSlotsByTutor(int tutorId) {
        scheduleSlotDao.deleteSlotsByTutor(tutorId);
    }

    public void saveSlot(ScheduleSlot slot) {
        validateSlot(slot);
        try {
            scheduleSlotDao.saveSlot(slot);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Ошибка при сохранении слота расписания", e);
        }
    }

    private void validateSlot(ScheduleSlot slot) {
        if (slot.getStartTime().isAfter(slot.getEndTime())) {
            throw new IllegalArgumentException("Время начала должно быть раньше времени окончания");
        }
    }

    public void deleteAvailableSlotsByTutor(int tutorId) {
        List<ScheduleSlot> availableSlots = scheduleSlotDao.findAvailableSlotsByTutor(tutorId);

        for (ScheduleSlot slot : availableSlots) {
            if (!lessonDao.existsByScheduleSlotId(slot.getId())) {
                scheduleSlotDao.delete(slot);
            }
        }
    }

}