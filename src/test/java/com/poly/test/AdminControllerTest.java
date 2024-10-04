package com.poly.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.UserResponse;
import com.poly.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private UserService userService;

   private UserRequest userRequest;
   private UserResponse userResponse;

   @BeforeEach
   void initData() {
       userRequest = UserRequest.builder()
               .name("toan")
               .email(null)
               .address(null)
               .username("congtoan")
               .password("123456")
               .roles(Set.of("USER"))
               .build();

       userResponse = UserResponse.builder()
               .id("5b88eb9378cd")
               .name("toan")
               .email("")
               .address("")
               .username("congtoan")
               .build();
   }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_valid() throws Exception {
       // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userRequest);

        Mockito.when(userService.create(ArgumentMatchers.any()))
                        .thenReturn(userResponse);

       // WHEN, THENS
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/user/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("1"));
    }


}
