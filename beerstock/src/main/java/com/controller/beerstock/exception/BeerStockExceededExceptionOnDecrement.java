package com.controller.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededExceptionOnDecrement extends Exception {

    public BeerStockExceededExceptionOnDecrement(Long id, int quantityToDecrement) {

        super(String.format("Beers with %s ID to decremented informed exceeds the minimum stock capacity: %s", id, quantityToDecrement));
    }
}
