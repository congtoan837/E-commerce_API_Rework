package com.poly.controller;

import com.poly.entity.*;

import com.poly.services.AuthService;
import com.poly.services.ResponseUtils;
import com.poly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    UserService userService;

    @Autowired
    public JavaMailSender emailSender;

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    Path staticPath = Paths.get("src/main/resources/static");

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(@RequestParam int page, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                Page<User> users = userService.findAll(PageRequest.of(page, 5, Sort.by("roles").ascending()));
                return responseUtils.getResponseEntity(users, "1", "Get user success!", HttpStatus.OK);
            }else {
                return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return responseUtils.getResponseEntity(null, "-1", "Get user fail!", HttpStatus.OK);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                if (userService.getByUsername(user.getUsername()) != null) {
                    return responseUtils.getResponseEntity(null, "-1", "Username is already exists!", HttpStatus.BAD_REQUEST);
                } else if (user.getPassword().length() < 6) {
                    return responseUtils.getResponseEntity(null, "-1", "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST);
                } else if (user.getPhone().length() < 10) {
                    return responseUtils.getResponseEntity(null, "-1", "Number phone must be at 11 digit!", HttpStatus.BAD_REQUEST);
                } else {
                    userService.save(user);
                    return responseUtils.getResponseEntity(user, "1", "Create user success!", HttpStatus.OK);
                }
            }else {
                return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return responseUtils.getResponseEntity(null, "-1", "Create user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody(required = false) User user, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                Long id = user.getId();
                Optional<User> getUser = userService.findById(id).map(myUser -> {
                    myUser.setName(user.getName());
                    myUser.setEmail(user.getEmail().trim());
                    myUser.setPhone(user.getPhone().trim());
                    myUser.setPassword(user.getPassword().trim());
                    myUser.setAddress(user.getAddress());
                    myUser.setStatus(user.isStatus());
                    myUser.setUsername(user.getUsername().trim());
                    if (user.getImage().equals("")) {
                        myUser.setImage(user.getImage());
                    }
                    if (user.getRoles().size() > 0) {
                        myUser.setRoles(user.getRoles());
                    }
                    return userService.save(myUser);
                });
                if (getUser.isEmpty()) {
                    return responseUtils.getResponseEntity(null, "-1", "Update user fail!", HttpStatus.BAD_REQUEST);
                }
                return responseUtils.getResponseEntity(getUser, "1", "Update user success!", HttpStatus.OK);
            }else {
                return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return responseUtils.getResponseEntity(null, "-1", "Update user fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        try {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
                if(userService.findById(id).isEmpty()) {
                    return responseUtils.getResponseEntity("-1", "User "+ id +" not found!", HttpStatus.BAD_REQUEST);
                } else {
                    userService.deleteById(id);
                    return responseUtils.getResponseEntity("1", "Delete user success!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return responseUtils.getResponseEntity("-1", "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@ModelAttribute User user, @ModelAttribute MultipartFile file,
                                    Authentication authentication) {
        AuthService authService = (AuthService) authentication.getPrincipal();
        Path userPath = Paths.get(authService.getUsername());
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(userPath))) {
            try {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(userPath));
            } catch (IOException e) {

            }
        }
        Path path = CURRENT_FOLDER.resolve(staticPath).resolve(userPath).resolve(file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(path)) {
            os.write(file.getBytes());
        } catch (IOException e) {

        }
        user.setImage(staticPath.resolve(file.getOriginalFilename()).toString());
        return responseUtils.getResponseEntity(user, "-1", "Upload success!", HttpStatus.OK);
    }

    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(@RequestPart String image, Authentication authentication) throws IOException {
        try {
            AuthService authService = (AuthService) authentication.getPrincipal();

            File file = ResourceUtils.getFile("classpath:static/"+authService.getUsername()+"/"+image);

            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

            Map<String, String> jsonMap = new HashMap<>();

            jsonMap.put("content", encodeImage);

            return responseUtils.getResponseEntity(jsonMap, "1", "Get image success!", HttpStatus.OK);

        }catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get image fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/mail")
    public ResponseEntity<?> mail() {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            boolean multipart = true;

            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

            String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                    + "<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

            message.setContent(htmlMsg, "text/html");

            helper.setTo("toanlcps10057@fpt.edu.vn");

            helper.setSubject("Test send HTML email");

            // Send Message!
            this.emailSender.send(message);
            return responseUtils.getResponseEntity("1", "Sending mail success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error sending mail!", HttpStatus.BAD_REQUEST);
        }
    }
}
