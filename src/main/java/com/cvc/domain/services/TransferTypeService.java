package com.cvc.domain.services;

import java.time.Duration;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.util.TypeTransfer;

@Service
public class TransferTypeService {

    public TypeTransfer defineTransferType(LocalDate transferDate){
    	
        if(transferDate.isBefore(LocalDate.now())){
            throw new BusinessException("Invalid Date interval");
        }else if(transferDate.isEqual(LocalDate.now())){
            return TypeTransfer.A;
        }else if(Duration.between(LocalDate.now().atStartOfDay(), transferDate.atStartOfDay()).toDays() <= 10){
            return TypeTransfer.B;
        }else if(Duration.between(LocalDate.now().atStartOfDay(), transferDate.atStartOfDay()).toDays() > 10){
            return TypeTransfer.C;
        }else {
        	throw new BusinessException("Invalid Date interval");
        }
    }
}
