package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.controller.TutorController;
import com.jtspringproject.JtSpringProject.controller.UserController;
import com.jtspringproject.JtSpringProject.models.Clients;
import com.jtspringproject.JtSpringProject.models.Tutors;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

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
    private clientService clientService;
    @MockBean
    private paymentService paymentService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    public void testRegisterNewClient_Success() throws Exception {
        // Подготовка тестовых данных
        User testUser = new User();
        testUser.setUsername("newclient");
        testUser.setPassword("pass1234");
        testUser.setEmail("client@example.com");

        // Мокирование
        when(userService.checkUserExists(anyString())).thenReturn(false);
        when(userService.addUser(any(User.class))).thenReturn(testUser);

        // Выполнение запроса
        mockMvc.perform(post("/newuserregister")
                        .with(csrf())
                        .param("username", testUser.getUsername())
                        .param("password", testUser.getPassword())
                        .param("email", testUser.getEmail())
                        .param("role", "CLIENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userLogin"))
                .andExpect(flash().attribute("success", "newclient зарегистрирован успешно."));

        // Проверки
        verify(userService, times(1)).addUser(any(User.class));
        verify(clientService, times(1)).addClient(any(Clients.class));
    }

    @Test
    @WithMockUser
    public void testRegisterUser_ShortPassword() throws Exception {
        // Подготовка тестовых данных
        User testUser = new User();
        testUser.setUsername("newuser");
        testUser.setPassword("123"); // Пароль короче 4 символов
        testUser.setEmail("user@example.com");

        // Выполнение запроса
        mockMvc.perform(post("/newuserregister")
                        .param("username", testUser.getUsername())
                        .param("password", testUser.getPassword())
                        .param("email", testUser.getEmail())
                        .param("role", "CLIENT")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("msg", "Пароль должен содержать минимум 4 символа"));

        // Проверка, что сервис не вызывался
        verify(userService, never()).addUser(any(User.class));
    }

    @Test
    @WithMockUser
    public void testRegisterUser_AlreadyExists() throws Exception {
        // Подготовка тестовых данных
        User testUser = new User();
        testUser.setUsername("existinguser");
        testUser.setPassword("validpass");
        testUser.setEmail("existing@example.com");

        // Мокирование
        when(userService.checkUserExists(testUser.getUsername())).thenReturn(true);

        // Выполнение запроса
        mockMvc.perform(post("/newuserregister")
                        .param("username", testUser.getUsername())
                        .param("password", testUser.getPassword())
                        .param("email", testUser.getEmail())
                        .param("role", "CLIENT")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("msg", "Пользователь уже существует."));

        // Проверка, что сервис не вызывался
        verify(userService, never()).addUser(any(User.class));
    }

}