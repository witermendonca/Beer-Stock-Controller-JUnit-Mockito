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


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final Long INVALID_BEER_ID = 1L;

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
        //when
        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
        //returns beer with empty registered

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findById(INVALID_BEER_ID));

    }


    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        // given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        //when
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));
        //returns an immutable beer list

        //then
        List<BeerDTO> foundListBeersDTO = beerService.listAll();

        assertThat(foundListBeersDTO, is(not(empty())));
        assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        //when
        when(beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        //returns an empty beer list

        //then
        List<BeerDTO> foundListBeersDTO = beerService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        //given
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDTO);

        //when
        when(beerRepository.findById(expectedDeletedBeerDTO.getId())).thenReturn(Optional.of(expectedDeletedBeer));
        doNothing().when(beerRepository).deleteById(expectedDeletedBeerDTO.getId());
        //doNothing() for setting void methods to do nothing.

        //then
        beerService.deleteById(expectedDeletedBeerDTO.getId());

        //check how many times the method was invoked
        verify(beerRepository, times(1)).findById(expectedDeletedBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(expectedDeletedBeerDTO.getId());

    }

    @Test
    void whenExclusionIsCalledWithInvalidIdThenExceptionShouldBeThrown() {
        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.deleteById(INVALID_BEER_ID));
    }


}
