package com.concrete.spring.validation;

import com.concrete.spring.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {
    public boolean validate(User user){
        if(user!=null){
            if(user.getName()!=null && user.getEmail()!=null && user.getPassword()!=null){
                return true;
            }
        }
        return false;
    }

    public boolean validateLogin(User user){
        if(user!=null){
            if(user.getEmail()!=null && user.getPassword()!=null){
                return true;
            }
        }
        return false;
    }
}
