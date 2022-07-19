package com.cvc.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cvc.domain.entities.Transfer;
import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.repositories.AccountRepository;
import com.cvc.domain.repositories.TransferRepository;
import com.cvc.domain.services.TaxService;
import com.cvc.domain.services.TransferService;
import com.cvc.domain.services.TransferTypeService;
import com.cvc.domain.services.mockfactory.TransferFactory;
import com.cvc.domain.util.TypeTransfer;

@ExtendWith(SpringExtension.class)
public class TransferServiceTest {

    @MockBean
    private TransferTypeService transferTypeService;
    @MockBean
    private TaxService taxService;
    @MockBean
    private TransferRepository transferRepository;
    @MockBean
    private AccountRepository accountRepository;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(transferTypeService,taxService,transferRepository,accountRepository);
    }

    @Test
    public void scheduleWithPastDate() {
        LocalDate currentDate = LocalDate.now();
        Transfer transfer = TransferFactory.createTransferDate(currentDate.minusDays(1),0);
        BDDMockito.given(transferTypeService.defineTransferType(currentDate.minusDays(1))).willThrow(BusinessException.class);
        Assertions.assertThrows(BusinessException.class, () -> transferService.schedule(transfer));
    }

    @Test
    public void scheduleTestInvalidTax() {
        Transfer transfer = TransferFactory.createTransferDate(LocalDate.now(),0);
        LocalDate currentDate = LocalDate.now();
        BDDMockito.given(transferTypeService.defineTransferType(currentDate)).willReturn(TypeTransfer.A);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willThrow(BusinessException.class);
        Assertions.assertThrows(BusinessException.class, () -> transferService.schedule(transfer));
    }

    @Test
    public void scheduleSameOriginAndDestination() {
        Transfer transfer = TransferFactory.createTransferSameOriginAndDestination();
        BDDMockito.given(transferTypeService.defineTransferType(transfer.getTransferDate())).willReturn(TypeTransfer.A);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(3.30).setScale(2));
        Assertions.assertThrows(BusinessException.class, () -> transferService.schedule(transfer));
    }

    @Test
    public void scheduleInvalidOrigin() {
        Transfer transfer = TransferFactory.createTransferDate(LocalDate.now(),0);
        BDDMockito.given(transferTypeService.defineTransferType(transfer.getTransferDate())).willReturn(TypeTransfer.A);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(3.30).setScale(2));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> transferService.schedule(transfer));
    }

    @Test
    public void scheduleInvalidDestination() {
        Transfer transfer = TransferFactory.createTransferDate(LocalDate.now(),0);
        BDDMockito.given(transferTypeService.defineTransferType(transfer.getTransferDate())).willReturn(TypeTransfer.A);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(3.30).setScale(2));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.of(transfer.getOrigin()));
        BDDMockito.given(accountRepository.findById(transfer.getDestination().getNumber())).willReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> transferService.schedule(transfer));
    }

    @Test
    public void scheduleCaseA() {
        LocalDate currentDate = LocalDate.now();
        Transfer transfer = TransferFactory.createTransferDate(currentDate,0);

        BDDMockito.given(transferTypeService.defineTransferType(currentDate)).willReturn(TypeTransfer.A);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(3.30).setScale(2));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.of(transfer.getOrigin()));
        BDDMockito.given(accountRepository.findById(transfer.getDestination().getNumber())).willReturn(Optional.of(transfer.getDestination()));
        BDDMockito.given(transferRepository.save(transfer)).willReturn(transfer);

        Transfer persistedTransfer = transferService.schedule(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(3.30).setScale(2),persistedTransfer.getTaxAmount());
        Assertions.assertEquals(TypeTransfer.A,persistedTransfer.getTypeTransfer());
    }

    @Test
    public void scheduleCaseB() {
        LocalDate currentDate = LocalDate.now().plusDays(1);
        Transfer transfer = TransferFactory.createTransferDate(currentDate,9);

        BDDMockito.given(transferTypeService.defineTransferType(currentDate)).willReturn(TypeTransfer.B);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(108));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.of(transfer.getOrigin()));
        BDDMockito.given(accountRepository.findById(transfer.getDestination().getNumber())).willReturn(Optional.of(transfer.getDestination()));
        BDDMockito.given(transferRepository.save(transfer)).willReturn(transfer);

        Transfer persistedTransfer = transferService.schedule(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(108),persistedTransfer.getTaxAmount());
        Assertions.assertEquals(TypeTransfer.B,persistedTransfer.getTypeTransfer());
    }

    @Test
    public void scheduleCaseBSameDate() {
        LocalDate currentDate = LocalDate.now().plusDays(1);
        Transfer transfer = TransferFactory.createTransferDate(currentDate,0);

        BDDMockito.given(transferTypeService.defineTransferType(currentDate)).willReturn(TypeTransfer.B);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(12));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.of(transfer.getOrigin()));
        BDDMockito.given(accountRepository.findById(transfer.getDestination().getNumber())).willReturn(Optional.of(transfer.getDestination()));
        BDDMockito.given(transferRepository.save(transfer)).willReturn(transfer);

        Transfer persistedTransfer = transferService.schedule(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(12),persistedTransfer.getTaxAmount());
        Assertions.assertEquals(TypeTransfer.B,persistedTransfer.getTypeTransfer());
    }

    @Test
    public void scheduleCaseC() {
        LocalDate currentDate = LocalDate.now();
        Transfer transfer = TransferFactory.createTransferDate(currentDate,15);

        BDDMockito.given(transferTypeService.defineTransferType(currentDate)).willReturn(TypeTransfer.C);
        BDDMockito.given(taxService.calcTaxAmount(transfer)).willReturn(BigDecimal.valueOf(0.80).setScale(2));
        BDDMockito.given(accountRepository.findById(transfer.getOrigin().getNumber())).willReturn(Optional.of(transfer.getOrigin()));
        BDDMockito.given(accountRepository.findById(transfer.getDestination().getNumber())).willReturn(Optional.of(transfer.getDestination()));
        BDDMockito.given(transferRepository.save(transfer)).willReturn(transfer);

        Transfer persistedTransfer = transferService.schedule(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.80).setScale(2),persistedTransfer.getTaxAmount());
        Assertions.assertEquals(TypeTransfer.C,persistedTransfer.getTypeTransfer());
    }
}
