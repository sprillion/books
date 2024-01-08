package com.matyuhin.books.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
                return User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRoles().get(0).getName())
                        .build();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        throw new UsernameNotFoundException(username);
    }
}
