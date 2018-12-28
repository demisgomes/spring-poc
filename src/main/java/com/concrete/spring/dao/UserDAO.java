package com.concrete.spring.dao;

import com.concrete.spring.domain.Phone;
import com.concrete.spring.domain.User;
import com.concrete.spring.domain.UserRepository;
import com.concrete.spring.exception.*;
import com.concrete.spring.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDAO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserValidation userValidation;

    /**
     * Method for populate the database with a single user
     */
    public User createSingleUser(){
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
        try {
            createUser(user1);
        } catch (InsertException e) {
            e.printStackTrace();
        } catch (EmailAlreadyExistsException e){
            e.printStackTrace();
        }

        Optional<User> optionalUser=userRepository.findById(1);
        System.out.println(optionalUser.toString());
        return optionalUser.get();
    }


    /**
     * Verifies if an email exists in database
     * @param email email to be searched
     * @return false if email does not exists
     * @throws EmailAlreadyExistsException if email exists
     */
    public boolean emailExists(String email) throws EmailAlreadyExistsException {
        User user=userRepository.findByEmail(email);
        if(user!=null){
            throw new EmailAlreadyExistsException();
        }
        return false;
    }

    /**
     * return a user through email
     * @param email user email
     * @return an user that matches this email
     * @throws EmailNotFoundException when email does not exists
     */
    public User findByEmail(String email) throws EmailNotFoundException{
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new EmailNotFoundException();
        }
        return user;
    }

    /**
     * Returns a user that matches email and password
     * @param email the email to be searched
     * @param password the password to be searched
     * @return an user that matches email and password
     * @throws InvalidUserPasswordException when email and password do not match with any user
     */
    public User findByEmailAndPassword(String email, String password) throws InvalidUserPasswordException{
        User user=userRepository.findByEmailAndPassword(email, password);
        if(user==null){
            throw new InvalidUserPasswordException();
        }
        return user;
    }

    /**
     * adds Date info for a new user
     * @param user the user without Date info
     * @return a user with created, modified, last login, and token info fulfilled
     */
    public User createUser(User user) throws InsertException, EmailAlreadyExistsException{
        if(userValidation.validate(user)){

            try{
                emailExists(user.getEmail());
                Date date = new Date();
                date.setTime(Calendar.getInstance().getTimeInMillis());
                user.setCreated(date);
                user.setModified(date);
                user.setLastLogin(date);
                user.setToken(UUID.randomUUID().toString());

                return save(user);
            }

            catch (EmailAlreadyExistsException e){
                throw new EmailAlreadyExistsException();
            }
        }
        else{
            throw new InsertException("One or more fields from user were do not filled correctly");
        }

    }

    public User signInUser(User user) throws InsertException{
        if(userValidation.validateLogin(user)){
            Date date = new Date();
            date.setTime(Calendar.getInstance().getTimeInMillis());
            user.setLastLogin(date);

            return save(user);
        }
        else{
            throw new InsertException("One or more fields from user were do not filled correctly");
        }
    }

    public User save(User user) throws InsertException {
        try{
            return userRepository.save(user);
        }
        catch(Exception e){
            throw new InsertException();
        }

    }

    public User findByToken(String token) throws UnauthorizedAccessTokenException {
        User user=userRepository.findByToken(token);
        if(user==null){
            throw new UnauthorizedAccessTokenException();
        }
        return user;
    }

    public User findById(Integer id) throws IdNotFoundException {
        if(userRepository.existsById(id)){
            return userRepository.findById(id).get();
        }
        else{
            throw new IdNotFoundException();
        }
    }

}
