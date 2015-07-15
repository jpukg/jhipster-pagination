package io.github.jhipster.web.rest.mapper;

import io.github.jhipster.domain.*;
import io.github.jhipster.web.rest.dto.CarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Car and its DTO CarDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarMapper {

    CarDTO carToCarDTO(Car car);

    Car carDTOToCar(CarDTO carDTO);
}
