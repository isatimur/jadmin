package ru.atc.jadmin.repository.search;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.atc.jadmin.domain.Theme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Theme entity.
 */
public interface ThemeSearchRepository extends ElasticsearchRepository<Theme, Long> {
    Page<Theme> findByNameContains(String name,Pageable pageable);
}
