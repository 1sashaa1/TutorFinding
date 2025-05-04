package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.controller.TutorController;
import com.jtspringproject.JtSpringProject.models.Tutors;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@WebMvcTest(TutorController.class)
@AutoConfigureMockMvc
public class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private tutorService tutorService;
    @MockBean
    private userService userService;
    @MockBean
    private scheduleService scheduleService;
    @MockBean
    private subjectService subjectService;
    @MockBean
    private reviewService reviewService;
    @MockBean
    private lessonService lessonService;
    @MockBean
    private YouTubeService youTubeService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "TUTOR")
    public void testCompleteLessonSuccess() throws Exception {
        String reason = "Student request";
        Long lessonId = 1L;

        // Мокируем сервисный метод
        when(lessonService.completeLesson(lessonId.intValue())).thenReturn(true);

        mockMvc.perform(post("/tutor/lessons/{id}/complete", lessonId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"" + reason + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl").value("/tutor/schedule"))
                // Изменяем проверку - теперь ожидаем наличие поля success со значением true
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "TUTOR")
    public void testCompleteLessonFailure() throws Exception {
        Long lessonId = 1L;

        doThrow(new RuntimeException("Error completing lesson"))
                .when(lessonService).completeLesson(lessonId.intValue());

        mockMvc.perform(post("/tutor/lessons/{id}/complete", lessonId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.redirectUrl").value("/tutor/schedule"));
    }

    @Test
    @WithMockUser(roles = "TUTOR")
    public void testCancelLessonSuccess() throws Exception {
        Long lessonId = 1L;
        String reason = "Student request";

        when(lessonService.cancelLesson(lessonId.intValue())).thenReturn(true);

        mockMvc.perform(post("/tutor/lessons/{id}/cancel", lessonId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"" + reason + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUrl").value("/tutor/schedule"))
                .andExpect(jsonPath("$.success").doesNotExist());
    }


    @Test
    @WithMockUser(roles = "TUTOR")
    public void testCancelLessonFailure() throws Exception {
        Long lessonId = 1L;
        String reason = "System error";

        doThrow(new RuntimeException("Error cancelling lesson"))
                .when(lessonService).cancelLesson(lessonId.intValue());

        mockMvc.perform(post("/tutor/lessons/{id}/cancel", lessonId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"" + reason + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.redirectUrl").value("/tutor/schedule"));
    }
}