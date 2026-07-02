package io.jpass.auth.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jpass.auth.domain.model.RoleName;
import io.jpass.auth.infrastructure.entities.RoleEntity;
import io.jpass.auth.infrastructure.entities.UserEntity;
import io.jpass.auth.infrastructure.repositories.RoleJpaRepository;
import io.jpass.auth.infrastructure.repositories.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    private static final String EMAIL = "admin@jpass.test";
    private static final String PASSWORD = "correct-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();
        roleJpaRepository.deleteAll();

        RoleEntity role = new RoleEntity();
        role.setName(RoleName.USER);
        RoleEntity savedRole = roleJpaRepository.save(role);

        UserEntity user = new UserEntity();
        user.setEmail(EMAIL);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        user.setRoles(List.of(savedRole));
        userJpaRepository.save(user);
    }

    @Test
    void loginReturnsJwtTokenForValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", EMAIL,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())));
    }

    @Test
    void loginRejectsInvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", EMAIL,
                                "password", "wrong-password"
                        ))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtTokenAuthenticatesProtectedRequests() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", EMAIL,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/test/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("authenticated"));
    }

    @Test
    void registerCreatesUserAndReturnsJwtToken() throws Exception {
        String email = "new-user@jpass.test";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", not(blankOrNullString())));

        UserEntity user = userJpaRepository.findByEmail(email).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(passwordEncoder.matches(PASSWORD, user.getPasswordHash())).isTrue();
        org.assertj.core.api.Assertions.assertThat(user.getRoles())
                .extracting(RoleEntity::getName)
                .containsExactly(RoleName.USER);
    }

    @Test
    void registerRejectsDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", EMAIL,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isConflict());
    }

    @Test
    void registeredUserCanLogin() throws Exception {
        String email = "login-after-register@jpass.test";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", PASSWORD
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())));
    }

    @Test
    void openApiDocsDoNotRequireAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class ProtectedEndpointConfiguration {

        @Bean
        ProtectedEndpointController protectedEndpointController() {
            return new ProtectedEndpointController();
        }
    }

    @RestController
    static class ProtectedEndpointController {

        @GetMapping("/test/protected")
        String protectedEndpoint() {
            return "authenticated";
        }
    }
}
