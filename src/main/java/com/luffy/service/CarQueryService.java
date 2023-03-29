package com.luffy.service;

import com.luffy.domain.*; // for static metamodels
import com.luffy.domain.Car;
import com.luffy.repository.CarRepository;
import com.luffy.service.criteria.CarCriteria;
import com.luffy.service.dto.CarDTO;
import com.luffy.service.mapper.CarMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Car} entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarDTO} or a {@link Page} of {@link CarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarQueryService extends QueryService<Car> {

    private final Logger log = LoggerFactory.getLogger(CarQueryService.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarQueryService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    /**
     * Return a {@link List} of {@link CarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarDTO> findByCriteria(CarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carMapper.toDto(carRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarDTO> findByCriteria(CarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page).map(carMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert {@link CarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Car_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Car_.name));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Car_.model));
            }
            if (criteria.getPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlate(), Car_.plate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Car_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBrandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBrandId(), root -> root.join(Car_.brand, JoinType.LEFT).get(Brand_.id))
                    );
            }
        }
        return specification;
    }
}
