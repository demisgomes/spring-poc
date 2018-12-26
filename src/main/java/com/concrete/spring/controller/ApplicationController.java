package com.concrete.spring.controller;

import com.concrete.spring.domain.ErrorMessage;
import com.concrete.spring.domain.User;
import com.concrete.spring.domain.UserRepository;
import com.concrete.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApplicationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private ErrorMessage returnErrorMessage(String message){
        ErrorMessage errorMessage=new ErrorMessage();
        errorMessage.setMessage(message);
        return errorMessage;
    }

    @GetMapping("/anerror")
    public ErrorMessage error(){
        return returnErrorMessage("An error has occurred");
    }


    @PostMapping("/users")
    public ResponseEntity register(@RequestBody(required = false) User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody(required = false) User user){
        return userService.login(user);

    }

    @GetMapping("/users/{id}")
    public ResponseEntity profile(@PathVariable Integer id, @RequestHeader String token){
        return userService.profile(id,token);
    }
}
