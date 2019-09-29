package kr.ac.skuniv.realestate_batch.repository;


import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface RentDateRepository extends JpaRepository<RentDate, Long> {
}
