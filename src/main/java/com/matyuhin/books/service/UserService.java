package com.matyuhin.books.service;

import com.matyuhin.books.dto.UserDto;
import com.matyuhin.books.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    User getCurrentUser();
    List<UserDto> findAllUsers();
    void changeRole(String email, String role);
}
