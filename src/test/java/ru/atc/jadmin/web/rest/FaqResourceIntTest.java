package ru.atc.jadmin.web.rest;

import ru.atc.jadmin.JadminApp;

import ru.atc.jadmin.domain.Faq;
import ru.atc.jadmin.repository.FaqRepository;
import ru.atc.jadmin.service.FaqService;
import ru.atc.jadmin.repository.search.FaqSearchRepository;
import ru.atc.jadmin.service.dto.FaqDTO;
import ru.atc.jadmin.service.mapper.FaqMapper;

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
 * Test class for the FaqResource REST controller.
 *
 * @see FaqResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JadminApp.class)
public class FaqResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_QUESTION_TEXT = "AAAAA";
    private static final String UPDATED_QUESTION_TEXT = "BBBBB";
    private static final String DEFAULT_ANSWER_TEXT = "AAAAA";
    private static final String UPDATED_ANSWER_TEXT = "BBBBB";

    private static final Long DEFAULT_SORT = 1L;
    private static final Long UPDATED_SORT = 2L;

    @Inject
    private FaqRepository faqRepository;

    @Inject
    private FaqMapper faqMapper;

    @Inject
    private FaqService faqService;

    @Inject
    private FaqSearchRepository faqSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFaqMockMvc;

    private Faq faq;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FaqResource faqResource = new FaqResource();
        ReflectionTestUtils.setField(faqResource, "faqService", faqService);
        this.restFaqMockMvc = MockMvcBuilders.standaloneSetup(faqResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Faq createEntity(EntityManager em) {
        Faq faq = new Faq()
                .name(DEFAULT_NAME)
                .questionText(DEFAULT_QUESTION_TEXT)
                .answerText(DEFAULT_ANSWER_TEXT)
                .sort(DEFAULT_SORT);
        return faq;
    }

    @Before
    public void initTest() {
        faqSearchRepository.deleteAll();
        faq = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaq() throws Exception {
        int databaseSizeBeforeCreate = faqRepository.findAll().size();

        // Create the Faq
        FaqDTO faqDTO = faqMapper.faqToFaqDTO(faq);

        restFaqMockMvc.perform(post("/api/faqs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faqDTO)))
                .andExpect(status().isCreated());

        // Validate the Faq in the database
        List<Faq> faqs = faqRepository.findAll();
        assertThat(faqs).hasSize(databaseSizeBeforeCreate + 1);
        Faq testFaq = faqs.get(faqs.size() - 1);
        assertThat(testFaq.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFaq.getQuestionText()).isEqualTo(DEFAULT_QUESTION_TEXT);
        assertThat(testFaq.getAnswerText()).isEqualTo(DEFAULT_ANSWER_TEXT);
        assertThat(testFaq.getSort()).isEqualTo(DEFAULT_SORT);

        // Validate the Faq in ElasticSearch
        Faq faqEs = faqSearchRepository.findOne(testFaq.getId());
        assertThat(faqEs).isEqualToComparingFieldByField(testFaq);
    }

    @Test
    @Transactional
    public void getAllFaqs() throws Exception {
        // Initialize the database
        faqRepository.saveAndFlush(faq);

        // Get all the faqs
        restFaqMockMvc.perform(get("/api/faqs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(faq.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].questionText").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
                .andExpect(jsonPath("$.[*].answerText").value(hasItem(DEFAULT_ANSWER_TEXT.toString())))
                .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT.intValue())));
    }

    @Test
    @Transactional
    public void getFaq() throws Exception {
        // Initialize the database
        faqRepository.saveAndFlush(faq);

        // Get the faq
        restFaqMockMvc.perform(get("/api/faqs/{id}", faq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(faq.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.questionText").value(DEFAULT_QUESTION_TEXT.toString()))
            .andExpect(jsonPath("$.answerText").value(DEFAULT_ANSWER_TEXT.toString()))
            .andExpect(jsonPath("$.sort").value(DEFAULT_SORT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFaq() throws Exception {
        // Get the faq
        restFaqMockMvc.perform(get("/api/faqs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaq() throws Exception {
        // Initialize the database
        faqRepository.saveAndFlush(faq);
        faqSearchRepository.save(faq);
        int databaseSizeBeforeUpdate = faqRepository.findAll().size();

        // Update the faq
        Faq updatedFaq = faqRepository.findOne(faq.getId());
        updatedFaq
                .name(UPDATED_NAME)
                .questionText(UPDATED_QUESTION_TEXT)
                .answerText(UPDATED_ANSWER_TEXT)
                .sort(UPDATED_SORT);
        FaqDTO faqDTO = faqMapper.faqToFaqDTO(updatedFaq);

        restFaqMockMvc.perform(put("/api/faqs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faqDTO)))
                .andExpect(status().isOk());

        // Validate the Faq in the database
        List<Faq> faqs = faqRepository.findAll();
        assertThat(faqs).hasSize(databaseSizeBeforeUpdate);
        Faq testFaq = faqs.get(faqs.size() - 1);
        assertThat(testFaq.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFaq.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testFaq.getAnswerText()).isEqualTo(UPDATED_ANSWER_TEXT);
        assertThat(testFaq.getSort()).isEqualTo(UPDATED_SORT);

        // Validate the Faq in ElasticSearch
        Faq faqEs = faqSearchRepository.findOne(testFaq.getId());
        assertThat(faqEs).isEqualToComparingFieldByField(testFaq);
    }

    @Test
    @Transactional
    public void deleteFaq() throws Exception {
        // Initialize the database
        faqRepository.saveAndFlush(faq);
        faqSearchRepository.save(faq);
        int databaseSizeBeforeDelete = faqRepository.findAll().size();

        // Get the faq
        restFaqMockMvc.perform(delete("/api/faqs/{id}", faq.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean faqExistsInEs = faqSearchRepository.exists(faq.getId());
        assertThat(faqExistsInEs).isFalse();

        // Validate the database is empty
        List<Faq> faqs = faqRepository.findAll();
        assertThat(faqs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFaq() throws Exception {
        // Initialize the database
        faqRepository.saveAndFlush(faq);
        faqSearchRepository.save(faq);

        // Search the faq
        restFaqMockMvc.perform(get("/api/_search/faqs?query=id:" + faq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faq.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].questionText").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].answerText").value(hasItem(DEFAULT_ANSWER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT.intValue())));
    }
}
