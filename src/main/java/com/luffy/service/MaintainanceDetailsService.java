package com.luffy.service;

import com.luffy.service.dto.MaintainanceDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.luffy.domain.MaintainanceDetails}.
 */
public interface MaintainanceDetailsService {
    /**
     * Save a maintainanceDetails.
     *
     * @param maintainanceDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    MaintainanceDetailsDTO save(MaintainanceDetailsDTO maintainanceDetailsDTO);

    /**
     * Updates a maintainanceDetails.
     *
     * @param maintainanceDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    MaintainanceDetailsDTO update(MaintainanceDetailsDTO maintainanceDetailsDTO);

    /**
     * Partially updates a maintainanceDetails.
     *
     * @param maintainanceDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintainanceDetailsDTO> partialUpdate(MaintainanceDetailsDTO maintainanceDetailsDTO);

    /**
     * Get all the maintainanceDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintainanceDetailsDTO> findAll(Pageable pageable);

    /**
     * Get all the maintainanceDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintainanceDetailsDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" maintainanceDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintainanceDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" maintainanceDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
