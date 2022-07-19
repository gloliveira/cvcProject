package com.cvc.domain.services;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.cvc.domain.entities.Transfer;
import com.cvc.domain.exception.BusinessException;

@Service
public class TaxService {

    public BigDecimal calcTaxAmount(Transfer trans){
    	
        long difference = Duration.between(trans.getTransferDate().atStartOfDay(), trans.getScheduleDate().atStartOfDay()).toDays();
        
        if ( trans.getTypeTransfer() == null || trans.getTypeTransfer().equals("") ) {
        	throw new BusinessException("Undefined");
        }
        
        
        switch (trans.getTypeTransfer()) {
        	case A:
        		return BigDecimal.valueOf(3L).add(trans.getAmount().multiply(BigDecimal.valueOf(0.03)));
            case B:
            	if(difference == 0){
                	return BigDecimal.valueOf(12L);
                }else {
                    return BigDecimal.valueOf(12L).multiply(BigDecimal.valueOf(difference));
                }
            case C:
                if (difference > 10 && difference <= 20) {
                	return trans.getAmount().multiply(BigDecimal.valueOf(0.08));
                }else if (difference > 20 && difference <= 30) {
                        return trans.getAmount().multiply(BigDecimal.valueOf(0.06));
                }else if (difference > 30 && difference <= 40) {
                        return trans.getAmount().multiply(BigDecimal.valueOf(0.04));
                }else if (difference > 40 && trans.getAmount().longValue() > 100000) {
                        return trans.getAmount().multiply(BigDecimal.valueOf(0.02));
                }
            }
        throw new BusinessException("Undefined");
        }
}
