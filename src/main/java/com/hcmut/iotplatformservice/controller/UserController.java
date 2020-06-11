package com.hcmut.iotplatformservice.controller;

import com.hcmut.iotplatformservice.model.UserModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// User controller
@RestController
public class UserController {

	// CREATE
	@PostMapping(value = "api/user")
	public String createUser(@RequestParam(value = "name") String name, @RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "email") String email,
			@RequestParam(value = "phone") String phone, @RequestParam(value = "birthday") int birthday) {
		
		return UserModel.getInstance().addUser(name, username, password, email, phone, birthday);
	}

	// READ
	@GetMapping(value = "api/user", produces = "application/json")
	public String readUser(@RequestParam(value = "ids") int[] ids) {

		return UserModel.getInstance().getUserById(ids);
	}

	// DELETE
	@DeleteMapping(value = "api/user")
	public String deleteUser(@RequestParam(value = "ids") int[] ids) {
		
		return UserModel.getInstance().deleteUser(ids);
	}
}