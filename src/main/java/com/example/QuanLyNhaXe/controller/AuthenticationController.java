package com.example.QuanLyNhaXe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.QuanLyNhaXe.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.QuanLyNhaXe.dto.SignupStaffDTO;
import com.example.QuanLyNhaXe.dto.ChangePasswordDTO;
import com.example.QuanLyNhaXe.dto.LoginDTO;
import com.example.QuanLyNhaXe.dto.SignupDTO;
import com.example.QuanLyNhaXe.dto.SignupDriverDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth Controller")

public class AuthenticationController {
	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
		return new ResponseEntity<>(authenticationService.login(loginDTO), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<Object> registerCustomer(@RequestBody SignupDTO signupDTO) {
		return new ResponseEntity<>(authenticationService.registerCustomer(signupDTO), HttpStatus.CREATED);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/signup/staff")
	public ResponseEntity<Object> createStaff(@RequestBody SignupStaffDTO signupStaffDTO) {
		return new ResponseEntity<>(authenticationService.createStaff(signupStaffDTO), HttpStatus.CREATED);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/signup/driver")
	public ResponseEntity<Object> createDriver(@RequestBody SignupDriverDTO signupDriverDTO) {
		return new ResponseEntity<>(authenticationService.createDriver(signupDriverDTO), HttpStatus.CREATED);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/refresh-token")
	public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
		Object result = authenticationService.refreshToken(request);
		if (result.equals("Token has expired"))
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/logout")
	public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
		return new ResponseEntity<>(authenticationService.logout(request, response), HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/password-change")
	public ResponseEntity<Object> createDriver(@RequestBody ChangePasswordDTO changePasswordDTO,
			@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		return new ResponseEntity<>(authenticationService.changePassword(changePasswordDTO, authorization),
				HttpStatus.OK);
	}

}
