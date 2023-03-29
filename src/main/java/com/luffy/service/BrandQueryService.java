package com.luffy.service;

import com.luffy.domain.*; // for static metamodels
import com.luffy.domain.Brand;
import com.luffy.repository.BrandRepository;
import com.luffy.service.criteria.BrandCriteria;
import com.luffy.service.dto.BrandDTO;
import com.luffy.service.mapper.BrandMapper;
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
 * Service for executing complex queries for {@link Brand} entities in the database.
 * The main input is a {@link BrandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BrandDTO} or a {@link Page} of {@link BrandDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BrandQueryService extends QueryService<Brand> {

    private final Logger log = LoggerFactory.getLogger(BrandQueryService.class);

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandQueryService(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    /**
     * Return a {@link List} of {@link BrandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BrandDTO> findByCriteria(BrandCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Brand> specification = createSpecification(criteria);
        return brandMapper.toDto(brandRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BrandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BrandDTO> findByCriteria(BrandCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Brand> specification = createSpecification(criteria);
        return brandRepository.findAll(specification, page).map(brandMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BrandCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Brand> specification = createSpecification(criteria);
        return brandRepository.count(specification);
    }

    /**
     * Function to convert {@link BrandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Brand> createSpecification(BrandCriteria criteria) {
        Specification<Brand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Brand_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Brand_.name));
            }
            if (criteria.getCorporationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCorporationId(),
                            root -> root.join(Brand_.corporation, JoinType.LEFT).get(Corporation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
