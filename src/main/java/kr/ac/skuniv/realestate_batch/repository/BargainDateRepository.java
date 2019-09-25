package kr.ac.skuniv.realestate_batch.repository;

import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BargainDateRepository extends JpaRepository<BargainDate, Long> {
}
