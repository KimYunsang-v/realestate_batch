package kr.ac.skuniv.realestate_batch.repository;

import java.util.Optional;

import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingEntityRepository extends JpaRepository<BuildingEntity, Long> {
    Optional<BuildingEntity> findByCityAndGroopAndBuildingNumAndFloor(Integer city, Integer groop, String buildingNum, Integer floor);
}
