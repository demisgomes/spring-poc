package com.concrete.spring.service;

import com.concrete.spring.dao.UserDAO;
import com.concrete.spring.domain.ErrorMessage;
import com.concrete.spring.domain.User;
import com.concrete.spring.exception.*;
import com.concrete.spring.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserValidation userValidation;

    @Autowired
    UserDAO userDAO;


    //30 mins
    //private final long TOKEN_TIMEOUT=1800000;
    private final long TOKEN_TIMEOUT=20000;

    private ResponseEntity signInUser(User user, boolean updateToken){
        //save into database
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userDAO.save(user));
            //return userRepository.save(user);
        }
        //returns if raises an error
        catch (InsertException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(returnErrorMessage("Failed to insert user in database"));
            //return returnErrorMessage("Failed to insert user in database");
        }
    }

    private boolean isValidToken(Date lastLogin){
        if(System.currentTimeMillis()-lastLogin.getTime()>TOKEN_TIMEOUT){
            return false;
        }
        return true;
    }

    private ErrorMessage returnErrorMessage(String message){
        ErrorMessage errorMessage=new ErrorMessage();
        errorMessage.setMessage(message);
        return errorMessage;
    }

    public ResponseEntity register(User user){
        //verifies if user has name, email, and password
        if(userValidation.validate(user)){
            try{
                //verifies if user email already exists
                userDAO.emailExists(user.getEmail());
                //if email does not exists, put in the database
                return ResponseEntity.status(HttpStatus.CREATED).body(userDAO.createUser(user));
            }
            catch (EmailAlreadyExistsException e){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnErrorMessage("This email already exists"));
            }

            //returns if raises an error in database
            catch (InsertException e){
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(returnErrorMessage("Failed to insert user in database"));
            }
//            if(userDAO.findByEmail(user.getEmail())!=null){
//
////            }
//            else{
//                //save into database
//                try{
//
//                    //return userRepository.save(user);
//                }
//                //returns if raises an error
//                catch (InsertException e){
//
//                    //return returnErrorMessage("Failed to insert user in database");
//                }
//            }
        }
        //returns if some field did not filled correctly
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnErrorMessage("One or more fields do not filled correctly"));
            //returnErrorMessage("One or more fields do not filled correctly");
        }
    }

    public ResponseEntity login(User user){
        //verifies if user has name, email, and password
        if(userValidation.validateLogin(user)){
            //verifies if user email already exists
            try{
                //if exists, verifies if password is correct
                userDAO.findByEmail(user.getEmail());
                //if correct, update the last login and reply
                User validatedUser = userDAO.findByEmailAndPassword(user.getEmail(), user.getPassword());
                //updates token if it is not valid
                if(!isValidToken(validatedUser.getLastLogin())){
                    validatedUser.setToken(UUID.randomUUID().toString());
                }
                return ResponseEntity.status(HttpStatus.OK).body(userDAO.signInUser(validatedUser));
            }
            catch (InsertException e){
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(returnErrorMessage("Failed to insert user in database"));
            }
            //if password does not match, returns email/passord invalid with unauthorized
            catch (InvalidUserPasswordException e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Invalid user and/or password"));
            }
            catch (EmailNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Invalid user and/or password"));
            }
//            if(userDAO.findByEmail(user.getEmail())!=null){
//                //if exists, verifies if password is correct
//                User validatedUser= userDAO.findByEmailAndPassword(user.getEmail(), user.getPassword());
//                //if correct, update the last login and reply
//                if(validatedUser!=null){
//                    return signInUser(validatedUser, true);
//                }
//                //if password does not match, returns email/passord invalid with unauthorized
//                else{
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Invalid user and/or password"));
//                }
//            }
//            //if email does not exists returns email/password invalid with not found
//            else {
//
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Invalid user and/or password"));
//            }
        }
        //returns if some field did not filled correctly
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnErrorMessage("One or more fields do not filled correctly"));
            //returnErrorMessage("One or more fields do not filled correctly");
        }
    }

    public ResponseEntity profile(Integer id, String token){
        try{
            //if token exists
            User userByToken=userDAO.findByToken(token);
            //if user retrieved by id equals to user retrieved by token
            User userById = userDAO.findById(id);
            if(userById.equals(userByToken)){
                //verify if last login was 30 minutes left

                //if(Calendar.getInstance().getTimeInMillis()-userById.getLastLogin().getTime()<TOKEN_TIMEOUT){
                if(isValidToken(userById.getLastLogin())){
                    //if positive, returns user
                    return login(userById);
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
        catch (UnauthorizedAccessTokenException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Unauthorized access"));
        }
        //if id does not exists
        catch (IdNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Id does not match with any user"));
        }

//        //if token exists
//        if(userByToken!=null){
//            User userById = userRepository.findById(id).get();
//            //if user retrieved by id equals to user retrieved by token
//            if(userById.equals(userByToken)){
//                //verify if last login was 30 minutes left
//                if(Calendar.getInstance().getTimeInMillis()-userById.getLastLogin().getTime()<TOKEN_TIMEOUT){
//                    //if positive, returns user
//                    return userService.login(userById);
//                }
//                //otherwise, returns invalid session
//                else{
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Invalid Session"));
//                }
//            }
//            //if users don't match
//            else{
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnErrorMessage("Unauthorized access"));
//            }
//        }
//        //if token does not exists
//        else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnErrorMessage("Unauthorized access"));
//        }
    }
}
