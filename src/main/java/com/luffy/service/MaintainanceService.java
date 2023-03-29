package com.luffy.service;

import com.luffy.service.dto.MaintainanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.luffy.domain.Maintainance}.
 */
public interface MaintainanceService {
    /**
     * Save a maintainance.
     *
     * @param maintainanceDTO the entity to save.
     * @return the persisted entity.
     */
    MaintainanceDTO save(MaintainanceDTO maintainanceDTO);

    /**
     * Updates a maintainance.
     *
     * @param maintainanceDTO the entity to update.
     * @return the persisted entity.
     */
    MaintainanceDTO update(MaintainanceDTO maintainanceDTO);

    /**
     * Partially updates a maintainance.
     *
     * @param maintainanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintainanceDTO> partialUpdate(MaintainanceDTO maintainanceDTO);

    /**
     * Get all the maintainances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintainanceDTO> findAll(Pageable pageable);

    /**
     * Get all the maintainances with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintainanceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" maintainance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintainanceDTO> findOne(Long id);

    /**
     * Delete the "id" maintainance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
