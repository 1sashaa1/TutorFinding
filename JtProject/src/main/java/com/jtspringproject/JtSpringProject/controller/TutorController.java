package com.jtspringproject.JtSpringProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtspringproject.JtSpringProject.models.*;
import com.jtspringproject.JtSpringProject.services.tutorService;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.scheduleService;
import com.jtspringproject.JtSpringProject.services.subjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class TutorController {

    private final tutorService tutorService;
    private final userService userService;
    private final scheduleService scheduleService;
    private final scheduleService scheduleSlotService;
    private final subjectService subjectService;

    @Autowired
    public TutorController(userService userService, tutorService tutorService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService) {
        this.tutorService = tutorService;
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.scheduleSlotService = scheduleSlotService;
        this.subjectService = subjectService;
    }

    @GetMapping("/tutor_inform")
    public String showTutorRegistrationForm(Model model) {
        // Получаем имя текущего пользователя
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Получаем объект User по имени (или email) текущего пользователя
        User currentUser = userService.getUserByUsername(username);

        if (currentUser != null) {
            int userId = currentUser.getId(); // Получаем id текущего пользователя

            // Получаем данные преподавателя по id
            Tutors tutor = tutorService.getTutorId(userId);

            if (tutor != null) {
                model.addAttribute("tutor", tutor);
                model.addAttribute("username", username);
                return "tutor_dashboard"; // Если преподаватель найден
            } else {
                return "tutor_inform"; // Если преподаватель не найден
            }
        } else {
            // Если текущий пользователь не найден
            return "redirect:/login"; // Перенаправляем на страницу логина
        }
    }

    @RequestMapping(value = "/registerTutor", method = RequestMethod.POST)
    public ModelAndView registerTutor(@ModelAttribute Tutors tutors) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        boolean exists = tutorService.checkTutorExists(user.getId());

        if (exists) {
            System.out.println("Репетитор с таким id уже записан: " + tutors.getUser().getUsername());
            ModelAndView mView = new ModelAndView("tutor_dashboard");
            mView.addObject("username", username);
            mView.addObject("msg", "Данные о Вас уже записаны в системе!");
            return mView;
        } else {
            try {
                tutors.setUser(user);  // Устанавливаем пользователя в Tutors
                tutorService.addTutor(tutors);  // Сохраняем Tutors
                user.setTutor(tutors);  // Устанавливаем Tutors в User
                userService.addUser(user);  // Обновляем User

                ModelAndView mView = new ModelAndView("tutor_dashboard");
                mView.addObject("msg", "Вы успешно зарегистрировали свои данные!");
                mView.addObject("username", username);
                System.out.println("Новые данные о репетиторе созданы: " + tutors.getUser().getUsername());
                return mView;
            } catch (Exception e) {
                ModelAndView mView = new ModelAndView("tutor_inform");
                mView.addObject("msg", "Ошибка при дополнительной регистрации: " + e.getMessage());
                return mView;
            }
        }
    }

    // Страница успешной регистрации
    @GetMapping("/tutor/registration-success")
    public String registrationSuccess() {
        return "tutor_registration_success";  // Страница с подтверждением
    }

    @GetMapping("/tutorProfileDisplay")
    public String displayProfile(Model model, Principal principal) {
        String username = principal.getName(); // Получаем логин текущего пользователя
        List<Subject> subjects = subjectService.getAllSubjects();
        User user = userService.getUserByUsername(username);
        Tutors tutor = tutorService.findByUserUsername(user.getId()); // Получаем преподавателя

        if (tutor == null) {
            return "redirect:/error";
        }
        System.out.println("Предметы: " + subjects);
        model.addAttribute("tutor", tutor);
        model.addAttribute("username", username);
        model.addAttribute("allSubjects", subjects);

        // Фото
        if (tutor.getPhoto() != null) {
            String base64Photo = Base64.getEncoder().encodeToString(tutor.getPhoto());
            model.addAttribute("photo", "data:image/jpeg;base64," + base64Photo);
        } else {
            model.addAttribute("photo", "/static/images/default-avatar.png");
        }

        // Загружаем расписание
        List<ScheduleSlot> slots = scheduleSlotService.getSlotsByTutor(tutor);
        System.out.println("Расписание преподавателя -- " + slots);
        model.addAttribute("slots", slots);

        return "tutorProfileDisplay";
    }

    @PostMapping("/uploadPhoto")
    public String uploadPhoto(@RequestParam("photo") MultipartFile file, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Tutors tutor = tutorService.findByUserUsername(user.getId());

        if (tutor == null) {
            return "redirect:/error"; // Если преподаватель не найден
        }

        try {
            byte[] photoBytes = file.getBytes();
            tutor.setPhoto(photoBytes);
            tutorService.addTutor(tutor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/tutorProfileDisplay"; // Обновляем страницу
    }


    @PostMapping("/updateProfile")
    public String updateProfile(
            @RequestParam("tutorId") int tutorId,
            @RequestParam("subjectId") int subjectId,
            @RequestParam("rate") Double rate,
            @RequestParam("experience") String experience,
            @RequestParam("education") String education,
            @RequestParam("preferredFormat") String preferredFormat,
            @RequestParam("availableTimes") String availableTimesJson,
            Principal principal, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Получаем преподавателя
            Tutors tutor = tutorService.findById(tutorId);
            if (tutor == null) {
                redirectAttributes.addFlashAttribute("error", "Преподаватель не найден");
                return "redirect:/error";
            }

            // Проверяем принадлежность преподавателя текущему пользователю
            String currentUsername = principal.getName();
            if (!tutor.getUser().getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error", "Нет прав на редактирование");
                return "redirect:/error";
            }
            // Обновляем предмет
            Subject subject = subjectService.findById(subjectId);
            tutor.setSubject(subject);

            // Обновляем остальные данные
            tutor.setRate(BigDecimal.valueOf(rate));
            tutor.setExperience(experience);
            tutor.setEducation(education);
            tutor.setPreferredFormat(PreferredFormat.valueOf(preferredFormat));

            // Обновляем расписание
            updateTutorSchedule(tutor, availableTimesJson);

            // Сохраняем изменения
            tutorService.updateTutor(tutor);

            model.addAttribute("tutor", tutor);

            redirectAttributes.addFlashAttribute("success", "Профиль успешно обновлен");
            return "redirect:/tutorProfileDisplay";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
            return "redirect:/tutorProfileDisplay";
        }
    }

    private void updateTutorProfile(Tutors tutor, Subject subject, Double rate,
                                    String experience, String education, String preferredFormat) {
        tutor.setSubject(subject);
        tutor.setRate(BigDecimal.valueOf(rate));
        tutor.setExperience(experience);
        tutor.setEducation(education);
        tutor.setPreferredFormat(PreferredFormat.valueOf(preferredFormat));
    }

    private void updateTutorSchedule(Tutors tutor, String availableTimesJson)
            throws JsonProcessingException, IllegalArgumentException {

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> slotsData = mapper.readValue(availableTimesJson,
                new TypeReference<List<Map<String, String>>>(){});

        // Удаляем старые слоты
        scheduleService.deleteSlotsByTutor(tutor.getId());

        // Создаем и сохраняем новые слоты
        for (Map<String, String> slotData : slotsData) {
            ScheduleSlot slot = new ScheduleSlot();
            slot.setDate(LocalDate.parse(slotData.get("date")));

            // Используем правильные ключи из JSON (с подчеркиванием)
            String startTimeStr = slotData.get("start_time");
            String endTimeStr = slotData.get("end_time");

            if (startTimeStr == null || endTimeStr == null) {
                throw new IllegalArgumentException("Не указано время начала или окончания");
            }

            System.out.println("Received start time: " + startTimeStr);
            slot.setStartTime(LocalTime.parse(startTimeStr));
            slot.setEndTime(LocalTime.parse(endTimeStr));
            slot.setAvailable(true);
            slot.setTutor(tutor);

            scheduleService.saveSlot(slot);
        }
    }
    @GetMapping("/tutor/photo/{id}")
    public ResponseEntity<byte[]> getTutorPhoto(@PathVariable Long id) {
        Tutors tutor = tutorService.findByUserUsername(Math.toIntExact(id));

        if (tutor == null || tutor.getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Укажи нужный формат (JPEG, PNG и т. д.)
                .body(tutor.getPhoto()); // Отправляем бинарные данные
    }
}

