package com.jtspringproject.JtSpringProject;

import com.jtspringproject.JtSpringProject.models.Tutors;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.tutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TutorRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private userService userService;

    @MockBean
    private tutorService tutorService;

    @Test
    @WithMockUser(username = "new_tutor")
    public void testSuccessfulTutorRegistration() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("new_tutor");
        user.setPassword("secure123");

        when(userService.getUserByUsername("new_tutor")).thenReturn(user);
        when(tutorService.checkTutorExists(1)).thenReturn(false);

        mockMvc.perform(post("/registerTutor")
                        .with(csrf())
                        .flashAttr("tutors", new Tutors()))
                .andExpect(status().isOk())
                .andExpect(view().name("tutor_dashboard"))
                .andExpect(model().attributeExists("msg"))
                .andExpect(model().attribute("username", "new_tutor"));

        verify(tutorService).addTutor(any(Tutors.class));
        verify(userService).addUser(any(User.class));
    }
}