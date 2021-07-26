package com.poly;

import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.services.RoleService;
import com.poly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ProjectDemoApplication {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectDemoApplication.class, args);
    }

    @PostConstruct
    public void init() {

        List<Role> roleList = (List<Role>) roleService.findAll();
        if (roleList.isEmpty()) {
            Role roleAdmin = new Role();
            roleAdmin.setId(1L);
            roleAdmin.setName("ADMIN");
            roleService.save(roleAdmin);

            Role roleManager = new Role();
            roleManager.setId(2L);
            roleManager.setName("MANAGER");
            roleService.save(roleManager);

            Role roleUser = new Role();
            roleUser.setId(3L);
            roleUser.setName("USER");
            roleService.save(roleUser);
        }

        List<User> users = (List<User>) userService.findAll();
        if (users.isEmpty()) {
            User user = new User();
            Set<Role> roles = new HashSet<>();

            Role roleAdmin = new Role();
            roleAdmin.setId(1L);
            roleAdmin.setName("ADMIN");

            Role roleManager = new Role();
            roleManager.setId(2L);
            roleManager.setName("MANAGER");

            Role roleUser = new Role();
            roleUser.setId(3L);
            roleUser.setName("USER");

            roles.add(roleAdmin);

            user.setUsername("admin");
            user.setPassword("12345678");
            user.setRoles(roles);
            userService.save(user);
        }
    }

}
