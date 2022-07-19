package com.cvc.web.controller.mapper;

import org.springframework.stereotype.Component;

import com.cvc.domain.entities.Account;
import com.cvc.domain.entities.Transfer;
import com.cvc.web.controller.dto.TransferDTO;

@Component
public class TransferMapper {

    public Transfer convertToModel(TransferDTO transferDTO){
        return new Transfer(
                null,
                transferDTO.getAmount(),
                null,
                transferDTO.getTransferDate(),
                transferDTO.getScheduleDate(),
                new Account (transferDTO.getAccountOrigin(),null,null),
                new Account (transferDTO.getAccountDestination(),null,null),
                null
        );
    }

    public TransferDTO convertToDTO(Transfer transfer){
        TransferDTO transferDTO =  new TransferDTO(
                transfer.getId(),
                transfer.getAmount(),
                null,
                transfer.getTransferDate(),
                transfer.getScheduleDate(),
                transfer.getOrigin().getNumber(),
                transfer.getDestination().getNumber(),
                null
        );
        transferDTO.setTaxAmount(transfer.getTaxAmount());
        return transferDTO;
    }
}
