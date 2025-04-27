package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.*;

import java.io.Console;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jtspringproject.JtSpringProject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.services.tutorService;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.scheduleService;
import com.jtspringproject.JtSpringProject.services.subjectService;
import com.jtspringproject.JtSpringProject.services.lessonService;
import com.jtspringproject.JtSpringProject.services.reviewService;

@Controller
public class ClientController {

    private final userService userService;
    private final scheduleService scheduleService;
    private final scheduleService scheduleSlotService;
    private final subjectService subjectService;
    private final lessonService lessonService;
    private final reviewService reviewService;

        @Autowired
        public ClientController(clientService clientService, com.jtspringproject.JtSpringProject.services.userService userService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService, com.jtspringproject.JtSpringProject.services.lessonService lessonService, com.jtspringproject.JtSpringProject.services.reviewService reviewService) {
            this.userService = userService;
            this.scheduleService = scheduleService;
            this.scheduleSlotService = scheduleSlotService;
            this.subjectService = subjectService;
            this.lessonService = lessonService;
            this.reviewService = reviewService;
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

    }
