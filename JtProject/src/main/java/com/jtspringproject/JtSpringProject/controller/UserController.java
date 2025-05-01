package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.EntityNotFoundException;
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

@Controller
public class UserController{

	private final userService userService;
	private final tutorService tutorService;
	private final scheduleService scheduleService;
	private final lessonService lessonService;
	private final scheduleService scheduleSlotService;
	private final subjectService subjectService;
	private final clientService clientService;
	private  final paymentService paymentService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(userService userService, tutorService productService, com.jtspringproject.JtSpringProject.services.tutorService tutorService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.lessonService lessonService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService, com.jtspringproject.JtSpringProject.services.clientService clientService, com.jtspringproject.JtSpringProject.services.paymentService paymentService) {
		this.userService = userService;
        this.tutorService = tutorService;
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
        this.scheduleSlotService = scheduleSlotService;
        this.subjectService = subjectService;
        this.clientService = clientService;
        this.paymentService = paymentService;
    }


	@GetMapping("/register")
	public String registerUser()
	{
		return "register";
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

	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public ModelAndView newUserRegister(@ModelAttribute User user, @RequestParam String role) {
		boolean exists = this.userService.checkUserExists(user.getUsername());
		if (exists) {
			ModelAndView mView = new ModelAndView("register");
			mView.addObject("msg", "Пользователь уже существует.");
			return mView;
		}
		else if (!exists) {
			System.out.println(user.getEmail());
			try {
				user.setRole(Roles.valueOf(role));

			} catch (IllegalArgumentException e) {
				ModelAndView mView = new ModelAndView("register");
				mView.addObject("msg", "Ошибка: выберите корректную роль.");
				return mView;
			}
			user.setCreated_at(LocalDateTime.now());

			// 🔐

			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			this.userService.addUser(user);
			if (user.getRole() == Roles.CLIENT){
				Clients client = new Clients();
				client.setUser(user);
				clientService.addClient(client);
			}
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
			String hashedPassword = passwordEncoder.encode(password);
			user.setPassword(hashedPassword);

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
			User user = userService.getUserByUsername(username);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Пользователь не найден"));
			}

			// Получаем клиента (добавленная проверка)
			Clients client = clientService.findByUser(user)
					.orElseThrow(() -> new EntityNotFoundException("Профиль клиента не найден"));

			ScheduleSlot slot = scheduleService.getSlotById(slotId);
			if (slot == null || !slot.isAvailable()) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Выбранный слот недоступен"));
			}

			BigDecimal lessonPrice = slot.getTutor().getRate();

			// Проверяем баланс клиента
			if (client.getBudget() == null || client.getBudget().compareTo(lessonPrice) < 0) {
				return ResponseEntity.badRequest()
						.body(Map.of("error", "Недостаточно средств на балансе"));
			}

			// Создаем занятие
			Lesson lesson = new Lesson();
			lesson.setClient(client);
			lesson.setTeacher(slot.getTutor());
			lesson.setScheduleSlot(slot);
			lesson.setSubject(slot.getTutor().getSubject());
			lesson.setStatus(LessonStatus.SCHEDULED);

			lesson = lessonService.addLesson(lesson);

			client.setBudget(client.getBudget().subtract(lessonPrice));
			clientService.addClient(client);

			// Создаем запись о платеже
			Payment payment = new Payment();
			payment.setClient(client);
			payment.setLesson(lesson);
			payment.setAmount(lessonPrice.negate()); // Отрицательная сумма для списания
			payment.setPaymentDate(LocalDate.now());
			payment.setStatus(PaymentStatus.COMPLETED);

			paymentService.savePayment(payment);

			// Обновляем слот
			slot.setAvailable(false);
			scheduleService.saveSlot(slot);

			return ResponseEntity.ok(Map.of(
					"success", "Вы успешно записаны на занятие!",
					"redirect", "/client_lessons"
			));

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
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
		model.addAttribute("currentUser", user);
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

	@PostMapping("/createClient")
	@ResponseBody
	public ResponseEntity<?> createClient(@RequestBody User user) {
		try {
			user.setCreated_at(LocalDateTime.now());
			user.setRole(Roles.CLIENT);
			user.setPassword("1111");

			// 🔐 ШИФРОВАНИЕ ПАРОЛЯ
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);

			if (user.getName() == null || user.getName().isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of(
						"success", false,
						"message", "Имя клиента обязательно"
				));
			}

			User newClient = userService.addUser(user);

			return ResponseEntity.ok(Map.of(
					"success", true,
					"message", "Клиент создан",
					"client", newClient
			));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of(
							"success", false,
							"message", e.getMessage()
					));
		}
	}
	}