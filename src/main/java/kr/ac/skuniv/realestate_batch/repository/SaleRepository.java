package kr.ac.skuniv.realestate_batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.skuniv.realestate_batch.domain.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
