package com.concrete.spring;

import com.concrete.spring.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * command runner allows to execute repository methods at startup
 * https://start.spring.io/
 * select web, h2, hibernate, h2
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

	//@Autowired
	//private UserRepository userRepository;

	@Autowired
	private UserDAO userDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//UserDAO userDAO=new UserDAO(userRepository);
		userDAO.createSingleUser();
	}
}

