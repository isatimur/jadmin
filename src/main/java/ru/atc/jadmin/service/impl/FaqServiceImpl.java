package ru.atc.jadmin.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.jadmin.domain.Faq;
import ru.atc.jadmin.repository.FaqRepository;
import ru.atc.jadmin.repository.search.FaqSearchRepository;
import ru.atc.jadmin.service.FaqService;
import ru.atc.jadmin.service.dto.FaqDTO;
import ru.atc.jadmin.service.mapper.FaqMapper;

/**
 * Service Implementation for managing Faq.
 */
@Service
@Transactional
public class FaqServiceImpl implements FaqService {

    private final Logger log = LoggerFactory.getLogger(FaqServiceImpl.class);

    @Inject
    private FaqRepository faqRepository;

    @Inject
    private FaqMapper faqMapper;

    @Inject
    private FaqSearchRepository faqSearchRepository;

    /**
     * Save a faq.
     *
     * @param faqDTO the entity to save
     * @return the persisted entity
     */
    public FaqDTO save(FaqDTO faqDTO) {
        log.debug("Request to save Faq : {}", faqDTO);
        Faq faq = faqMapper.faqDTOToFaq(faqDTO);
        faq = faqRepository.save(faq);
        FaqDTO result = faqMapper.faqToFaqDTO(faq);
        faqSearchRepository.save(faq);
        return result;
    }

    /**
     * Get all the faqs.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FaqDTO> findAll() {
        log.debug("Request to get all Faqs");
        List<FaqDTO> result = faqRepository.findAll().stream()
            .map(faqMapper::faqToFaqDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     * Get one faq by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FaqDTO findOne(Long id) {
        log.debug("Request to get Faq : {}", id);
        Faq faq = faqRepository.findOne(id);
        FaqDTO faqDTO = faqMapper.faqToFaqDTO(faq);
        return faqDTO;
    }

    /**
     * Delete the  faq by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Faq : {}", id);
        faqRepository.delete(id);
        faqSearchRepository.delete(id);
    }

    /**
     * Search for the faq corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FaqDTO> search(String query) {
        log.debug("Request to search Faqs for query {}", query);
        return StreamSupport
            .stream(
//                faqSearchRepository.search(queryStringQuery(query))
                faqSearchRepository.findByNameContainsOrQuestionTextContainsOrAnswerTextContains(query, query, query)
                    .spliterator(), false)
            .map(faqMapper::faqToFaqDTO)
            .collect(Collectors.toList());
    }
}
