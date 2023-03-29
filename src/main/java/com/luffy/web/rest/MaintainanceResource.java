package com.luffy.web.rest;

import com.luffy.repository.MaintainanceRepository;
import com.luffy.service.MaintainanceQueryService;
import com.luffy.service.MaintainanceService;
import com.luffy.service.criteria.MaintainanceCriteria;
import com.luffy.service.dto.MaintainanceDTO;
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
 * REST controller for managing {@link com.luffy.domain.Maintainance}.
 */
@RestController
@RequestMapping("/api")
public class MaintainanceResource {

    private final Logger log = LoggerFactory.getLogger(MaintainanceResource.class);

    private static final String ENTITY_NAME = "maintainance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaintainanceService maintainanceService;

    private final MaintainanceRepository maintainanceRepository;

    private final MaintainanceQueryService maintainanceQueryService;

    public MaintainanceResource(
        MaintainanceService maintainanceService,
        MaintainanceRepository maintainanceRepository,
        MaintainanceQueryService maintainanceQueryService
    ) {
        this.maintainanceService = maintainanceService;
        this.maintainanceRepository = maintainanceRepository;
        this.maintainanceQueryService = maintainanceQueryService;
    }

    /**
     * {@code POST  /maintainances} : Create a new maintainance.
     *
     * @param maintainanceDTO the maintainanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintainanceDTO, or with status {@code 400 (Bad Request)} if the maintainance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maintainances")
    public ResponseEntity<MaintainanceDTO> createMaintainance(@Valid @RequestBody MaintainanceDTO maintainanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save Maintainance : {}", maintainanceDTO);
        if (maintainanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintainance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaintainanceDTO result = maintainanceService.save(maintainanceDTO);
        return ResponseEntity
            .created(new URI("/api/maintainances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maintainances/:id} : Updates an existing maintainance.
     *
     * @param id the id of the maintainanceDTO to save.
     * @param maintainanceDTO the maintainanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintainanceDTO,
     * or with status {@code 400 (Bad Request)} if the maintainanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintainanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maintainances/{id}")
    public ResponseEntity<MaintainanceDTO> updateMaintainance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaintainanceDTO maintainanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Maintainance : {}, {}", id, maintainanceDTO);
        if (maintainanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintainanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintainanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaintainanceDTO result = maintainanceService.update(maintainanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintainanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /maintainances/:id} : Partial updates given fields of an existing maintainance, field will ignore if it is null
     *
     * @param id the id of the maintainanceDTO to save.
     * @param maintainanceDTO the maintainanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintainanceDTO,
     * or with status {@code 400 (Bad Request)} if the maintainanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintainanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintainanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/maintainances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaintainanceDTO> partialUpdateMaintainance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaintainanceDTO maintainanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Maintainance partially : {}, {}", id, maintainanceDTO);
        if (maintainanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintainanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintainanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintainanceDTO> result = maintainanceService.partialUpdate(maintainanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintainanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maintainances} : get all the maintainances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintainances in body.
     */
    @GetMapping("/maintainances")
    public ResponseEntity<List<MaintainanceDTO>> getAllMaintainances(
        MaintainanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Maintainances by criteria: {}", criteria);
        Page<MaintainanceDTO> page = maintainanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintainances/count} : count all the maintainances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/maintainances/count")
    public ResponseEntity<Long> countMaintainances(MaintainanceCriteria criteria) {
        log.debug("REST request to count Maintainances by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintainanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintainances/:id} : get the "id" maintainance.
     *
     * @param id the id of the maintainanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintainanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintainances/{id}")
    public ResponseEntity<MaintainanceDTO> getMaintainance(@PathVariable Long id) {
        log.debug("REST request to get Maintainance : {}", id);
        Optional<MaintainanceDTO> maintainanceDTO = maintainanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintainanceDTO);
    }

    /**
     * {@code DELETE  /maintainances/:id} : delete the "id" maintainance.
     *
     * @param id the id of the maintainanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maintainances/{id}")
    public ResponseEntity<Void> deleteMaintainance(@PathVariable Long id) {
        log.debug("REST request to delete Maintainance : {}", id);
        maintainanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
