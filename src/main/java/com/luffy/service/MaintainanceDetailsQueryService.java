package com.luffy.service;

import com.luffy.domain.*; // for static metamodels
import com.luffy.domain.MaintainanceDetails;
import com.luffy.repository.MaintainanceDetailsRepository;
import com.luffy.service.criteria.MaintainanceDetailsCriteria;
import com.luffy.service.dto.MaintainanceDetailsDTO;
import com.luffy.service.mapper.MaintainanceDetailsMapper;
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
 * Service for executing complex queries for {@link MaintainanceDetails} entities in the database.
 * The main input is a {@link MaintainanceDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MaintainanceDetailsDTO} or a {@link Page} of {@link MaintainanceDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintainanceDetailsQueryService extends QueryService<MaintainanceDetails> {

    private final Logger log = LoggerFactory.getLogger(MaintainanceDetailsQueryService.class);

    private final MaintainanceDetailsRepository maintainanceDetailsRepository;

    private final MaintainanceDetailsMapper maintainanceDetailsMapper;

    public MaintainanceDetailsQueryService(
        MaintainanceDetailsRepository maintainanceDetailsRepository,
        MaintainanceDetailsMapper maintainanceDetailsMapper
    ) {
        this.maintainanceDetailsRepository = maintainanceDetailsRepository;
        this.maintainanceDetailsMapper = maintainanceDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link MaintainanceDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MaintainanceDetailsDTO> findByCriteria(MaintainanceDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MaintainanceDetails> specification = createSpecification(criteria);
        return maintainanceDetailsMapper.toDto(maintainanceDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MaintainanceDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintainanceDetailsDTO> findByCriteria(MaintainanceDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaintainanceDetails> specification = createSpecification(criteria);
        return maintainanceDetailsRepository.findAll(specification, page).map(maintainanceDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintainanceDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaintainanceDetails> specification = createSpecification(criteria);
        return maintainanceDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintainanceDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaintainanceDetails> createSpecification(MaintainanceDetailsCriteria criteria) {
        Specification<MaintainanceDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaintainanceDetails_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MaintainanceDetails_.name));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), MaintainanceDetails_.action));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), MaintainanceDetails_.price));
            }
            if (criteria.getMaintainanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMaintainanceId(),
                            root -> root.join(MaintainanceDetails_.maintainance, JoinType.LEFT).get(Maintainance_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
