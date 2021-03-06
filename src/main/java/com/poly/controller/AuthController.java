package com.poly.controller;

import com.poly.dto.LoginRequest;
import com.poly.dto.SignupRequest;
import com.poly.entity.JwtResponse;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.JwtUtils;
import com.poly.ex.Utility;
import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender mailSender;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            AuthService userDetails = (AuthService) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return getResponseEntity(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles), "Login success!", HttpStatus.OK);
        } catch (Exception e) {
            return getResponseEntity(null, "Login fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletRequest servletRequest) {
        try {
            if (userService.getByUsername(request.getUsername()) != null) {
                return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
            } else if (request.getUsername().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (request.getPassword().length() < 6) {
                return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
            } else if (!validate(request.getEmail())){
                return responseUtils.getResponseEntity(null, "-1", "Email is not in the correct formatting!", HttpStatus.BAD_REQUEST);
            } else {
                User user = mapper.map(request, User.class);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setVerifyCode(RandomString.make(64));

                Set<Role> roles = new HashSet<>();
                Role role = new Role();
                role.setId(3L);
                roles.add(role);

                user.setRoles(roles);
                if (user.getImage() == null) {
                    user.setImage("https://congtoan-bucket.s3.ap-southeast-1.amazonaws.com/1630749704358-avatar.png");
                }
                User response = userService.save(user);

                String siteURL = Utility.getSiteURL(servletRequest);

                try {
                    sendVerificationEmail(request, response, siteURL);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return responseUtils.getResponseEntity("1", "Create user success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity(null, "-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("code") String code) {
        try {
            User user = userService.getByVerifyCode(code);
            if (!user.isEnabled()) {
                userService.findByVerifyCodeAndEnable(code);
                user.setVerifyCode(null);
                return responseUtils.getResponseEntity(HttpStatus.OK);
            } else {
                return responseUtils.getResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private void sendVerificationEmail(SignupRequest request, User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "K??ch ho???t t??i kho???n c???a b???n";
        String senderMail = "congtoan837@gmail.com";
        String senderName = "E-Commerce";
        String mailContent = "<p>Xin ch??o " + request.getName() + ",</p>";
        mailContent += "<p>????? k??ch ho???t t??i kho???n, b???n vui l??ng nh???p v??o link d?????i ????y:</p>";
        mailContent += "<p>" + siteURL + "/verify?code=" + user.getVerifyCode() + "</p>";
        mailContent += "<p>C??m ??n b???n ???? s??? d???ng d???ch v??? c???a ch??ng t??i.</p>";

        message.setContent(mailContent, "text/html; charset=utf-8");

        helper.setTo(request.getEmail());
        helper.setFrom(senderMail, senderName);
        helper.setSubject(subject);

        this.mailSender.send(message);
    }

    private ResponseEntity<?> getResponseEntity(Object data, String mess, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("Data", data);
        response.put("Status", status);
        response.put("Messenger", mess);
        return new ResponseEntity<>(response, status);
    }
}
