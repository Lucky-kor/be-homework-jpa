package com.springboot.order.dto;

import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class OrderCoffeeDto {
    @Positive
    private Long coffeeId;

    @Positive
    private int quantity;
}
