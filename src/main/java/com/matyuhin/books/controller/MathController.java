package com.matyuhin.books.controller;

import com.matyuhin.books.dto.CalculatorDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class MathController {

    @GetMapping({"/calculator"})
    public ModelAndView getAllStores(@ModelAttribute("calc") CalculatorDto calc) {
        var mav = new ModelAndView("calculator");
        if (calc == null) calc = new CalculatorDto();
        mav.addObject("calc", calc);
        return mav;
    }

    @PostMapping("/calculate")
    public ModelAndView saveStore(@Valid @ModelAttribute("calc") CalculatorDto calc) {
        var answer = ((calc.getArg1() * calc.getArg2()) / 100) * (100 - calc.getArg3());
        answer *= 100;
        answer = Math.round(answer);
        answer /= 100;
        calc.setAnswer(answer);
        return getAllStores(calc);
    }
}
