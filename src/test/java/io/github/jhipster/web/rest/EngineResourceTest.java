package io.github.jhipster.web.rest;

import io.github.jhipster.Application;
import io.github.jhipster.domain.Engine;
import io.github.jhipster.repository.EngineRepository;
import io.github.jhipster.repository.search.EngineSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EngineResource REST controller.
 *
 * @see EngineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EngineResourceTest {


    private static final Integer DEFAULT_CAPACITY = 0;
    private static final Integer UPDATED_CAPACITY = 1;
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private EngineRepository engineRepository;

    @Inject
    private EngineSearchRepository engineSearchRepository;

    private MockMvc restEngineMockMvc;

    private Engine engine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EngineResource engineResource = new EngineResource();
        ReflectionTestUtils.setField(engineResource, "engineRepository", engineRepository);
        ReflectionTestUtils.setField(engineResource, "engineSearchRepository", engineSearchRepository);
        this.restEngineMockMvc = MockMvcBuilders.standaloneSetup(engineResource).build();
    }

    @Before
    public void initTest() {
        engine = new Engine();
        engine.setCapacity(DEFAULT_CAPACITY);
        engine.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEngine() throws Exception {
        int databaseSizeBeforeCreate = engineRepository.findAll().size();

        // Create the Engine
        restEngineMockMvc.perform(post("/api/engines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engine)))
                .andExpect(status().isCreated());

        // Validate the Engine in the database
        List<Engine> engines = engineRepository.findAll();
        assertThat(engines).hasSize(databaseSizeBeforeCreate + 1);
        Engine testEngine = engines.get(engines.size() - 1);
        assertThat(testEngine.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testEngine.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = engineRepository.findAll().size();
        // set the field null
        engine.setCapacity(null);

        // Create the Engine, which fails.
        restEngineMockMvc.perform(post("/api/engines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engine)))
                .andExpect(status().isBadRequest());

        List<Engine> engines = engineRepository.findAll();
        assertThat(engines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = engineRepository.findAll().size();
        // set the field null
        engine.setName(null);

        // Create the Engine, which fails.
        restEngineMockMvc.perform(post("/api/engines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engine)))
                .andExpect(status().isBadRequest());

        List<Engine> engines = engineRepository.findAll();
        assertThat(engines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEngines() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engines
        restEngineMockMvc.perform(get("/api/engines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(engine.getId().intValue())))
                .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getEngine() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get the engine
        restEngineMockMvc.perform(get("/api/engines/{id}", engine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(engine.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEngine() throws Exception {
        // Get the engine
        restEngineMockMvc.perform(get("/api/engines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEngine() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

		int databaseSizeBeforeUpdate = engineRepository.findAll().size();

        // Update the engine
        engine.setCapacity(UPDATED_CAPACITY);
        engine.setName(UPDATED_NAME);
        restEngineMockMvc.perform(put("/api/engines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(engine)))
                .andExpect(status().isOk());

        // Validate the Engine in the database
        List<Engine> engines = engineRepository.findAll();
        assertThat(engines).hasSize(databaseSizeBeforeUpdate);
        Engine testEngine = engines.get(engines.size() - 1);
        assertThat(testEngine.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEngine.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteEngine() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

		int databaseSizeBeforeDelete = engineRepository.findAll().size();

        // Get the engine
        restEngineMockMvc.perform(delete("/api/engines/{id}", engine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Engine> engines = engineRepository.findAll();
        assertThat(engines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
