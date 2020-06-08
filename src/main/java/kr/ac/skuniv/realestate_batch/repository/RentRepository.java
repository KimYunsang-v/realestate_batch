package kr.ac.skuniv.realestate_batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.skuniv.realestate_batch.domain.entity.Rent;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
