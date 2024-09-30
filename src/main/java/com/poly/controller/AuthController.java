package com.poly.controller;

import com.poly.dto.LoginRequest;
import com.poly.dto.SignupRequest;
import com.poly.entity.JwtResponse;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.JwtUtils;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.ex.Utility;
import com.poly.services.AuthService;
import com.poly.exception.ResponseUtils;
import com.poly.services.UserService;
import net.bytebuddy.utility.RandomString;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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
    private ModelMapperConfig mapper;
    @Autowired
    private JavaMailSender mailSender;

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
    public Object signup(@RequestBody SignupRequest request, HttpServletRequest servletRequest) throws Exception {
        if (request.getUsername().length() < 6)
            throw new Exception("");
        if (request.getPassword().length() < 6)
            throw new Exception("");
        if (!validate(request.getEmail()))
            throw new Exception("");

        if (userService.getByUsername(request.getUsername()) != null)
            return new ResponseEntity<>(null, "Username is already exists!", HttpStatus.BAD_REQUEST);

        User user = mapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerifyCode(RandomString.make(64));
        // role
        Role roleUser = Role.builder().id(3L).build();
        user.getRoles().add(roleUser);
        // image
        if (user.getImage() == null) {
            user.setImage(StringContent.avatar_default);
        }
        User response = userService.save(user);

//        String siteURL = Utility.getSiteURL(servletRequest);
//        // send code
//        try {
//            sendVerificationEmail(request, response, siteURL);
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
        return response;
    }

//    @GetMapping("/verify")
//    public Object verifyAccount(@RequestParam("code") String code) {
//        User user = userService.getByVerifyCode(code);
//        if (user.isEnabled()) {
//            return true;
//        }
//
//        userService.findByVerifyCodeAndEnable(code);
//        return new ResponseEntity<>(null, "Verify success!", HttpStatus.OK);
//    }
//
//    private void sendVerificationEmail(SignupRequest request, User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        String subject = "Kích hoạt tài khoản của bạn";
//        String senderMail = "congtoan837@gmail.com";
//        String senderName = "E-Commerce";
//        String mailContent = "<p>Xin chào " + request.getName() + ",</p>";
//        mailContent += "<p>Để kích hoạt tài khoản, bạn vui lòng nhấp vào link dưới đây:</p>";
//        mailContent += "<p>" + siteURL + "/verify?code=" + user.getVerifyCode() + "</p>";
//        mailContent += "<p>Cám ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
//
//        message.setContent(mailContent, "text/html; charset=utf-8");
//
//        helper.setTo(request.getEmail());
//        helper.setFrom(senderMail, senderName);
//        helper.setSubject(subject);
//
//        this.mailSender.send(message);
//    }
}
