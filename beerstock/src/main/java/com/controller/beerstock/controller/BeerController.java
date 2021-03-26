package com.controller.beerstock.controller;

import com.controller.beerstock.entity.Beer;
import com.controller.beerstock.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    @Autowired
    private BeerRepository beerRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beer post(@RequestBody Beer beer){
        return beerRepository.save(beer);
    }

    @GetMapping
    public List<Beer> listAllBeers(){
        return beerRepository.findAll();
    }
}
