package com.matyuhin.books.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorDto {

    private int arg1;
    private float arg2;
    private float arg3;
    private float answer;
}
