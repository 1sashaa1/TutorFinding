package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.ScheduleSlot;
import com.jtspringproject.JtSpringProject.models.Tutors;
import com.jtspringproject.JtSpringProject.services.scheduleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jtspringproject.JtSpringProject.services.tutorService;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.models.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class ScheduleController {

    private final scheduleService scheduleSlotService;
    private final tutorService tutorService;
    private final userService userService;

    @Autowired
    public ScheduleController(scheduleService scheduleSlotService,
                              tutorService tutorService,
                              userService userService) {
        this.scheduleSlotService = scheduleSlotService;
        this.tutorService = tutorService;
        this.userService = userService;
    }

    // Обработать добавление нового слота
    @PostMapping("/tutor/schedule/add")
    public String addScheduleSlot(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  @RequestParam("start_time") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                  @RequestParam("end_time") @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Tutors tutor = tutorService.findByUserUsername(user.getId());

        ScheduleSlot slot = new ScheduleSlot();
        slot.setDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setAvailable(true);
        slot.setTutor(tutor);

        scheduleSlotService.saveSlot(slot);

        redirectAttributes.addFlashAttribute("msg", "Новый слот успешно добавлен!");
        return "redirect:/tutor/schedule";
    }

    // Удалить слот (по id)
    @PostMapping("/tutor/schedule/delete/{id}")
    public String deleteSlot(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        scheduleSlotService.deleteSlot(id);
        redirectAttributes.addFlashAttribute("msg", "Слот успешно удалён.");
        return "redirect:/tutor/schedule";
    }
}
