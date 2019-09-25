package kr.ac.skuniv.realestate_batch.repository;

import kr.ac.skuniv.realestate_batch.domain.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
