package com.concrete.spring.controller;

import com.concrete.spring.domain.ErrorMessage;
import com.concrete.spring.domain.User;
import com.concrete.spring.domain.UserRepository;
import com.concrete.spring.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ApplicationController {

    @Autowired
    private UserRepository userRepository;

    //30 mins
    private final long TOKEN_TIMEOUT=1800000;
    //private final long TOKEN_TIMEOUT=20000;


    private ErrorMessage returnErrorMessage(String message){
        ErrorMessage errorMessage=new ErrorMessage();
        errorMessage.setMessage(message);
        return errorMessage;
    }

    private ResponseEntity signInUser(User user, boolean updateToken){
        Date date = new Date();
        date.setTime(Calendar.getInstance().getTimeInMillis());
        user.setLastLogin(date);
        if(updateToken){
            user.setToken(UUID.randomUUID().toString());
        }
        //save into database
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
            //return userRepository.save(user);
        }
        //returns if raises an error
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(returnErrorMessage("Failed to insert user in database"));
            //return returnErrorMessage("Failed to insert user in database");
        }
    }

    @GetMapping("/anerror")
    public ErrorMessage error(){
        return returnErrorMessage("An error has occurred");
    }


    @PostMapping("/users")
    public ResponseEntity register(@RequestBody(required = false) User user){
        UserValidation userValidation=new UserValidation();
        //verifies if user has name, email, and password
        if(userValidation.validate(user)){
            //verifies if user email already exists
            if(userRepository.findByEmail(user.getEmail())!=null){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnErrorMessage("This email already exists"));
            }
            //if email does not exists, put in the database
            else{
                Date date = new Date();
                date.setTime(Calendar.getInstance().getTimeInMillis());
                user.setCreated(date);
                user.setModified(date);
                user.setLastLogin(date);
                user.setToken(UUID.randomUUID().toString());
                //save into database
                try{
                    return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
                    //return userRepository.save(user);
                }
                //returns if raises an error
                catch (Exception e){
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(returnErrorMessage("Failed to insert user in database"));
                    //return returnErrorMessage("Failed to insert user in database");
                }

            }

        }
        //returns if some field did not filled correctly
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnErrorMessage("One or more fields do not filled correctly"));
            //returnErrorMessage("One or more fields do not filled correctly");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody(required = false) User user){
        UserValidation userValidation=new UserValidation();
        //verifies if user has name, email, and password
        if(userValidation.validateLogin(user)){
            //verifies if user email already exists
            if(userRepository.findByEmail(user.getEmail())!=null){
                //if exists, verifies if password is correct
                User validatedUser=userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
                //if correct, update the last login and reply
                if(validatedUser!=null){
                    return signInUser(validatedUser, true);
                }
                //if password does not match, returns email/passord invalid with unauthorized
                else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Invalid user and/or password"));
                }
            }
            //if email does not exists returns email/password invalid with not found
            else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Invalid user and/or password"));
            }

        }
        //returns if some field did not filled correctly
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnErrorMessage("One or more fields do not filled correctly"));
            //returnErrorMessage("One or more fields do not filled correctly");
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity profile(@PathVariable Integer id, @RequestHeader String token){
        User userByToken=userRepository.findByToken(token);
        //if token exists
        if(userByToken!=null){
            User userById = userRepository.findById(id).get();
            //if user retrieved by id equals to user retrieved by token
            if(userById.equals(userByToken)){
                //verify if last login was 30 minutes left
                if(Calendar.getInstance().getTimeInMillis()-userById.getLastLogin().getTime()<TOKEN_TIMEOUT){
                    //if positive, returns user
                    return signInUser(userById, false);
                }
                //otherwise, returns invalid session
                else{
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Invalid Session"));
                }
            }
            //if users don't match
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Unauthorized access"));
            }
        }
        //if token does not exists
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Unauthorized access"));
        }
    }
}
