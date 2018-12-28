package com.concrete.spring;

import com.concrete.spring.dao.UserDAO;
import com.concrete.spring.domain.User;
import com.concrete.spring.exception.InsertException;
import com.concrete.spring.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

//http://www.vogella.com/tutorials/JUnit/article.html

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	UserService userService;

//	@Test
//	public void contextLoads() {
//		userDAO.createSingleUser();
//	}

	//cadastro

	@Test
	public void userShouldNull(){
		try {
			User userSigned=userDAO.signInUser(new User());
			Assert.assertNull(userSigned);
		} catch (InsertException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void emailAlreadyExistsShouldAppear(){
		User user=userDAO.createSingleUser();
		ResponseEntity responseEntity=userService.register(user);
		Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
	}

	@Test
	public void userShouldRegister(){
		User user=userDAO.createSingleUser();
		Assert.assertNotNull(user.getCreated());
		Assert.assertNotNull(user.getLastLogin());
		Assert.assertNotNull(user.getModified());
		Assert.assertNotNull(user.getToken());
	}

	//login
	@Test
	public void userShouldLogin(){
		User user=userDAO.createSingleUser();
		ResponseEntity responseEntity=userService.login(user);
		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void emailNotFoundShouldAppear(){
		User user=userDAO.createSingleUser();
		user.setEmail("aojkbgaijlegbuoieagf8yqa9");
		ResponseEntity responseEntity=userService.login(user);
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void InvalidUserPasswordShouldAppear(){
		User user=userDAO.createSingleUser();
		user.setPassword(user.getPassword()+"aojkbgaijlegbuoieagf8yqa9");
		ResponseEntity responseEntity=userService.login(user);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void userLoginShouldNull(){
		ResponseEntity responseEntity=userService.login(null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	//profile
	@Test
	public void nullToken(){
		ResponseEntity responseEntity=userService.profile(1, null);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void nullId(){
		ResponseEntity responseEntity=userService.profile(null, "qiwdnwofnwiofbwofuiw");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void profileShouldWork() throws InsertException {
		User user=userDAO.createSingleUser();
		User userLogged=userDAO.signInUser(user);
		ResponseEntity responseEntity=userService.profile(userLogged.getId(),userLogged.getToken());
		Assert.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}

	@Test
	public void UnauthorizedAccessTokenShouldWork() throws InsertException {
		User user=userDAO.createSingleUser();
		User userLogged=userDAO.signInUser(user);
		ResponseEntity responseEntity=userService.profile(userLogged.getId(),"teste");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
	}

	@Test
	public void InvalidSessionShouldWork() throws InsertException{
		User user=userDAO.createSingleUser();
		User userLogged=userDAO.signInUser(user);
		//put last login a date 30 mins before
		userLogged.setLastLogin(new Date(Calendar.getInstance().getTimeInMillis()-1800001));
		userDAO.save(userLogged);
		ResponseEntity responseEntity=userService.profile(userLogged.getId(),userLogged.getToken());
		Assert.assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());
	}

}

