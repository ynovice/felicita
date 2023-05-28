package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.Application;
import com.github.ynovice.felicita.AuthenticationHelper;
import com.github.ynovice.felicita.model.entity.AuthServer;
import com.github.ynovice.felicita.model.entity.OAuth2Credential;
import com.github.ynovice.felicita.model.entity.Role;
import com.github.ynovice.felicita.model.entity.User;
import com.github.ynovice.felicita.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class
)
@TestPropertySource("/application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void registerIfNotRegistered_givenNotRegistered_thenRegister() {

        OAuth2User oAuth2User = getOAuth2User();

        User registeredUser = userService.registerIfNotRegistered(oAuth2User);

        assertEquals(oAuth2User.getAttribute("name"), registeredUser.getUsername());
        assertNotNull(registeredUser.getId());
        assertNotNull(registeredUser.getRole());

        OAuth2Credential registeredOAuth2Credential = registeredUser.getOAuth2Credentials().get(0);

        assertEquals(oAuth2User.getName(), registeredOAuth2Credential.getExternalId());
        assertEquals(AuthServer.GOOGLE, registeredOAuth2Credential.getAuthServer());

        assertNotNull(registeredOAuth2Credential.getPresentation());
        assertNotNull(registeredOAuth2Credential.getCreatedAt());

        assertTrue(userRepository.existsById(registeredUser.getId()));
        assertTrue(userRepository.existsByUsername(registeredUser.getUsername()));
    }

    @Test
    public void registerIfNotRegistered_givenRegistered_thenReturnFromDatabase() {

        OAuth2User oAuth2User = getOAuth2User();

        User registeredUser = userService.registerIfNotRegistered(oAuth2User);

        User onceAgainRegisteredUser = userService.registerIfNotRegistered(oAuth2User);

        assertEquals(registeredUser, onceAgainRegisteredUser);
    }

    private OAuth2User getOAuth2User() {

         return authenticationHelper.createOAuth2User(
                "1",
                "Default User 1",
                "default123user1902348@gmail.com",
                Role.USER
        );
    }
}
