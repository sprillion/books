package com.matyuhin.books.controller;

import com.matyuhin.books.dto.UserDto;
import com.matyuhin.books.models.Roles;
import com.matyuhin.books.repository.UserRepository;
import com.matyuhin.books.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Slf4j
@Controller
public class SecurityController {

    private UserService userService;
    private UserRepository userRepository;
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/accessDenied")
    public ModelAndView accessDenied() {
        return new ModelAndView("access-denied");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about-the-app");
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        var user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult bindingResult,
                               Model model) {
        var existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", null,
                    "Электронная почта уже используется");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        var users = userService.findAllUsers();
        String[] roles = Arrays.stream(Roles.values()).map(Enum::toString).toArray(String[]::new);
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "users";
    }

    @PostMapping("/changeRole")
    public String changeRole(@ModelAttribute("email") String email, @ModelAttribute("role") String role) {
        log.info(email + "   " + role);
        userService.changeRole(email, role);
        return "redirect:/users";
    }
}