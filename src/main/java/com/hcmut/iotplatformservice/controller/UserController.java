package com.hcmut.iotplatformservice.controller;

import java.util.List;

import com.hcmut.iotplatformservice.entity.UserEntity;
import com.hcmut.iotplatformservice.model.UserModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController {

	@PostMapping(value = "api/user", produces = "application/json")
	public String createUser(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "username") String username, 
			@RequestParam(value = "password") String password,
			@RequestParam(value = "email") String email, 
			@RequestParam(value = "phone") long phone,
			@RequestParam(value = "birthday") int birthday) {

		Boolean flag = UserModel.getInstance().addUser(name, username, password, email, phone, birthday);

		if (flag) {
			return SUCCESS_MESSAGE;
		}

		return FAIL_MESSAGE;
	}

	@GetMapping(value = "api/user", produces = "application/json")
	public List<UserEntity> readUser() {return UserModel.getInstance().getAllUser();}

	@DeleteMapping(value = "api/user", produces = "application/json")
	public String deleteUser(@RequestParam(value = "id") Integer id) {

		Boolean flag = UserModel.getInstance().deleteUser(id);

		if (flag) {
			return SUCCESS_MESSAGE;
		}

		return FAIL_MESSAGE;
	}
}