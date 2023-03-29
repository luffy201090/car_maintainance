package com.luffy.service;

import com.luffy.domain.*; // for static metamodels
import com.luffy.domain.Corporation;
import com.luffy.repository.CorporationRepository;
import com.luffy.service.criteria.CorporationCriteria;
import com.luffy.service.dto.CorporationDTO;
import com.luffy.service.mapper.CorporationMapper;
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
 * Service for executing complex queries for {@link Corporation} entities in the database.
 * The main input is a {@link CorporationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorporationDTO} or a {@link Page} of {@link CorporationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorporationQueryService extends QueryService<Corporation> {

    private final Logger log = LoggerFactory.getLogger(CorporationQueryService.class);

    private final CorporationRepository corporationRepository;

    private final CorporationMapper corporationMapper;

    public CorporationQueryService(CorporationRepository corporationRepository, CorporationMapper corporationMapper) {
        this.corporationRepository = corporationRepository;
        this.corporationMapper = corporationMapper;
    }

    /**
     * Return a {@link List} of {@link CorporationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorporationDTO> findByCriteria(CorporationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Corporation> specification = createSpecification(criteria);
        return corporationMapper.toDto(corporationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorporationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorporationDTO> findByCriteria(CorporationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Corporation> specification = createSpecification(criteria);
        return corporationRepository.findAll(specification, page).map(corporationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorporationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Corporation> specification = createSpecification(criteria);
        return corporationRepository.count(specification);
    }

    /**
     * Function to convert {@link CorporationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Corporation> createSpecification(CorporationCriteria criteria) {
        Specification<Corporation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Corporation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Corporation_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Corporation_.description));
            }
        }
        return specification;
    }
}
