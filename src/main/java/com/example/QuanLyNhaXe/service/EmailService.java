package com.example.QuanLyNhaXe.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.sun.mail.smtp.SMTPAddressFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.QuanLyNhaXe.dto.BookingDTO;
import com.example.QuanLyNhaXe.dto.CreateBookingDTO;
import com.example.QuanLyNhaXe.model.Booking;
import com.example.QuanLyNhaXe.model.Ticket;
import com.example.QuanLyNhaXe.repository.BookingRepository;
import com.example.QuanLyNhaXe.util.Message;
import com.google.api.services.gmail.Gmail.Users.Drafts.List;
import com.example.QuanLyNhaXe.exception.BadRequestException;
import com.example.QuanLyNhaXe.exception.NotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import com.google.api.services.gmail.model.Draft;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	private final BookingRepository bookingRepository;

	@Value("${spring.mail.username}")
	private String emailFrom;

	private void sendEmail(String email, String subject, String content) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom(emailFrom, "Đặt vé thành công");
			helper.setTo(email);

			helper.setSubject(subject);
			helper.setText(content, true);

			javaMailSender.send(message);
		} catch (MessagingException | UnsupportedEncodingException  e) {
			throw new BadRequestException("Lỗi khi gửi email");
		}
	}

	public  boolean checkEmail(String email) {
	        Map<String, String> parameters = new HashMap<>();
	        parameters.put("email", email);

	        String requestBody = mapToFormData(parameters);

	        HttpRequest request = null;
	        try {
	            String emailCheckURI = "https://melink.vn/checkmail/checkemail.php";
	            request = HttpRequest.newBuilder()
	                    .uri(new URI(emailCheckURI))
	                    .header("Content-Type", "application/x-www-form-urlencoded")
	                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                    .build();
	        } catch (URISyntaxException e) {
	            throw new RuntimeException(e);
	        }

	        HttpClient client = HttpClient.newHttpClient();
	        try {
	            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	            String responseBody = response.body();
	            return responseBody.equals("<span style='color:green'><b>Valid!</b>");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	}

	private static String mapToFormData(Map<String, String> parameters) {
		StringBuilder formData = new StringBuilder();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			if (formData.length() > 0) {
				formData.append("&");
			}
			formData.append(entry.getKey());
			formData.append("=");
			formData.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
		}
		return formData.toString();
	}

	public void sendBookingInformation(Booking booking) {

		String subject = "Thông tin đặt vé";

		String emailContent = """
				Dear %s,<br><br>
				Thank you for your booking. Here are the details:<br>
				- Thông tin đặt vé<br>
				Mã đặt vé: %s<br>
				Thời gian: %s<br>
				Số lượng vé: %d<br>

				Số điện thoại: %s<br>
				-

				If you have any further questions, please feel free to contact us.
				Best regards,
				Your Booking Team""".formatted(booking.getName(), booking.getCode(), booking.getBookingDate(),
				booking.getTicketNumber(), booking.getTel());

		sendEmail(booking.getEmail(), subject, emailContent);
	}

}
