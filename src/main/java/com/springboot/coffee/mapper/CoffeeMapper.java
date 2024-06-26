package com.springboot.coffee.mapper;

import com.springboot.coffee.dto.CoffeePatchDto;
import com.springboot.coffee.dto.CoffeePostDto;
import com.springboot.coffee.dto.CoffeeResponseDto;
import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.service.CoffeeService;
import com.springboot.order.dto.OrderCoffeeDto;
import com.springboot.order.entity.OrderCoffee;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {
    Coffee coffeePostDtoToCoffee(CoffeePostDto coffeePostDto);
    Coffee coffeePatchDtoToCoffee(CoffeePatchDto coffeePatchDto);
    CoffeeResponseDto coffeeToCoffeeResponseDto(Coffee coffee);
    Coffee orderCoffeePostDtoToCoffee(OrderCoffeeDto orderCoffeeDto);
    List<CoffeeResponseDto> coffeesToCoffeeResponseDtos(List<Coffee> coffees);
}
