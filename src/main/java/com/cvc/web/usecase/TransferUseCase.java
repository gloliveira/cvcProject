package com.cvc.web.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cvc.domain.entities.Transfer;

public interface TransferUseCase {

    Transfer schedule(Transfer transfer);
    Page<Transfer> listSchedules(Pageable pageable);
}
