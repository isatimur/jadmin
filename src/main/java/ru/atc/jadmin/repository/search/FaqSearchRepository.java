package ru.atc.jadmin.repository.search;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.atc.jadmin.domain.Faq;

/**
 * Spring Data ElasticSearch repository for the Faq entity.
 */
public interface FaqSearchRepository extends ElasticsearchRepository<Faq, Long> {
    List<Faq> findByNameContainsOrQuestionTextContainsOrAnswerTextContains(String name, String questionText,
        String answerText);
}
