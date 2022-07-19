package com.cvc.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cvc.domain.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
