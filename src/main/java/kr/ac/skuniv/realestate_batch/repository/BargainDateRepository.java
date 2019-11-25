package kr.ac.skuniv.realestate_batch.repository;

import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BargainDateRepository extends JpaRepository<BargainDate, Long> {

    @Query(nativeQuery = true,  value = "select * from bargain_date join building_entity on building_entity.id = bargain_date.building_id " +
            "where bargain_date.py_price is null and building_entity.area is not null LIMIT 10000")
    List<BargainDate> findByCount(@Param("offset") long offset);

    List<BargainDate> findByPyPriceIsNull();

}
