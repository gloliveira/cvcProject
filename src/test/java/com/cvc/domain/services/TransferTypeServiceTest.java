package com.cvc.domain.services;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.services.TransferTypeService;
import com.cvc.domain.util.TypeTransfer;

@ExtendWith(SpringExtension.class)
public class TransferTypeServiceTest {

    private TransferTypeService transferTypeService;

    @BeforeEach
    void setUp() {
        transferTypeService = new TransferTypeService();
    }

    @Test
    public void defineTaxDateIntervalInvalidException() {
        Assertions.assertThrows(BusinessException.class, () -> transferTypeService.defineTransferType(LocalDate.now().plusDays(-1)));
    }

    @Test
    public void defineTaxTypeA() {
        Assertions.assertEquals(TypeTransfer.A,transferTypeService.defineTransferType(LocalDate.now()));
    }

    @Test
    public void defineTaxTypeB() {
        Assertions.assertEquals(TypeTransfer.B,transferTypeService.defineTransferType(LocalDate.now().plusDays(1)));
    }

    @Test
    public void defineTaxTypeBLimit10() {
        Assertions.assertEquals(TypeTransfer.B,transferTypeService.defineTransferType(LocalDate.now().plusDays(10)));
    }

    @Test
    public void defineTaxTypeC() {
        Assertions.assertEquals(TypeTransfer.C,transferTypeService.defineTransferType(LocalDate.now().plusDays(11)));
    }
}
