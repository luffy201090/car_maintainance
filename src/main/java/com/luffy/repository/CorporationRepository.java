package com.luffy.repository;

import com.luffy.domain.Corporation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Corporation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Long>, JpaSpecificationExecutor<Corporation> {}
