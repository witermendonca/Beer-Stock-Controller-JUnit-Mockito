package com.controller.beerstock.service;

import com.controller.beerstock.builder.BeerDTOBuilder;
import com.controller.beerstock.dto.BeerDTO;
import com.controller.beerstock.entity.Beer;
import com.controller.beerstock.exception.BeerAlreadyRegisteredException;
import com.controller.beerstock.mapper.BeerMapper;
import com.controller.beerstock.repository.BeerRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Mockito.when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
        Mockito.when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        //then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

        //assertEquals(expectedBeerDTO.getId(), createdBeerDTO.getId());
        //assertEquals(expectedBeerDTO.getName(), createdBeerDTO.getName());
        //assertEquals(expectedBeerDTO.getQuantity(), createdBeerDTO.getQuantity());


        //hamcrest
        MatcherAssert.assertThat(createdBeerDTO.getId(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getId())));
        MatcherAssert.assertThat(createdBeerDTO.getName(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getName())));
        MatcherAssert.assertThat(createdBeerDTO.getQuantity(), Matchers.is(Matchers.equalTo(expectedBeerDTO.getQuantity())));

        //Expected: is a value greater than <12>
        //     but: <10> was less than <12>
        MatcherAssert.assertThat(createdBeerDTO.getQuantity(), Matchers.is(Matchers.greaterThan(12)));

    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown(){
        //give
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        Mockito.when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        //then
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));

    }


}
