package com.example.QuanLyNhaXe.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.QuanLyNhaXe.dto.SignupStaffDTO;
import com.example.QuanLyNhaXe.dto.AccountDTO;
import com.example.QuanLyNhaXe.dto.ChangePasswordDTO;
import com.example.QuanLyNhaXe.dto.LoginDTO;
import com.example.QuanLyNhaXe.model.Account;

import com.example.QuanLyNhaXe.model.Customer;
import com.example.QuanLyNhaXe.model.Driver;
import com.example.QuanLyNhaXe.model.Role;
import com.example.QuanLyNhaXe.model.Staff;
import com.example.QuanLyNhaXe.model.User;
import com.example.QuanLyNhaXe.repository.AccountRepository;
import com.example.QuanLyNhaXe.repository.CustomerRepository;
import com.example.QuanLyNhaXe.repository.DriverRepository;
import com.example.QuanLyNhaXe.repository.StaffRepository;
import com.example.QuanLyNhaXe.repository.UserRepository;
import com.example.QuanLyNhaXe.util.Message;
import com.example.QuanLyNhaXe.dto.SignupDTO;
import com.example.QuanLyNhaXe.dto.SignupDriverDTO;
import com.example.QuanLyNhaXe.exception.ConflictException;
import com.example.QuanLyNhaXe.util.ResponseMessage;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import com.example.QuanLyNhaXe.dto.TokenDTO;
import com.example.QuanLyNhaXe.dto.UserDTO;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final AuthenticationManager authenticationManager;
	private final AccountRepository accountRepository;
	private final JwtService jwtService;
	private final UserService userService;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final StaffRepository staffRepository;
	private final DriverRepository driverRepository;
	private static final String DEFAULT_TYPE_AUTHENTICATION = "Bearer ";
	private static final String DEFAULT_IMG = "https://bucketsaveimage.s3.amazonaws.com/Image/1697883009849-images.png";

	public TokenDTO login(LoginDTO loginDTO) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
		Account account = accountRepository.findByUsername(loginDTO.getUsername())
				.orElseThrow(() -> new NotFoundException(Message.ACCOUNT_NOT_FOUND));
		if (!account.isActive())
			throw new BadRequestException("Tài khoản đã bị vô hiệu hóa");

		UserDTO userDTO = userService.getUserInfor(account.getId());
		String accessToken = jwtService.generateToken(account);
		String refreshToken = jwtService.generateRefreshToken(account);
		account.setRefreshToken(refreshToken);
		accountRepository.save(account);
		return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).user(userDTO).build();
	}

	public User createUser(SignupDTO signupDTO, Integer roleId, String userName) {
		boolean checkExist = accountRepository.existsByUsername(userName);
		if (checkExist) {
			throw new ConflictException("Tài khoản đã tồn tại trong hệ thống");

		}
		Role role = new Role();
		role.setId(roleId);
		Account account = Account.builder().username(userName).password(passwordEncoder.encode(signupDTO.getPassword()))
				.isActive(true).role(role).build();
		return User.builder().name(signupDTO.getName()).email(signupDTO.getEmail()).tel(signupDTO.getTel())
				.gender(signupDTO.getGender()).account(account).build();

	}

	@Transactional
	public ResponseMessage registerCustomer(SignupDTO signupDTO) {
		String userName = signupDTO.getTel();
		User user = createUser(signupDTO, 4, userName);
		Customer customer = Customer.builder().user(user).img(DEFAULT_IMG).build();

		try {
			accountRepository.save(user.getAccount());
			userRepository.save(user);
			customerRepository.save(customer);

		} catch (DataAccessException e) {
			return new ResponseMessage(Message.INACCURATE_DATA);
		}
		return new ResponseMessage(Message.SUCCESS_REGISTER);

	}

	@Transactional
	public ResponseMessage createStaff(SignupStaffDTO signupStaffDTO) {

		SignupDTO signupDTO = SignupDTO.builder().email(signupStaffDTO.getEmail()).tel(signupStaffDTO.getTel())
				.name(signupStaffDTO.getName()).gender(signupStaffDTO.getGender()).password("@123").build();
		String userName = signupStaffDTO.getEmail();
		User user = createUser(signupDTO, 2, userName);
		boolean checkExist2 = staffRepository.existsByIdCard(signupStaffDTO.getIdCard());
		if (checkExist2) {
			throw new ConflictException("Nhân viên đã tồn tại trong hệ thống");
		}
		Staff staff = Staff.builder().address(signupStaffDTO.getAddress())
				.beginWorkDate(signupStaffDTO.getBeginWorkDate()).idCard(signupStaffDTO.getIdCard()).img(DEFAULT_IMG)
				.user(user).build();
		try {
			accountRepository.save(user.getAccount());
			userRepository.save(user);
			staffRepository.save(staff);

		} catch (DataAccessException e) {
			return new ResponseMessage(Message.INACCURATE_DATA);
		}
		return new ResponseMessage(Message.SUCCESS_ADD_STAFF);
	}

	@Transactional
	public ResponseMessage createDriver(SignupDriverDTO signupDriverDTO) {

		SignupDTO signupDTO = SignupDTO.builder().email(signupDriverDTO.getEmail()).tel(signupDriverDTO.getTel())
				.name(signupDriverDTO.getName()).gender(signupDriverDTO.getGender()).password("@123456").build();
		String userName = signupDriverDTO.getEmail();
		User user = createUser(signupDTO, 3, userName);
		boolean checkExistIdCard = driverRepository.existsByIdCard(signupDriverDTO.getIdCard());
		if (checkExistIdCard) {
			throw new ConflictException("Tài xế đã tồn tại trong hệ thống");
		}
		boolean checkExistIssueDate = driverRepository.existsByLicenseNumber(signupDriverDTO.getIdCard());
		if (checkExistIssueDate) {
			throw new ConflictException("Số bằng lái tồn tại trong hệ thống");
		}
		Driver driver = Driver.builder().address(signupDriverDTO.getAddress())
				.beginWorkDate(signupDriverDTO.getBeginWorkDate()).idCard(signupDriverDTO.getIdCard()).img(DEFAULT_IMG)
				.licenseNumber(signupDriverDTO.getLicenseNumber()).issueDate(signupDriverDTO.getIssueDate()).user(user)
				.build();
		try {
			accountRepository.save(user.getAccount());
			userRepository.save(user);
			driverRepository.save(driver);

		} catch (DataAccessException e) {
			return new ResponseMessage(Message.INACCURATE_DATA);
		}
		return new ResponseMessage(Message.SUCCESS_ADD_DRIVER);
	}

	public Object refreshToken(HttpServletRequest request) {
		final Integer userId;
		final Date timeExiration;
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith(DEFAULT_TYPE_AUTHENTICATION)) {
			throw new BadRequestException("No token to refresh");
		}
		String refreshToken = authHeader.substring(7);
		try {
			userId = Integer.valueOf(jwtService.extractUsername(refreshToken));
			timeExiration = jwtService.extractExiration(refreshToken);
		} catch (ExpiredJwtException e) {
			return "Token has expired";
		}
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Message.USER_NOT_FOUND));

		if (!refreshToken.equals(user.getAccount().getRefreshToken()))
			throw new BadRequestException("Invalid token refresh");
		if (jwtService.isTokenValid(refreshToken, user.getAccount())) {
			String accessToken = jwtService.generateToken(user.getAccount());
			String newRefreshToken = jwtService.generateNewRefreshToken(timeExiration, user.getAccount());
			Account account = user.getAccount();
			account.setRefreshToken(newRefreshToken);
			accountRepository.save(account);
			return TokenDTO.builder().accessToken(accessToken).refreshToken(newRefreshToken).build();
		} else {
			throw new BadRequestException("Invalid token refresh");
		}
	}

	public ResponseMessage logout(HttpServletRequest request, HttpServletResponse response) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith(DEFAULT_TYPE_AUTHENTICATION)) {
			throw new BadRequestException(Message.USER_NOT_FOUND);
		}
		String jwt = authHeader.substring(7);
		Integer userId = Integer.valueOf(jwtService.extractUsername(jwt));
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Message.USER_NOT_FOUND));
		Account account = user.getAccount();
		if (account.getRefreshToken() != null) {
			account.setRefreshToken(null);
			accountRepository.save(account);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		} else
			throw new NotFoundException("Không tìm thấy Refresh Token");
		return new ResponseMessage("Đăng xuất thành công");
	}

	public ResponseMessage changePassword(ChangePasswordDTO changePasswordDTO, String authorization) {

		String jwt = authorization.substring(7);
		Integer userId = Integer.valueOf(jwtService.extractUsername(jwt));
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Message.USER_NOT_FOUND));
		Account account = user.getAccount();
		if (changePasswordDTO.getOldPassword() == null || changePasswordDTO.getNewPassword() == null
				|| changePasswordDTO.getOldPassword().isEmpty() || changePasswordDTO.getNewPassword().isEmpty()) {
			return new ResponseMessage("Mật khẩu không được để trống");
		} else {
			if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), account.getPassword())) {
				account.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
				accountRepository.save(account);
				return new ResponseMessage("Đổi mật khẩu thành công");
			}
			throw new NotFoundException("Mật khẩu cũ không đúng");
		}

	}

}
