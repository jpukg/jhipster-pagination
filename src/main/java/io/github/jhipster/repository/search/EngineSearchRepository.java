package io.github.jhipster.repository.search;

import io.github.jhipster.domain.Engine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Engine entity.
 */
public interface EngineSearchRepository extends ElasticsearchRepository<Engine, Long> {
}
