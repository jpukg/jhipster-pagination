package io.github.jhipster.repository;

import io.github.jhipster.domain.Engine;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Engine entity.
 */
public interface EngineRepository extends JpaRepository<Engine,Long> {

}
