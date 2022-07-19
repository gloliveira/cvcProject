package com.cvc.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvc.domain.entities.Account;
import com.cvc.domain.entities.Transfer;
import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.repositories.AccountRepository;
import com.cvc.domain.repositories.TransferRepository;
import com.cvc.domain.util.TypeTransfer;
import com.cvc.web.usecase.TransferUseCase;

@Service
public class TransferService implements TransferUseCase {

    private final TransferTypeService transferTypeService;
    private final TaxService taxService;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(TransferTypeService transferTypeService, TaxService taxService,TransferRepository transferRepository,AccountRepository accountRepository){
        this.transferRepository = transferRepository;
        this.taxService = taxService;
        this.transferTypeService = transferTypeService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<Transfer> listSchedules(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Transfer schedule(Transfer transfer) {
        TypeTransfer typeTransfer = transferTypeService.defineTransferType(transfer.getTransferDate());
        transfer.setTypeTransfer(typeTransfer);
        transfer.setTaxAmount(taxService.calcTaxAmount(transfer));
        if(!transfer.getOrigin().getNumber().equals(transfer.getDestination().getNumber())){
            this.validateAccount(transfer.getOrigin());
            this.validateAccount(transfer.getDestination());
            return transferRepository.save(transfer);
        }
        throw new BusinessException("Transfer should have different origin and destination");
    }

    private void validateAccount(Account account){
        String accountNumber = account.getNumber();
        if(!accountRepository.findById(accountNumber).isPresent()){
            throw new BusinessException("""
                    Invalid account: %s 
                    """.formatted(accountNumber));
        }
    }
}
