package com.matyuhin.books.service;

import com.matyuhin.books.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DirectoryUserDetailsService implements UserDetailsService {
    private final UserService userService;
    public DirectoryUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final com.matyuhin.books.entity.User user = userService.findUserByEmail(username);
            if (user != null) {
                System.out.println(user.getRoles().get(0).getName());
                return User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRoles().get(0).getName())
                        .build();
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        throw new UsernameNotFoundException(username);
    }
}
