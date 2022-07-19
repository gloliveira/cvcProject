package com.cvc.domain.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cvc.domain.entities.Transfer;
import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.services.TaxService;
import com.cvc.domain.services.mockfactory.TransferFactory;
import com.cvc.domain.util.TypeTransfer;

@ExtendWith(SpringExtension.class)
public class TaxServiceTest {

    private TaxService taxService;

    @BeforeEach
    void setUp() {
        taxService = new TaxService();
    }

    @Test
    public void calcTaxAmountWithTransferTypeNull() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10();
        Assertions.assertThrows(BusinessException.class, () -> taxService.calcTaxAmount(transfer));
    }

    @Test
    public void calcTaxAmountWithTransferTypeA() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10();
        transfer.setTypeTransfer(TypeTransfer.A);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(3.30).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeB() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(9);
        transfer.setTypeTransfer(TypeTransfer.B);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(108),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCLessThen10() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(10);
        transfer.setTypeTransfer(TypeTransfer.C);
        Assertions.assertThrows(BusinessException.class, () -> taxService.calcTaxAmount(transfer));
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen10LessOrEqualThen20() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(15);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.80).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCIntervalEqual20() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(20);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.80).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen20LessOrEqualThen30() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(25);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.60).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCIntervalEqual30() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(30);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.60).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen30LessOrEqualThen40() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(35);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.40).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCIntervalEqual40() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(40);
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(0.40).setScale(2),taxAmount);
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen40AndLessThen100000() {
        Transfer transfer = TransferFactory.createTransferDefaultAmount10IntervalDate(41);
        transfer.setTypeTransfer(TypeTransfer.C);
        Assertions.assertThrows(BusinessException.class, () -> taxService.calcTaxAmount(transfer));
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen40AndEqual100000() {
        Transfer transfer = TransferFactory.createTransferIntervalDate(41,BigDecimal.valueOf(100000));
        transfer.setTypeTransfer(TypeTransfer.C);
        Assertions.assertThrows(BusinessException.class, () -> taxService.calcTaxAmount(transfer));
    }

    @Test
    public void calcTaxAmountWithTransferTypeCGreaterThen40AndGreaterThen100000() {
        Transfer transfer = TransferFactory.createTransferIntervalDate(41,BigDecimal.valueOf(100001));
        transfer.setTypeTransfer(TypeTransfer.C);
        BigDecimal taxAmount = taxService.calcTaxAmount(transfer);
        Assertions.assertEquals(BigDecimal.valueOf(2000.02).setScale(2),taxAmount);
    }
}
