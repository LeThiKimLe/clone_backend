package com.example.QuanLyNhaXe.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.QuanLyNhaXe.dto.EditAccountDTO;
import com.example.QuanLyNhaXe.dto.UserDTO;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;
import com.example.QuanLyNhaXe.model.Customer;
import com.example.QuanLyNhaXe.model.Driver;
import com.example.QuanLyNhaXe.model.Staff;
import com.example.QuanLyNhaXe.model.User;
import com.example.QuanLyNhaXe.repository.CustomerRepository;
import com.example.QuanLyNhaXe.repository.DriverRepository;
import com.example.QuanLyNhaXe.repository.StaffRepository;
import com.example.QuanLyNhaXe.repository.UserRepository;
import com.example.QuanLyNhaXe.util.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final StaffRepository staffRepository;
	private final DriverRepository driverRepository;
	private final CustomerRepository customerRepository;
	private final S3Service s3Service;

	private final JwtService jwtService;

	public UserDTO getUserInfor(Integer accountId) {
		User user = userRepository.findByaccountId(accountId)
				.orElseThrow(() -> new NotFoundException(Message.USER_NOT_FOUND));
		return modelMapper.map(user, UserDTO.class);
	}

	public User getUserByAuthorizationHeader(String authorizationHeader) {
		String jwt = authorizationHeader.substring(7);
		Integer userId = Integer.valueOf(jwtService.extractUsername(jwt));
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Message.USER_NOT_FOUND));
	}

	public UserDTO updateInfor(String authorization, EditAccountDTO editAccountDTO) {


		User user = getUserByAuthorizationHeader(authorization);
		Integer roleId = user.getAccount().getRole().getId();

		String image = s3Service.uploadFile(editAccountDTO.getFile());
		switch (roleId) {
		case 1, 2:
			user.setTel(editAccountDTO.getTel());
			user.setGender(editAccountDTO.getGender());
			userRepository.save(user);
			Staff staff = user.getStaff();
			staff.setAddress(editAccountDTO.getAddress());
			if (image != "")
				staff.setImg(image);
			staffRepository.save(staff);
			return modelMapper.map(user, UserDTO.class);

		case 3:
			user.setTel(editAccountDTO.getTel());
			user.setGender(editAccountDTO.getGender());
			userRepository.save(user);
			Driver driver = user.getDriver();
			driver.setAddress(editAccountDTO.getAddress());
			if (image != "")
				driver.setImg(image);
			driverRepository.save(driver);
			return modelMapper.map(user, UserDTO.class);
		case 4:
			user.setName(editAccountDTO.getName());
			user.setEmail(editAccountDTO.getEmail());
			user.setGender(editAccountDTO.getGender());
			userRepository.save(user);
			Customer customer = user.getCustomer();
			if (image != "")
				customer.setImg(image);
			customerRepository.save(customer);
			return modelMapper.map(user, UserDTO.class);
		default:
			String message = "Unauthorized role";
			throw new BadRequestException(message);
		}

	}

}
