package io.github.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.domain.Engine;
import io.github.jhipster.repository.EngineRepository;
import io.github.jhipster.repository.search.EngineSearchRepository;
import io.github.jhipster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Engine.
 */
@RestController
@RequestMapping("/api")
public class EngineResource {

    private final Logger log = LoggerFactory.getLogger(EngineResource.class);

    @Inject
    private EngineRepository engineRepository;

    @Inject
    private EngineSearchRepository engineSearchRepository;

    /**
     * POST  /engines -> Create a new engine.
     */
    @RequestMapping(value = "/engines",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engine> create(@Valid @RequestBody Engine engine) throws URISyntaxException {
        log.debug("REST request to save Engine : {}", engine);
        if (engine.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new engine cannot already have an ID").body(null);
        }
        Engine result = engineRepository.save(engine);
        engineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/engines/" + engine.getId())).body(result);
    }

    /**
     * PUT  /engines -> Updates an existing engine.
     */
    @RequestMapping(value = "/engines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engine> update(@Valid @RequestBody Engine engine) throws URISyntaxException {
        log.debug("REST request to update Engine : {}", engine);
        if (engine.getId() == null) {
            return create(engine);
        }
        Engine result = engineRepository.save(engine);
        engineSearchRepository.save(engine);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /engines -> get all the engines.
     */
    @RequestMapping(value = "/engines",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Engine>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Engine> page = engineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/engines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /engines/:id -> get the "id" engine.
     */
    @RequestMapping(value = "/engines/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Engine> get(@PathVariable Long id) {
        log.debug("REST request to get Engine : {}", id);
        return Optional.ofNullable(engineRepository.findOne(id))
            .map(engine -> new ResponseEntity<>(
                engine,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /engines/:id -> delete the "id" engine.
     */
    @RequestMapping(value = "/engines/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Engine : {}", id);
        engineRepository.delete(id);
        engineSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/engines/:query -> search for the engine corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/engines/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Engine> search(@PathVariable String query) {
        return StreamSupport
            .stream(engineSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
