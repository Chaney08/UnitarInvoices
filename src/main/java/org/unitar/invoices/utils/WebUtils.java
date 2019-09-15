package org.unitar.invoices.utils;

import org.unitar.invoices.model.User;
import org.unitar.invoices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class WebUtils {

    @Autowired
    private UserRepository appUserRepo;

    public User getUser(){
        return this.appUserRepo.findByUserName(getUserName());
    }

    public String getUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}