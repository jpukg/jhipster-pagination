package io.github.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.domain.Car;
import io.github.jhipster.repository.CarRepository;
import io.github.jhipster.repository.search.CarSearchRepository;
import io.github.jhipster.config.pagination.PaginationUtil;
import io.github.jhipster.web.rest.dto.CarDTO;
import io.github.jhipster.web.rest.mapper.CarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Car.
 */
@RestController
@RequestMapping("/api")
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    @Inject
    private CarRepository carRepository;

    @Inject
    private CarMapper carMapper;

    @Inject
    private CarSearchRepository carSearchRepository;

    /**
     * POST  /cars -> Create a new car.
     */
    @RequestMapping(value = "/cars",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDTO> create(@Valid @RequestBody CarDTO carDTO) throws URISyntaxException {
        log.debug("REST request to save Car : {}", carDTO);
        if (carDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new car cannot already have an ID").body(null);
        }
        Car car = carMapper.carDTOToCar(carDTO);
        Car result = carRepository.save(car);
        carSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cars/" + carDTO.getId())).body(carMapper.carToCarDTO(result));
    }

    /**
     * PUT  /cars -> Updates an existing car.
     */
    @RequestMapping(value = "/cars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDTO> update(@Valid @RequestBody CarDTO carDTO) throws URISyntaxException {
        log.debug("REST request to update Car : {}", carDTO);
        if (carDTO.getId() == null) {
            return create(carDTO);
        }
        Car car = carMapper.carDTOToCar(carDTO);
        Car result = carRepository.save(car);
        carSearchRepository.save(car);
        return ResponseEntity.ok().body(carMapper.carToCarDTO(result));
    }

    /**
     * GET  /cars -> get all the cars.
     */
    @RequestMapping(value = "/cars",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CarDTO>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Car> page = carRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cars");
        return new ResponseEntity<>(page.getContent().stream()
            .map(carMapper::carToCarDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /cars/:id -> get the "id" car.
     */
    @RequestMapping(value = "/cars/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Car : {}", id);
        return Optional.ofNullable(carRepository.findOne(id))
            .map(carMapper::carToCarDTO)
            .map(carDTO -> new ResponseEntity<>(
                carDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cars/:id -> delete the "id" car.
     */
    @RequestMapping(value = "/cars/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Car : {}", id);
        carRepository.delete(id);
        carSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/cars/:query -> search for the car corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/cars/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Car> search(@PathVariable String query) {
        return StreamSupport
            .stream(carSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
