package kr.ac.skuniv.realestate_batch.repository;

import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

public interface CharterDateRepository extends JpaRepository<CharterDate, Long> {
}
