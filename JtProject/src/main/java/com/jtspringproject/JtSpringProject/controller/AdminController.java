package com.jtspringproject.JtSpringProject.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import com.jtspringproject.JtSpringProject.dto.userDto;
import com.jtspringproject.JtSpringProject.dto.userpnDto;
import com.jtspringproject.JtSpringProject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.models.Roles;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final userService userService;
	private final tutorService tutorService;
	private final scheduleService scheduleService;
	private final lessonService lessonService;
	private final scheduleService scheduleSlotService;
	private final subjectService subjectService;
	private final clientService clientService;
	private  final paymentService paymentService;
	private final reviewService reviewService;
	private final AnalyticsService analyticsService;

	@Autowired
	public AdminController(userService userService, com.jtspringproject.JtSpringProject.services.tutorService tutorService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleService, com.jtspringproject.JtSpringProject.services.lessonService lessonService, com.jtspringproject.JtSpringProject.services.scheduleService scheduleSlotService, com.jtspringproject.JtSpringProject.services.subjectService subjectService, clientService clientService, com.jtspringproject.JtSpringProject.services.paymentService paymentService, com.jtspringproject.JtSpringProject.services.reviewService reviewService, AnalyticsService analyticsService) {
		this.userService = userService;
        this.tutorService = tutorService;
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
        this.scheduleSlotService = scheduleSlotService;
        this.subjectService = subjectService;
        this.clientService = clientService;
        this.paymentService = paymentService;
        this.reviewService = reviewService;
        this.analyticsService = analyticsService;
    }
	
	@GetMapping("/index")
	public String index(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		return "index";			
	}
	
	@GetMapping("login")
	public ModelAndView adminlogin(@RequestParam(required = false) String error) {
	    ModelAndView mv = new ModelAndView("adminlogin");
	    if ("true".equals(error)) {
	        mv.addObject("msg", "Invalid username or password. Please try again.");
	    }
	    return mv;
	}
	
	@GetMapping( value={"/","Dashboard"})
	public ModelAndView adminHome(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    ModelAndView mv = new ModelAndView("adminHome");
	    mv.addObject("admin", authentication.getName());
	    return mv;
	}
	
	@GetMapping("clients")
	public ModelAndView getCustomerDetail() {
		ModelAndView mView = new ModelAndView("displayClientsForAdmin");
		List<User> users = this.userService.getUsers();
		System.out.println("users: "+ users);
		List<User> clients = users.stream()
				.filter(user -> Roles.CLIENT.equals(user.getRole()))
				.collect(Collectors.toList());
		System.out.println("clients: "+ clients);
		mView.addObject("clients", clients);
		return mView;
	}

	@GetMapping("tutors")
	public ModelAndView getTutorsDetail() {
		ModelAndView mView = new ModelAndView("displayTutorsForAdmin");
		List<User> users = this.userService.getUsers();
		System.out.println("users: "+ users);
		List<User> clients = users.stream()
				.filter(user -> Roles.TEACHER.equals(user.getRole()))
				.collect(Collectors.toList());
		mView.addObject("clients", clients);
		return mView;
	}


	@GetMapping("/getClients")
	@ResponseBody
	public Map<String, Object> getClientsData() {
		List<User> users = this.userService.getUsers();
		System.out.println("users: "+ users);
		List<User> clients = users.stream()
				.filter(user -> Roles.CLIENT.equals(user.getRole()))
				.collect(Collectors.toList());
		System.out.println("clients: "+ clients);
		List<userDto> clientsRes = clients.stream()
				.map(user -> new userDto(
                        (long) user.getId(),
						user.getName(),
						user.getEmail(),
						user.getCreated_at()
				))
				.collect(Collectors.toList());
		return Map.of(
				"success", true,
				"clients", clientsRes,
				"count", clientsRes.size()
		);
	}

	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
		try {
			System.out.println("id: " + id);
			userService.deleteUser(id);
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Клиент успешно удален"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/api/clients/{id}")
	@ResponseBody
	public ResponseEntity<?> getClient(@PathVariable("id") Long id) {
		try {
			System.out.println("Клиент для редактирования с id " + id);
			User client = userService.getUserById(Math.toIntExact(id));
			System.out.println("Клиент для редактирования " + client);
			return ResponseEntity.ok(client);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", e.getMessage()));
		}
	}

	@PutMapping("/api/clients/{id}")
	@ResponseBody
	public ResponseEntity<?> updateClient(
			@PathVariable Long id,
			@Valid @RequestBody userpnDto updateDto) {

		try {
			System.out.println("Клиент на ред " + updateDto.getName());
			User updatedClient = userService.updateClientPartial(id, updateDto);
			System.out.println("Клиент отредактирован " + updatedClient);
			return ResponseEntity.ok(Map.of(
					"success", true,
					"message", "Данные клиента обновлены"
			));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", e.getMessage()));
		}
	}
	
	@GetMapping("profileDisplay")
	public String profileDisplay(Model model) {
		String displayusername,displaypassword,displayemail,displayaddress;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tutorfind","root","1209");
			PreparedStatement stmt = con.prepareStatement("select * from users where username = ?"+";");
			
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			stmt.setString(1, username);
			
			ResultSet rst = stmt.executeQuery();
			
			if(rst.next())
			{
			int userid = rst.getInt(1);
			displayusername = rst.getString(2);
			displayemail = rst.getString(3);
			displaypassword = rst.getString(4);
			model.addAttribute("userid",userid);
			model.addAttribute("username",displayusername);
			model.addAttribute("email",displayemail);
			model.addAttribute("password",displaypassword);
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		System.out.println("Hello");
		return "updateProfile";
	}
	
	@RequestMapping(value = "updateuser",method=RequestMethod.POST)
	public String updateUserProfile(@RequestParam("userid") int userid,@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("address") String address)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tutorfind","root","1209");
			
			PreparedStatement pst = con.prepareStatement("update users set username= ?,email = ?,password= ? where uid = ?;");
			pst.setString(1, username);
			pst.setString(2, email);
			pst.setString(3, password);
			pst.setInt(4, userid);
			int i = pst.executeUpdate();	
			
			Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
		            username,
		            password,
		            SecurityContextHolder.getContext().getAuthentication().getAuthorities());

		    SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		return "redirect:index";
	}
	@GetMapping("/statistics")
	public ModelAndView getStatistics() {
		ModelAndView mView = new ModelAndView("admin_statistics");
		List<User> users = this.userService.getUsers();
		System.out.println("users: "+ users);
		List<User> clients = users.stream()
				.filter(user -> Roles.CLIENT.equals(user.getRole()))
				.collect(Collectors.toList());

		Map<Integer, Long> distribution = reviewService.getRatingDistribution();
		long[] ratingData = new long[5];
		for (int i = 1; i <= 5; i++) {
			ratingData[5-i] = distribution.getOrDefault(i, 0L); // Порядок от 5 до 1 звезды
		}

		Map<String, Long> subjects = analyticsService.getPopularSubjects(10);

		List<String> labels = new ArrayList<>();
		List<Long> values = new ArrayList<>();

		subjects.forEach((key, value) -> {
			labels.add(key);
			values.add(value);
		});

		Map<String, Object> popularSubjects = new HashMap<>();
		popularSubjects.put("labels", labels);
		popularSubjects.put("values", values);

		mView.addObject("clients", clients);
		mView.addObject("totalUsers", userService.getTotalUsers());
		mView.addObject("activeTutors", tutorService.getActiveTutorsCount());
		mView.addObject("avgRating", reviewService.getAverageRating());
		//mView.addObject("avgSessionTime", analyticsService.getAverageSessionTime());
		//mView.addObject("avgLogins", analyticsService.getAverageLoginsPerWeek());
		mView.addObject("userGrowthPercent", analyticsService.getUserGrowthPercent());
		mView.addObject("tutorsWithReviews", reviewService.getTutorsWithReviewsCount());
		mView.addObject("totalReviews", reviewService.getTotalReviewsCount());

		mView.addObject("popularSubjects", popularSubjects);
		mView.addObject("ratingData", ratingData);
		//mView.addObject("activityData", analyticsService.getWeeklyActivity());
		//mView.addObject("registrationData", analyticsService.getRegistrationStats(period));
		//mView.addObject("tutorStats", analyticsService.getTutorDetailedStats());
		return mView;
	}

}
