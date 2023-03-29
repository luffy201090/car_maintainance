package com.luffy.service;

import com.luffy.service.dto.CarDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.luffy.domain.Car}.
 */
public interface CarService {
    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    CarDTO save(CarDTO carDTO);

    /**
     * Updates a car.
     *
     * @param carDTO the entity to update.
     * @return the persisted entity.
     */
    CarDTO update(CarDTO carDTO);

    /**
     * Partially updates a car.
     *
     * @param carDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarDTO> partialUpdate(CarDTO carDTO);

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarDTO> findAll(Pageable pageable);

    /**
     * Get all the cars with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarDTO> findOne(Long id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
