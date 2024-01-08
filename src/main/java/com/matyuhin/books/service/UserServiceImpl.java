package com.matyuhin.books.service;

import com.matyuhin.books.dto.UserDto;
import com.matyuhin.books.entity.Role;
import com.matyuhin.books.entity.User;
import com.matyuhin.books.models.Roles;
import com.matyuhin.books.repository.RoleRepository;
import com.matyuhin.books.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;


        for (int i = 0; i < Roles.values().length; i++)
        {
            Roles roles = Roles.values()[i];
            if (this.roleRepository.findByName(roles.toString()) == null)
            {
                addRole(roles);
            }
        }
    }

    @Override
    public void saveUser(UserDto userDto) {
        var user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var role = roleRepository.findByName(Roles.READ_ONLY.toString());
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return findUserByEmail(((UserDetails)principal).getUsername());
        } else {
            return null;
        }
    }

    @Override
    public void changeRole(String email, String role){
        User user = userRepository.findByEmail(email);
        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add((roleRepository.findByName(role)));
        user.setRoles(roles);
        userRepository.save(user);
    }

    private UserDto mapToUserDto(User user) {
        var userDto = new UserDto();
        var str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRoles().get(0).getName());
        return userDto;
    }

    private Role addRole(Roles roles) {
        var role = new Role();
        role.setName(roles.toString());
        return roleRepository.save(role);
    }

}
