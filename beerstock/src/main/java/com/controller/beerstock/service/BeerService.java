package com.controller.beerstock.service;

import com.controller.beerstock.dto.BeerDTO;
import com.controller.beerstock.entity.Beer;
import com.controller.beerstock.exception.BeerAlreadyRegisteredException;
import com.controller.beerstock.exception.BeerNotFoundException;
import com.controller.beerstock.exception.BeerStockExceededExceptionOnIncrement;
import com.controller.beerstock.exception.BeerStockExceededExceptionOnDecrement;
import com.controller.beerstock.mapper.BeerMapper;
import com.controller.beerstock.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);

        return  beerMapper.toDTO(savedBeer);
    }

    public List<BeerDTO> listAll(){
        return  beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));

        return beerMapper.toDTO(foundBeer);
    }

    public BeerDTO findById(Long id) throws BeerNotFoundException {
        Beer foundBeerId = verifyIfExists(id);

        return beerMapper.toDTO(foundBeerId);
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    public BeerDTO increment(Long id, Integer quantityToIncrement) throws BeerNotFoundException, BeerStockExceededExceptionOnIncrement {

        Beer  beerToIncrementStock = verifyIfExists(id);

        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();

        if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
            beerToIncrementStock.setQuantity(quantityAfterIncrement);
            Beer incrementedBeerStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO(incrementedBeerStock);
        }

        throw new BeerStockExceededExceptionOnIncrement(id, quantityToIncrement);

    }

    public BeerDTO decrement(Long id, int quantityToDecrement) throws BeerNotFoundException, BeerStockExceededExceptionOnDecrement {
        Beer beerToDecrementStock = verifyIfExists(id);

        int quantityAfterDecrement = beerToDecrementStock.getQuantity() - quantityToDecrement;

        if(quantityAfterDecrement >= 0){
            beerToDecrementStock.setQuantity(quantityAfterDecrement);
            Beer decrementedBeerStock = beerRepository.save(beerToDecrementStock);
            return beerMapper.toDTO(decrementedBeerStock);
        }

        throw new BeerStockExceededExceptionOnDecrement(id, quantityToDecrement);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {

        Optional<Beer> optionalSaveBeer = beerRepository.findByName(name);

        if (optionalSaveBeer.isPresent()){
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }
}
