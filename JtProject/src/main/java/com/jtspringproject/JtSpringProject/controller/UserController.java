package com.jtspringproject.JtSpringProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtspringproject.JtSpringProject.models.*;
import java.io.Console;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.jtspringproject.JtSpringProject.services.subjectService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController{

	private final userService userService;
	private final tutorService tutorService;
	private final scheduleService scheduleService;
	private final lessonService lessonService;
	private final scheduleService scheduleSlotService;
	private final subjectService subjectService;


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(userService userService, tutorService productService, com.jtspringproject.JtSpringProject.services.tutorService tutorService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.lessonService lessonService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService) {
		this.userService = userService;
        this.tutorService = tutorService;
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
        this.scheduleSlotService = scheduleSlotService;
        this.subjectService = subjectService;
    }


	@GetMapping("/register")
	public String registerUser()
	{
		return "register";
	}

	@GetMapping("/buy")
	public String buy()
	{
		return "buy";
	}

	@GetMapping("/login")
	public ModelAndView userlogin(@RequestParam(required = false) String error) {
	    ModelAndView mv = new ModelAndView("userLogin");
	    if ("true".equals(error)) {
	        mv.addObject("msg", "Please enter correct email and password");
	    }
	    return mv;
	}

	@GetMapping("/")
	public ModelAndView indexPage() {
		ModelAndView mView = new ModelAndView("index");

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Tutors> tutors = tutorService.getTutors();

		mView.addObject("username", username);
		mView.addObject("tutors", tutors);

		// Проверяем фото для всех преподавателей
		Map<Integer, String> photos = new HashMap<>();
		for (Tutors tutor : tutors) {
			if (tutor.getPhoto() != null) {
				String base64Photo = Base64.getEncoder().encodeToString(tutor.getPhoto());
				photos.put(tutor.getId(), "data:image/jpeg;base64," + base64Photo);
			} else {
				photos.put(tutor.getId(), "/static/images/default-avatar.png"); // Заглушка
			}
		}

		mView.addObject("photos", photos);

		if (tutors.isEmpty()) {
			mView.addObject("msg", "No tutors are available");
		}

		return mView;
	}


	@GetMapping("/user/products")
	public ModelAndView getproduct() {

		ModelAndView mView = new ModelAndView("uproduct");

		List<Tutors> products = this.tutorService.getTutors();

		if(products.isEmpty()) {
			mView.addObject("msg","No products are available");
		}else {
			mView.addObject("products",products);
		}

		return mView;
	}

	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public ModelAndView newUserRegister(@ModelAttribute User user, @RequestParam String role) {
		boolean exists = this.userService.checkUserExists(user.getUsername());

		if (!exists) {
			System.out.println(user.getEmail());
			try {
				user.setRole(Roles.valueOf(role));

			} catch (IllegalArgumentException e) {
				ModelAndView mView = new ModelAndView("register");
				mView.addObject("msg", "Ошибка: выберите корректную роль.");
				return mView;
			}
			user.setCreated_at(LocalDateTime.now());
			this.userService.addUser(user);
			ModelAndView mView = new ModelAndView("register");
			mView.addObject("msg", user.getUsername() + " зарегистрирован успешно.");
			System.out.println("Новый пользователь создан: " + user.getUsername());
			return new ModelAndView("userLogin");
		} else {
			System.out.println("Пользователь с таким именем уже существует: " + user.getUsername());
			ModelAndView mView = new ModelAndView("register");
			mView.addObject("msg", user.getUsername() + " уже занят. Выберите другое имя.");
			return mView;
		}
	}


	@GetMapping("/profileDisplay")
	public String profileDisplay(Model model, HttpServletRequest request) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);
	
		if (user != null) {
			model.addAttribute("userid", user.getId());
			model.addAttribute("username", user.getUsername());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("password", user.getPassword()); 
			model.addAttribute("dataofregistration", user.getCreated_at());
	    } else {
	    	model.addAttribute("msg", "User not found");
	    } 

		return "updateProfile";
	}

		@GetMapping("/test")
		public String Test(Model model)
		{
			System.out.println("test page");
			model.addAttribute("author","jay gajera");
			model.addAttribute("id",40);
			
			List<String> friends = new ArrayList<String>();
			model.addAttribute("f",friends);
			friends.add("xyz");
			friends.add("abc");
			
			return "test";
		}
		
		// for learning purpose of model and view ( how data is pass to view)
		
		@GetMapping("/test2")
		public ModelAndView Test2()
		{
			System.out.println("test page");
			//create modelandview object
			ModelAndView mv=new ModelAndView();
			mv.addObject("name","jay gajera 17");
			mv.addObject("id",40);
			mv.setViewName("test2");
			
			List<Integer> list=new ArrayList<Integer>();
			list.add(10);
			list.add(25);
			mv.addObject("marks",list);
			return mv;
			
			
		}

	@PostMapping("/updateuser")
	public String updateUser(@RequestParam("userid") int userid,
							 @RequestParam("username") String username,
							 @RequestParam("email") String email,
							 @RequestParam("password") String password) {

		User user = userService.getUserById(userid);

		if (user != null) {
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));

			// Сохраняем обновленного пользователя через сервис
			userService.addUser(user);
		}

		// Перенаправляем на страницу профиля
		return "redirect:/profileDisplay";  // обновляем профиль
	}

	@GetMapping("/getTutorInfo/{tutorId}")
	public String getTutorAllInfo(@PathVariable Long tutorId, Model model, HttpServletRequest request) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUserByUsername(username);

		// Получаем tutor по tutorId (или можете использовать user.getId(), если хотите использовать его)
		Tutors tutor = tutorService.getTutorId(Math.toIntExact(tutorId));

		// Если tutor не найден, перенаправляем на страницу ошибки
		if (tutor == null) {
			return "redirect:/error";
		}

		// Добавляем tutor в модель
		model.addAttribute("tutor", tutor);
		model.addAttribute("username", username);

		// Обрабатываем фото преподавателя
		if (tutor.getPhoto() != null) {
			String base64Photo = Base64.getEncoder().encodeToString(tutor.getPhoto());
			model.addAttribute("photo", "data:image/jpeg;base64," + base64Photo);
		} else {
			model.addAttribute("photo", "/static/images/default-avatar.png"); // Заглушка
		}

		List<Map<String, String>> scheduleList = new ArrayList<>();
		List<ScheduleSlot> slots = scheduleService.getSlotsByTutor(tutor);
		model.addAttribute("slots", slots);
		model.addAttribute("schedule", scheduleList);

		// Возвращаем имя представления
		return "tutorAdditionalInfo";
	}

	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestParam("slotId") Long slotId,
									Principal principal) {
		try {
			String username = principal.getName();
			System.out.println("username" + username);
			User user = userService.getUserByUsername(username);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Пользователь не найден"));
			}

			ScheduleSlot slot = scheduleService.getSlotById(slotId);
			if (slot == null || !slot.isAvailable()) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Выбранный слот недоступен"));
			}

			Lesson lesson = new Lesson();
			lesson.setClient(user.getClients());
			lesson.setTeacher(slot.getTutor());
			lesson.setScheduleSlot(slot);
			lesson.setSubject(slot.getTutor().getSubject());
			lesson.setStatus(LessonStatus.SCHEDULED);

			lessonService.addLesson(lesson);
			slot.setAvailable(false);
			scheduleService.saveSlot(slot);

			return ResponseEntity.ok(Map.of(
					"success", "Вы успешно записаны на занятие!",
					"redirect", "/client_lessons"
			));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Ошибка при записи: " + e.getMessage()));
		}
	}
	@GetMapping("/client_lessons")
	public String getClientLessons(Model model, Principal principal, HttpServletRequest request) {
		String username = principal.getName(); // Получаем логин текущего пользователя
		List<Subject> subjects = subjectService.getAllSubjects();
		User user = userService.getUserByUsername(username);
		List<Lesson> lessons = lessonService.findClientLessons(user.getClients().getId());
		model.addAttribute("username", username);
		System.out.println("Предметы: " + subjects);
		model.addAttribute("allSubjects", subjects);
		model.addAttribute("lessons", lessons);
		List<String> photos = new ArrayList<>();
		for (Lesson lesson : lessons) {
			if (lesson.getTeacher().getPhoto() != null) {
				String base64Photo = Base64.getEncoder().encodeToString(lesson.getTeacher().getPhoto());
				photos.add("data:image/jpeg;base64," + base64Photo); // Добавляем строку Base64 в список
			} else {
				photos.add("/static/images/default-avatar.png"); // Добавляем заглушку
			}
		}
		model.addAttribute("photos", photos); // Добавляем список строк с фото в модель
		return "client_lessons";
	}
	}