package ru.atc.jadmin.service;

import ru.atc.jadmin.service.dto.ThemeAndFaqDTO;
import ru.atc.jadmin.service.dto.ThemeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Theme.
 */
public interface ThemeService {

    /**
     * Save a theme.
     *
     * @param themeDTO the entity to save
     * @return the persisted entity
     */
    ThemeDTO save(ThemeDTO themeDTO);

    /**
     *  Get all the themes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ThemeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" theme.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ThemeDTO findOne(Long id);

    /**
     *  Delete the "id" theme.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the theme corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ThemeDTO> search(String query, Pageable pageable);

    /**
     *
     * @param query
     * @param pageable
     * @return
     */
    Page<ThemeAndFaqDTO> searchAllThemesAndFaqs(String query, Pageable pageable);
}
