package br.com.battista.arcadia.caller.controller;

import static br.com.battista.arcadia.caller.constants.EntityConstant.DEFAULT_VERSION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.battista.arcadia.caller.config.AppConfig;
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.AuthenticationException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserControllerTest extends BaseControllerConfig {

    private final String mail = "teste@teste.com";
    private final String username = "teste";

    @Autowired
    private UserController userController;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Test
    public void shouldReturnExceptionWhenInvalidProfileToActionSave() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        userController.save("abc", null);
    }

    @Test
    public void shouldReturnBadRequestWhenUserNullToActionSave() throws AuthenticationException {
        ResponseEntity<User> responseEntity = userController.save(ProfileAppConstant.ADMIN.name(), null);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnExceptionWhenInvalidUserToActionSave() throws AuthenticationException {
        rule.expect(ValidatorException.class);

        User user = User.builder().username(username).build();
        userController.save(ProfileAppConstant.ADMIN.name(), user);
    }

    @Test
    public void shouldReturnSuccessWhenValidUserToActionSave() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).build();

        ResponseEntity<User> responseEntity = userController.save(ProfileAppConstant.ADMIN.name(), user);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        User body = responseEntity.getBody();
        assertNotNull(body);
        assertNotNull(body.getPk());
        assertThat(body.getUsername(), equalTo(username));
        assertThat(body.getMail(), equalTo(mail));
        assertThat(body.getVersion(), equalTo(DEFAULT_VERSION));
    }

    @Test
    public void shouldReturnExceptionWhenInvalidProfileToActionGetAll() throws AuthenticationException {
        rule.expect(AuthenticationException.class);

        userController.getAll("abc");
    }

    @Test
    public void shouldReturnNotContentWhenNoUsersFounds() throws AuthenticationException {
        ResponseEntity<List<User>> responseEntity = userController.getAll(ProfileAppConstant.ADMIN.name());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnSuccessWhenExistsUserToActionGetAll() throws AuthenticationException {
        User user = User.builder().username(username).mail(mail).build();

        userController.save(ProfileAppConstant.ADMIN.name(), user);

        ResponseEntity<List<User>> responseEntity = userController.getAll(ProfileAppConstant.ADMIN.name());

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        List<User> body = responseEntity.getBody();
        assertNotNull(body);
        assertThat(body, hasSize(1));
        assertThat(body, hasItem(user));
    }

}