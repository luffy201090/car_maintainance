package com.luffy.service;

import com.luffy.service.dto.CorporationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.luffy.domain.Corporation}.
 */
public interface CorporationService {
    /**
     * Save a corporation.
     *
     * @param corporationDTO the entity to save.
     * @return the persisted entity.
     */
    CorporationDTO save(CorporationDTO corporationDTO);

    /**
     * Updates a corporation.
     *
     * @param corporationDTO the entity to update.
     * @return the persisted entity.
     */
    CorporationDTO update(CorporationDTO corporationDTO);

    /**
     * Partially updates a corporation.
     *
     * @param corporationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CorporationDTO> partialUpdate(CorporationDTO corporationDTO);

    /**
     * Get all the corporations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CorporationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" corporation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorporationDTO> findOne(Long id);

    /**
     * Delete the "id" corporation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
