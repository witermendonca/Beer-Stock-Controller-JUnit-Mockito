package com.controller.beerstock.controller;

import com.controller.beerstock.dto.BeerDTO;
import com.controller.beerstock.entity.Beer;
import com.controller.beerstock.exception.BeerAlreadyRegisteredException;
import com.controller.beerstock.repository.BeerRepository;
import com.controller.beerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController {

    private final BeerService beerService;

    private BeerRepository beerRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {

        return beerService.createBeer(beerDTO);
    }

    @GetMapping
    public List<Beer> listAllBeers(){
        return beerRepository.findAll();
    }
}
