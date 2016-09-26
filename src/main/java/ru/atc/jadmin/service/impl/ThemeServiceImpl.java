package ru.atc.jadmin.service.impl;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.jadmin.domain.Theme;
import ru.atc.jadmin.repository.ThemeRepository;
import ru.atc.jadmin.repository.search.ThemeSearchRepository;
import ru.atc.jadmin.service.ThemeService;
import ru.atc.jadmin.service.dto.ThemeAndFaqDTO;
import ru.atc.jadmin.service.dto.ThemeDTO;
import ru.atc.jadmin.service.mapper.ThemeMapper;

/**
 * Service Implementation for managing Theme.
 */
@Service
@Transactional
public class ThemeServiceImpl implements ThemeService {

    private final Logger log = LoggerFactory.getLogger(ThemeServiceImpl.class);

    @Inject
    private ThemeRepository themeRepository;

    @Inject
    private ThemeMapper themeMapper;

    @Inject
    private ThemeSearchRepository themeSearchRepository;

    /**
     * Save a theme.
     *
     * @param themeDTO the entity to save
     * @return the persisted entity
     */
    public ThemeDTO save(ThemeDTO themeDTO) {
        log.debug("Request to save Theme : {}", themeDTO);
        Theme theme = themeMapper.themeDTOToTheme(themeDTO);
        theme = themeRepository.save(theme);
        ThemeDTO result = themeMapper.themeToThemeDTO(theme);
        themeSearchRepository.save(theme);
        return result;
    }

    /**
     * Get all the themes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ThemeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Themes");
        Page<Theme> result = themeRepository.findAll(pageable);
        return result.map(theme -> themeMapper.themeToThemeDTO(theme));
    }

    /**
     * Get one theme by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ThemeDTO findOne(Long id) {
        log.debug("Request to get Theme : {}", id);
        Theme theme = themeRepository.findOne(id);
        ThemeDTO themeDTO = themeMapper.themeToThemeDTO(theme);
        return themeDTO;
    }

    /**
     * Delete the  theme by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Theme : {}", id);
        themeRepository.delete(id);
        themeSearchRepository.delete(id);
    }

    /**
     * Search for the theme corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ThemeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Themes for query {}", query);
        Page<Theme> result = //themeSearchRepository.search(queryStringQuery(query), pageable);
            themeSearchRepository.findByNameContains(query, pageable);
        return result.map(theme -> themeMapper.themeToThemeDTO(theme));
    }

    @Transactional(readOnly = true)
    @Override public Page<ThemeAndFaqDTO> searchAllThemesAndFaqs(String query, Pageable pageable) {
        log.debug("Request to search for a page of Themes for query {}", query);
        Page<Theme> result = //themeSearchRepository.search(queryStringQuery(query), pageable);
            themeSearchRepository.findByNameContains(query, pageable);
        return result.map(theme -> themeMapper.themeToThemeAndFaqDTO(theme));
    }
}
