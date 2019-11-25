package kr.ac.skuniv.realestate_batch.repository;

import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public interface CharterDateRepository extends JpaRepository<CharterDate, Long> {

    @Query(nativeQuery = true,  value = "select * from charter_date join building_entity on building_entity.id = charter_date.building_id " +
            "where charter_date.py_price is null and building_entity.area is not null LIMIT 10000")
    List<CharterDate> findByCount(@Param("offset") long offset);

}
