package com.cvc.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cvc.domain.entities.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
