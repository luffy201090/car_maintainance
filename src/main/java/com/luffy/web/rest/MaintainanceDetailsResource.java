package com.luffy.web.rest;

import com.luffy.repository.MaintainanceDetailsRepository;
import com.luffy.service.MaintainanceDetailsQueryService;
import com.luffy.service.MaintainanceDetailsService;
import com.luffy.service.criteria.MaintainanceDetailsCriteria;
import com.luffy.service.dto.MaintainanceDetailsDTO;
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
 * REST controller for managing {@link com.luffy.domain.MaintainanceDetails}.
 */
@RestController
@RequestMapping("/api")
public class MaintainanceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MaintainanceDetailsResource.class);

    private static final String ENTITY_NAME = "maintainanceDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaintainanceDetailsService maintainanceDetailsService;

    private final MaintainanceDetailsRepository maintainanceDetailsRepository;

    private final MaintainanceDetailsQueryService maintainanceDetailsQueryService;

    public MaintainanceDetailsResource(
        MaintainanceDetailsService maintainanceDetailsService,
        MaintainanceDetailsRepository maintainanceDetailsRepository,
        MaintainanceDetailsQueryService maintainanceDetailsQueryService
    ) {
        this.maintainanceDetailsService = maintainanceDetailsService;
        this.maintainanceDetailsRepository = maintainanceDetailsRepository;
        this.maintainanceDetailsQueryService = maintainanceDetailsQueryService;
    }

    /**
     * {@code POST  /maintainance-details} : Create a new maintainanceDetails.
     *
     * @param maintainanceDetailsDTO the maintainanceDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintainanceDetailsDTO, or with status {@code 400 (Bad Request)} if the maintainanceDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maintainance-details")
    public ResponseEntity<MaintainanceDetailsDTO> createMaintainanceDetails(
        @Valid @RequestBody MaintainanceDetailsDTO maintainanceDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MaintainanceDetails : {}", maintainanceDetailsDTO);
        if (maintainanceDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintainanceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaintainanceDetailsDTO result = maintainanceDetailsService.save(maintainanceDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/maintainance-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maintainance-details/:id} : Updates an existing maintainanceDetails.
     *
     * @param id the id of the maintainanceDetailsDTO to save.
     * @param maintainanceDetailsDTO the maintainanceDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintainanceDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the maintainanceDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintainanceDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maintainance-details/{id}")
    public ResponseEntity<MaintainanceDetailsDTO> updateMaintainanceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaintainanceDetailsDTO maintainanceDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaintainanceDetails : {}, {}", id, maintainanceDetailsDTO);
        if (maintainanceDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintainanceDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintainanceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaintainanceDetailsDTO result = maintainanceDetailsService.update(maintainanceDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintainanceDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /maintainance-details/:id} : Partial updates given fields of an existing maintainanceDetails, field will ignore if it is null
     *
     * @param id the id of the maintainanceDetailsDTO to save.
     * @param maintainanceDetailsDTO the maintainanceDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintainanceDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the maintainanceDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintainanceDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintainanceDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/maintainance-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaintainanceDetailsDTO> partialUpdateMaintainanceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaintainanceDetailsDTO maintainanceDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaintainanceDetails partially : {}, {}", id, maintainanceDetailsDTO);
        if (maintainanceDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintainanceDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintainanceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintainanceDetailsDTO> result = maintainanceDetailsService.partialUpdate(maintainanceDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintainanceDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maintainance-details} : get all the maintainanceDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintainanceDetails in body.
     */
    @GetMapping("/maintainance-details")
    public ResponseEntity<List<MaintainanceDetailsDTO>> getAllMaintainanceDetails(
        MaintainanceDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MaintainanceDetails by criteria: {}", criteria);
        Page<MaintainanceDetailsDTO> page = maintainanceDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintainance-details/count} : count all the maintainanceDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/maintainance-details/count")
    public ResponseEntity<Long> countMaintainanceDetails(MaintainanceDetailsCriteria criteria) {
        log.debug("REST request to count MaintainanceDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintainanceDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintainance-details/:id} : get the "id" maintainanceDetails.
     *
     * @param id the id of the maintainanceDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintainanceDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintainance-details/{id}")
    public ResponseEntity<MaintainanceDetailsDTO> getMaintainanceDetails(@PathVariable Long id) {
        log.debug("REST request to get MaintainanceDetails : {}", id);
        Optional<MaintainanceDetailsDTO> maintainanceDetailsDTO = maintainanceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintainanceDetailsDTO);
    }

    /**
     * {@code DELETE  /maintainance-details/:id} : delete the "id" maintainanceDetails.
     *
     * @param id the id of the maintainanceDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maintainance-details/{id}")
    public ResponseEntity<Void> deleteMaintainanceDetails(@PathVariable Long id) {
        log.debug("REST request to delete MaintainanceDetails : {}", id);
        maintainanceDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
