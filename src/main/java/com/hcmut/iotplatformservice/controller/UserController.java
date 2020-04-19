package com.hcmut.iotplatformservice.controller;

import com.hcmut.iotplatformservice.model.UserModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Template controller
@RestController
public class UserController {

	// CREATE
	@PostMapping(value = "api/user")
	public String createUser(@RequestParam(value = "name") String name, @RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "email") String email,
			@RequestParam(value = "phone") String phone, @RequestParam(value = "birthday") int birthday) {

		String message = UserModel.getInstance().addUser(name, username, password, email, phone, birthday);
		return message;
	}

	// READ
	@GetMapping(value = "api/user", produces = "application/json")
	public String readUser(@RequestParam(value = "id") int id) {

		if (id == 0) {
			return UserModel.getInstance().getAllUser();
		}

		return UserModel.getInstance().getUserById(id);
	}

	// DETELE
	@DeleteMapping(value = "api/user")
	public String deleteUser(@RequestParam(value = "id") int id) {

		String message = UserModel.getInstance().deleteUser(id);
		return message;
	}
}