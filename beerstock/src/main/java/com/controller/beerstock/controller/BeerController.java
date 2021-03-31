package com.controller.beerstock.controller;

import com.controller.beerstock.dto.BeerDTO;
import com.controller.beerstock.dto.QuantityDTO;
import com.controller.beerstock.exception.BeerAlreadyRegisteredException;
import com.controller.beerstock.exception.BeerNotFoundException;
import com.controller.beerstock.exception.BeerStockExceededExceptionOnDecrement;
import com.controller.beerstock.exception.BeerStockExceededExceptionOnIncrement;
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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {

        return beerService.createBeer(beerDTO);
    }

    @GetMapping
    public List<BeerDTO> listAllBeers(){
        return beerService.listAll();
    }

    @GetMapping("/name/{name}")
    public BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException {
        return beerService.findByName(name);
    }

    @GetMapping("/{id}")
    public BeerDTO findById(@PathVariable Long id) throws BeerNotFoundException {
        return beerService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id,@RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededExceptionOnIncrement {
        return beerService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public BeerDTO decrement(@PathVariable Long id,@RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededExceptionOnDecrement {
        return beerService.decrement(id, quantityDTO.getQuantity());
    }
}
