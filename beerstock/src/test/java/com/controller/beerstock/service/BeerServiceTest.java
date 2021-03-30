package com.controller.beerstock.service;

import com.controller.beerstock.builder.BeerDTOBuilder;
import com.controller.beerstock.dto.BeerDTO;
import com.controller.beerstock.entity.Beer;
import com.controller.beerstock.exception.BeerAlreadyRegisteredException;
import com.controller.beerstock.exception.BeerNotFoundException;
import com.controller.beerstock.mapper.BeerMapper;
import com.controller.beerstock.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;  //mock of BeerRepository

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        //give
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        //then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

        //assertEquals(expectedBeerDTO.getId(), createdBeerDTO.getId());
        //assertEquals(expectedBeerDTO.getName(), createdBeerDTO.getName());
        //assertEquals(expectedBeerDTO.getQuantity(), createdBeerDTO.getQuantity());


        //hamcrest
        assertThat(createdBeerDTO.getId(), is(equalTo(expectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getName(), is(equalTo(expectedBeerDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(expectedBeerDTO.getQuantity())));

        //Expected: is a value greater than <12>
        //     but: <10> was less than <12>
        assertThat(createdBeerDTO.getQuantity(), is(greaterThan(12)));

    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown(){
        //give
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));
        //returns beer with registered name ^

        //then
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));

    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        //when
        when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));
        //returns beer with registered name ^

        //then
        BeerDTO foundBeerDTO = beerService.findByName(expectedFoundBeerDTO.getName());
        assertThat(foundBeerDTO, is(equalTo(expectedFoundBeerDTO)));

    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());
        //returns beer with empty registered

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getName()));

    }

    @Test
    void whenValidBeerIdIsGivenThenReturnABeer() throws BeerNotFoundException {
        //given
        BeerDTO expectedIdFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedIdFoundBeer = beerMapper.toModel(expectedIdFoundBeerDTO);

        //when
        when(beerRepository.findById(expectedIdFoundBeer.getId())).thenReturn(Optional.of(expectedIdFoundBeer));
        //returns beer with registered id ^

        //then
        BeerDTO foundBeerDTO = beerService.findById(expectedIdFoundBeerDTO.getId());
        assertThat(foundBeerDTO, is(equalTo(expectedIdFoundBeerDTO)));

    }

    @Test
    void whenNotRegisteredBeerIdIsGivenThenThrowAnException() {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerRepository.findById(expectedFoundBeerDTO.getId())).thenReturn(Optional.empty());
        //returns beer with empty registered

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findById(expectedFoundBeerDTO.getId()));

    }


}
