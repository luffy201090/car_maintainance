package com.luffy.repository;

import com.luffy.domain.MaintainanceDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaintainanceDetails entity.
 */
@Repository
public interface MaintainanceDetailsRepository
    extends JpaRepository<MaintainanceDetails, Long>, JpaSpecificationExecutor<MaintainanceDetails> {
    @Query(
        "select maintainanceDetails from MaintainanceDetails maintainanceDetails where maintainanceDetails.user.login = ?#{principal.preferredUsername}"
    )
    List<MaintainanceDetails> findByUserIsCurrentUser();

    default Optional<MaintainanceDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MaintainanceDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MaintainanceDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct maintainanceDetails from MaintainanceDetails maintainanceDetails left join fetch maintainanceDetails.maintainance left join fetch maintainanceDetails.user",
        countQuery = "select count(distinct maintainanceDetails) from MaintainanceDetails maintainanceDetails"
    )
    Page<MaintainanceDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct maintainanceDetails from MaintainanceDetails maintainanceDetails left join fetch maintainanceDetails.maintainance left join fetch maintainanceDetails.user"
    )
    List<MaintainanceDetails> findAllWithToOneRelationships();

    @Query(
        "select maintainanceDetails from MaintainanceDetails maintainanceDetails left join fetch maintainanceDetails.maintainance left join fetch maintainanceDetails.user where maintainanceDetails.id =:id"
    )
    Optional<MaintainanceDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
