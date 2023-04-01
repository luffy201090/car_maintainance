package com.luffy.service;

import com.luffy.domain.*; // for static metamodels
import com.luffy.domain.Maintainance;
import com.luffy.repository.MaintainanceRepository;
import com.luffy.service.criteria.MaintainanceCriteria;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.mapper.MaintainanceMapper;
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
 * Service for executing complex queries for {@link Maintainance} entities in the database.
 * The main input is a {@link MaintainanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MaintainanceDTO} or a {@link Page} of {@link MaintainanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintainanceQueryService extends QueryService<Maintainance> {

    private final Logger log = LoggerFactory.getLogger(MaintainanceQueryService.class);

    private final MaintainanceRepository maintainanceRepository;

    private final MaintainanceMapper maintainanceMapper;

    private final UserService userService;

    public MaintainanceQueryService(MaintainanceRepository maintainanceRepository, MaintainanceMapper maintainanceMapper, UserService userService) {
        this.maintainanceRepository = maintainanceRepository;
        this.maintainanceMapper = maintainanceMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link MaintainanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MaintainanceDTO> findByCriteria(MaintainanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Maintainance> specification = createSpecification(criteria);
        return maintainanceMapper.toDto(maintainanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MaintainanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintainanceDTO> findByCriteria(MaintainanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Maintainance> specification = createSpecification(criteria);
        return maintainanceRepository.findAll(specification, page).map(maintainanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintainanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Maintainance> specification = createSpecification(criteria);
        return maintainanceRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintainanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Maintainance> createSpecification(MaintainanceCriteria criteria) {
        Specification<Maintainance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Maintainance_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLevel(), Maintainance_.level));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Maintainance_.price));
            }
            if (criteria.getPlace() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlace(), Maintainance_.place));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Maintainance_.date));
            }
            if (criteria.getCarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCarId(), root -> root.join(Maintainance_.car, JoinType.LEFT).get(Car_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Maintainance_.user, JoinType.LEFT).get(User_.id))
                    );
            } else {
                specification = specification.and((root, query, cb) -> cb.equal(root.join(Maintainance_.user, JoinType.LEFT).get(User_.id), userService.getCurrentUser().getId()));
            }        }
        return specification;
    }
}
