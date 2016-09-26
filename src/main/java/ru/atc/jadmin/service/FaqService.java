package ru.atc.jadmin.service;

import ru.atc.jadmin.service.dto.FaqDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Faq.
 */
public interface FaqService {

    /**
     * Save a faq.
     *
     * @param faqDTO the entity to save
     * @return the persisted entity
     */
    FaqDTO save(FaqDTO faqDTO);

    /**
     *  Get all the faqs.
     *  
     *  @return the list of entities
     */
    List<FaqDTO> findAll();

    /**
     *  Get the "id" faq.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FaqDTO findOne(Long id);

    /**
     *  Delete the "id" faq.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the faq corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<FaqDTO> search(String query);
}
