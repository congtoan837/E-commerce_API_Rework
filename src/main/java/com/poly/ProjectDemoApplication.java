package com.poly;

import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.ERole;
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

        List<Role> roleList = roleService.findAll();
        if (roleList.isEmpty()) {
            for (ERole roleName : ERole.values()) {
                Role role = new Role();
                role.setName(roleName);
                roleService.save(role);
            }
        }

        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            User user = new User();
            Set<Role> roles = new HashSet<>();

            Role roleAdmin = new Role();
            roleAdmin.setId(1L);

            roles.add(roleAdmin);

            user.setUsername("admin");
            user.setPassword("$2a$10$mCGfUjNS3bbojpVORiFw7OxhMBvVXCoueBcnWsD7ALCkQ1WsHfahy");
            user.setRoles(roles);
            userService.save(user);
        }
    }

}
