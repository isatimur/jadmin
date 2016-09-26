package ru.atc.jadmin.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.atc.jadmin.service.FaqService;
import ru.atc.jadmin.web.rest.util.HeaderUtil;
import ru.atc.jadmin.service.dto.FaqDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Faq.
 */
@RestController
@RequestMapping("/api")
public class FaqResource {

    private final Logger log = LoggerFactory.getLogger(FaqResource.class);
        
    @Inject
    private FaqService faqService;

    /**
     * POST  /faqs : Create a new faq.
     *
     * @param faqDTO the faqDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new faqDTO, or with status 400 (Bad Request) if the faq has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/faqs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FaqDTO> createFaq(@RequestBody FaqDTO faqDTO) throws URISyntaxException {
        log.debug("REST request to save Faq : {}", faqDTO);
        if (faqDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("faq", "idexists", "A new faq cannot already have an ID")).body(null);
        }
        FaqDTO result = faqService.save(faqDTO);
        return ResponseEntity.created(new URI("/api/faqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("faq", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /faqs : Updates an existing faq.
     *
     * @param faqDTO the faqDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated faqDTO,
     * or with status 400 (Bad Request) if the faqDTO is not valid,
     * or with status 500 (Internal Server Error) if the faqDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/faqs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FaqDTO> updateFaq(@RequestBody FaqDTO faqDTO) throws URISyntaxException {
        log.debug("REST request to update Faq : {}", faqDTO);
        if (faqDTO.getId() == null) {
            return createFaq(faqDTO);
        }
        FaqDTO result = faqService.save(faqDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("faq", faqDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /faqs : get all the faqs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of faqs in body
     */
    @RequestMapping(value = "/faqs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FaqDTO> getAllFaqs() {
        log.debug("REST request to get all Faqs");
        return faqService.findAll();
    }

    /**
     * GET  /faqs/:id : get the "id" faq.
     *
     * @param id the id of the faqDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the faqDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/faqs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FaqDTO> getFaq(@PathVariable Long id) {
        log.debug("REST request to get Faq : {}", id);
        FaqDTO faqDTO = faqService.findOne(id);
        return Optional.ofNullable(faqDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /faqs/:id : delete the "id" faq.
     *
     * @param id the id of the faqDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/faqs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        log.debug("REST request to delete Faq : {}", id);
        faqService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("faq", id.toString())).build();
    }

    /**
     * SEARCH  /_search/faqs?query=:query : search for the faq corresponding
     * to the query.
     *
     * @param query the query of the faq search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/faqs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FaqDTO> searchFaqs(@RequestParam String query) {
        log.debug("REST request to search Faqs for query {}", query);
        return faqService.search(query);
    }


}
