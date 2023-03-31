package com.luffy.repository;

import com.luffy.domain.Maintainance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Maintainance entity.
 */
@Repository
public interface MaintainanceRepository extends JpaRepository<Maintainance, Long>, JpaSpecificationExecutor<Maintainance> {
    @Query("select maintainance from Maintainance maintainance where maintainance.user.login = ?#{principal.preferredUsername}")
    List<Maintainance> findByUserIsCurrentUser();

    default Optional<Maintainance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Maintainance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Maintainance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct maintainance from Maintainance maintainance left join fetch maintainance.car left join fetch maintainance.user",
        countQuery = "select count(distinct maintainance) from Maintainance maintainance"
    )
    Page<Maintainance> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct maintainance from Maintainance maintainance left join fetch maintainance.car left join fetch maintainance.user")
    List<Maintainance> findAllWithToOneRelationships();

    @Query(
        "select maintainance from Maintainance maintainance left join fetch maintainance.car left join fetch maintainance.user where maintainance.id =:id"
    )
    Optional<Maintainance> findOneWithToOneRelationships(@Param("id") Long id);
}
