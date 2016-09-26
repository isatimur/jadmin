package ru.atc.jadmin.web.rest;

import ru.atc.jadmin.JadminApp;

import ru.atc.jadmin.domain.Theme;
import ru.atc.jadmin.repository.ThemeRepository;
import ru.atc.jadmin.service.ThemeService;
import ru.atc.jadmin.repository.search.ThemeSearchRepository;
import ru.atc.jadmin.service.dto.ThemeDTO;
import ru.atc.jadmin.service.mapper.ThemeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ThemeResource REST controller.
 *
 * @see ThemeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JadminApp.class)
public class ThemeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Long DEFAULT_SORT = 1L;
    private static final Long UPDATED_SORT = 2L;

    @Inject
    private ThemeRepository themeRepository;

    @Inject
    private ThemeMapper themeMapper;

    @Inject
    private ThemeService themeService;

    @Inject
    private ThemeSearchRepository themeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restThemeMockMvc;

    private Theme theme;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ThemeResource themeResource = new ThemeResource();
        ReflectionTestUtils.setField(themeResource, "themeService", themeService);
        this.restThemeMockMvc = MockMvcBuilders.standaloneSetup(themeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theme createEntity(EntityManager em) {
        Theme theme = new Theme()
                .name(DEFAULT_NAME)
                .sort(DEFAULT_SORT);
        return theme;
    }

    @Before
    public void initTest() {
        themeSearchRepository.deleteAll();
        theme = createEntity(em);
    }

    @Test
    @Transactional
    public void createTheme() throws Exception {
        int databaseSizeBeforeCreate = themeRepository.findAll().size();

        // Create the Theme
        ThemeDTO themeDTO = themeMapper.themeToThemeDTO(theme);

        restThemeMockMvc.perform(post("/api/themes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
                .andExpect(status().isCreated());

        // Validate the Theme in the database
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(databaseSizeBeforeCreate + 1);
        Theme testTheme = themes.get(themes.size() - 1);
        assertThat(testTheme.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTheme.getSort()).isEqualTo(DEFAULT_SORT);

        // Validate the Theme in ElasticSearch
        Theme themeEs = themeSearchRepository.findOne(testTheme.getId());
        assertThat(themeEs).isEqualToComparingFieldByField(testTheme);
    }

    @Test
    @Transactional
    public void getAllThemes() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get all the themes
        restThemeMockMvc.perform(get("/api/themes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT.intValue())));
    }

    @Test
    @Transactional
    public void getTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);

        // Get the theme
        restThemeMockMvc.perform(get("/api/themes/{id}", theme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(theme.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sort").value(DEFAULT_SORT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTheme() throws Exception {
        // Get the theme
        restThemeMockMvc.perform(get("/api/themes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);
        themeSearchRepository.save(theme);
        int databaseSizeBeforeUpdate = themeRepository.findAll().size();

        // Update the theme
        Theme updatedTheme = themeRepository.findOne(theme.getId());
        updatedTheme
                .name(UPDATED_NAME)
                .sort(UPDATED_SORT);
        ThemeDTO themeDTO = themeMapper.themeToThemeDTO(updatedTheme);

        restThemeMockMvc.perform(put("/api/themes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(themeDTO)))
                .andExpect(status().isOk());

        // Validate the Theme in the database
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(databaseSizeBeforeUpdate);
        Theme testTheme = themes.get(themes.size() - 1);
        assertThat(testTheme.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTheme.getSort()).isEqualTo(UPDATED_SORT);

        // Validate the Theme in ElasticSearch
        Theme themeEs = themeSearchRepository.findOne(testTheme.getId());
        assertThat(themeEs).isEqualToComparingFieldByField(testTheme);
    }

    @Test
    @Transactional
    public void deleteTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);
        themeSearchRepository.save(theme);
        int databaseSizeBeforeDelete = themeRepository.findAll().size();

        // Get the theme
        restThemeMockMvc.perform(delete("/api/themes/{id}", theme.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean themeExistsInEs = themeSearchRepository.exists(theme.getId());
        assertThat(themeExistsInEs).isFalse();

        // Validate the database is empty
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTheme() throws Exception {
        // Initialize the database
        themeRepository.saveAndFlush(theme);
        themeSearchRepository.save(theme);

        // Search the theme
        restThemeMockMvc.perform(get("/api/_search/themes?query=id:" + theme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theme.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT.intValue())));
    }
}
