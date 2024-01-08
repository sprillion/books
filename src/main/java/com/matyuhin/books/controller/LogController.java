package com.matyuhin.books.controller;

import com.matyuhin.books.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping({"/listLogs"})
    public ModelAndView getAllLogs() {
        var mav = new ModelAndView("list-logs");
        mav.addObject("logs", logRepository.findAll());
        return mav;
    }
}
