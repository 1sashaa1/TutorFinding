package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.*;

import java.io.Console;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.jtspringproject.JtSpringProject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.services.tutorService;
import com.jtspringproject.JtSpringProject.services.*;
import com.jtspringproject.JtSpringProject.services.scheduleService;
import com.jtspringproject.JtSpringProject.services.subjectService;
import com.jtspringproject.JtSpringProject.services.lessonService;
import com.jtspringproject.JtSpringProject.services.reviewService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController {

    private final userService userService;
    private final scheduleService scheduleService;
    private final scheduleService scheduleSlotService;
    private final subjectService subjectService;
    private final lessonService lessonService;
    private final reviewService reviewService;
    private final clientService clientService;
    private final tutorService tutorService;

        @Autowired
        public ClientController(clientService clientService, com.jtspringproject.JtSpringProject.services.userService userService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService, com.jtspringproject.JtSpringProject.services.lessonService lessonService, com.jtspringproject.JtSpringProject.services.reviewService reviewService, com.jtspringproject.JtSpringProject.services.clientService clientService1, com.jtspringproject.JtSpringProject.services.tutorService tutorService) {
            this.userService = userService;
            this.scheduleService = scheduleService;
            this.scheduleSlotService = scheduleSlotService;
            this.subjectService = subjectService;
            this.lessonService = lessonService;
            this.reviewService = reviewService;
            this.clientService = clientService1;
            this.tutorService = tutorService;
        }

    @PostMapping("/reviews")
    public ResponseEntity<?> sendReview(
            @RequestParam int lessonId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            Principal principal) {

        try {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Пользователь не найден");
            }

            reviewService.createReview(lessonId, rating, comment, user.getId());

            return ResponseEntity.ok("Отзыв сохранен");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка: " + e.getMessage());
        }
    }
    @GetMapping("/check-profile")
    public ResponseEntity<Map<String, Boolean>> checkProfile(Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Optional<Clients> clientOpt = clientService.findByUser(user);

        clientOpt.ifPresentOrElse(
                client -> System.out.println("Найден клиент: " + client.getId()),
                () -> System.out.println("Клиент не найден")
        );

        boolean isEmpty = clientOpt
                .map(client -> client.getSubject() == null)
                .orElse(true);
        System.out.println(isEmpty);
        return ResponseEntity.ok(Collections.singletonMap("isEmpty", isEmpty));
    }
    @GetMapping("/clientProfileDisplay")
    public String displayProfile(Model model, Principal principal) {
        String username = principal.getName(); // Получаем логин текущего пользователя
        List<Subject> subjects = subjectService.getAllSubjects();
        User user = userService.getUserByUsername(username);
        Optional<Clients> clientOpt = clientService.findByUser(user);
        if (clientOpt.isEmpty()) {
            return "redirect:/error";
        }
        Clients client = clientOpt.get();
        model.addAttribute("client", client);
        System.out.println("Предметы: " + subjects);
        model.addAttribute("username", username);
        model.addAttribute("allSubjects", subjects);

        return "clientProfileDisplay";
    }

    @PostMapping("/save-client-profile")
    public String saveProfile(
            @RequestParam Integer age,
            @RequestParam Clients.Level level,
            @RequestParam int subjectId,
            @RequestParam Clients.PreferredFormat preferredFormat,
            @RequestParam BigDecimal budget,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.getUserByUsername(principal.getName());
        Clients client = clientService.findByUser(user).orElse(new Clients());

        client.setAge(age);
        client.setLevel(level);
        Subject subject = subjectService.findById(subjectId);
        client.setSubject(subject);
        client.setPreferredFormat(preferredFormat);
        client.setBudget(budget);
        client.setUser(user);

        clientService.addClient(client);

        redirectAttributes.addFlashAttribute("success", "Профиль успешно сохранен!");
        return "redirect:/clientProfileDisplay";
    }
    @PostMapping("/lessons/{lessonId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelLesson(@PathVariable int lessonId) {
        boolean success = lessonService.cancelLesson(lessonId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        if (success) {
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Занятие не найдено или уже отменено");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
