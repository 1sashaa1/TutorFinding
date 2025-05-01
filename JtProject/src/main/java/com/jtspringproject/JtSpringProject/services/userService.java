package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dto.userpnDto;
import com.jtspringproject.JtSpringProject.models.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jtspringproject.JtSpringProject.dao.userDao;
import com.jtspringproject.JtSpringProject.models.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class userService {
	@Autowired
	private userDao userDao;
	@Autowired
	private final PasswordEncoder passwordEncoder;

    public userService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
		return this.userDao.getAllUser();
	}

	public User getUser(int id){
		return this.userDao.getUser(id);
	}
	
	public User addUser(User user) {
		try {
			return this.userDao.saveUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("Add user error");
		}
	}

	public User updateClientPartial(Long id, userpnDto updateDto){
		User client = userDao.getUserById(Math.toIntExact(id));

		// Обновляем только разрешенные поля
		if (updateDto.getName() != null) {
			client.setName(updateDto.getName());
		}
		if (updateDto.getEmail() != null) {
			client.setEmail(updateDto.getEmail());
		}

		return userDao.saveUser(client);
	}

	public void deleteUser(int id) {
		try {
			User user = userDao.getUser(id);
			userDao.deleteUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("Delete user error: " + e.getMessage(), e);
		}
	}
	
	public User checkLogin(String username,String password) {
		return this.userDao.getUser(username, password);
	}

	public boolean checkUserExists(String username) {
		return this.userDao.userExists(username);
	}

	public User getUserByUsername(String username) {
	        return this.userDao.getUserByUsername(username);
	    }

	public User getUserById(int id) {
		return userDao.getUserById(id);
	}
}
