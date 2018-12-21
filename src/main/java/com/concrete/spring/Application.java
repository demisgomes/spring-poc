package com.concrete.spring;

import com.concrete.spring.domain.Phone;
import com.concrete.spring.domain.User;
import com.concrete.spring.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Optional;

/**
 * command runner allows to execute repository methods at startup
 * https://start.spring.io/
 * select web, h2, hibernate, h2
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//tesing a user with two phones
		User user1=new User();

		Phone phone1 = new Phone();
		phone1.setDdd(21);
		phone1.setNumber("998423959239");

		Phone phone2 = new Phone();
		phone2.setDdd(21);
		phone2.setNumber("99843959239");

		ArrayList<Phone> phones=new ArrayList<>();
		phones.add(phone1);
		phones.add(phone2);

		user1.setPhones(phones);

		user1.setName("demis");
		user1.setEmail("demis@concrete.com");
		user1.setPassword("password");
		userRepository.save(user1);


		//logger.info("Users: ", userRepository.findById(1));

		Optional<User> optionalUser=userRepository.findById(1);
		System.out.println(optionalUser.toString());
	}
}

