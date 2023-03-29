package com.luffy.web.rest;

import com.luffy.repository.CorporationRepository;
import com.luffy.service.CorporationQueryService;
import com.luffy.service.CorporationService;
import com.luffy.service.criteria.CorporationCriteria;
import com.luffy.service.dto.CorporationDTO;
import com.luffy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.luffy.domain.Corporation}.
 */
@RestController
@RequestMapping("/api")
public class CorporationResource {

    private final Logger log = LoggerFactory.getLogger(CorporationResource.class);

    private static final String ENTITY_NAME = "corporation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorporationService corporationService;

    private final CorporationRepository corporationRepository;

    private final CorporationQueryService corporationQueryService;

    public CorporationResource(
        CorporationService corporationService,
        CorporationRepository corporationRepository,
        CorporationQueryService corporationQueryService
    ) {
        this.corporationService = corporationService;
        this.corporationRepository = corporationRepository;
        this.corporationQueryService = corporationQueryService;
    }

    /**
     * {@code POST  /corporations} : Create a new corporation.
     *
     * @param corporationDTO the corporationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corporationDTO, or with status {@code 400 (Bad Request)} if the corporation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corporations")
    public ResponseEntity<CorporationDTO> createCorporation(@Valid @RequestBody CorporationDTO corporationDTO) throws URISyntaxException {
        log.debug("REST request to save Corporation : {}", corporationDTO);
        if (corporationDTO.getId() != null) {
            throw new BadRequestAlertException("A new corporation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorporationDTO result = corporationService.save(corporationDTO);
        return ResponseEntity
            .created(new URI("/api/corporations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /corporations/:id} : Updates an existing corporation.
     *
     * @param id the id of the corporationDTO to save.
     * @param corporationDTO the corporationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporationDTO,
     * or with status {@code 400 (Bad Request)} if the corporationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corporationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corporations/{id}")
    public ResponseEntity<CorporationDTO> updateCorporation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorporationDTO corporationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Corporation : {}, {}", id, corporationDTO);
        if (corporationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CorporationDTO result = corporationService.update(corporationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corporationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /corporations/:id} : Partial updates given fields of an existing corporation, field will ignore if it is null
     *
     * @param id the id of the corporationDTO to save.
     * @param corporationDTO the corporationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporationDTO,
     * or with status {@code 400 (Bad Request)} if the corporationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the corporationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the corporationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/corporations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CorporationDTO> partialUpdateCorporation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorporationDTO corporationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Corporation partially : {}, {}", id, corporationDTO);
        if (corporationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorporationDTO> result = corporationService.partialUpdate(corporationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corporationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /corporations} : get all the corporations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corporations in body.
     */
    @GetMapping("/corporations")
    public ResponseEntity<List<CorporationDTO>> getAllCorporations(
        CorporationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Corporations by criteria: {}", criteria);
        Page<CorporationDTO> page = corporationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corporations/count} : count all the corporations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/corporations/count")
    public ResponseEntity<Long> countCorporations(CorporationCriteria criteria) {
        log.debug("REST request to count Corporations by criteria: {}", criteria);
        return ResponseEntity.ok().body(corporationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /corporations/:id} : get the "id" corporation.
     *
     * @param id the id of the corporationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corporationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corporations/{id}")
    public ResponseEntity<CorporationDTO> getCorporation(@PathVariable Long id) {
        log.debug("REST request to get Corporation : {}", id);
        Optional<CorporationDTO> corporationDTO = corporationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corporationDTO);
    }

    /**
     * {@code DELETE  /corporations/:id} : delete the "id" corporation.
     *
     * @param id the id of the corporationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corporations/{id}")
    public ResponseEntity<Void> deleteCorporation(@PathVariable Long id) {
        log.debug("REST request to delete Corporation : {}", id);
        corporationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
