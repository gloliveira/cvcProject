package com.cvc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.cvc.domain.entities.Transfer;
import com.cvc.web.controller.dto.TransferDTO;
import com.cvc.web.controller.mapper.TransferMapper;
import com.cvc.web.usecase.TransferUseCase;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/schedule")
public class TransferController {

    Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final TransferUseCase transferUseCase;
    private final TransferMapper transferMapper;
    private final MessageSource messageResource;

    TransferController(TransferUseCase transferUseCase,TransferMapper transferMapper,MessageSource messageResource){
        this.transferUseCase = transferUseCase;
        this.transferMapper = transferMapper;
        this.messageResource = messageResource;
    }

    @GetMapping
    Page<TransferDTO> getAllSchedules(@RequestParam(value = "page",defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "30") int size){
    	
    	logger.info("Start getAllSchedules");
        var pageable = PageRequest.of(page, size);
        String Language = messageResource.getMessage("country",null,new Locale("US"));
        
        List<TransferDTO> listSchedules = transferUseCase.listSchedules(pageable).stream().toList().stream().map(list ->{
        	TransferDTO transferDTO = new TransferDTO();
        	transferDTO = transferMapper.convertToDTO(list);
        	transferDTO.setLanguage(Language);
            return transferDTO;
        }).collect(Collectors.toList());
        logger.info("finich schedgetAllSchedulesules %s".formatted(listSchedules.size()));
        return  new PageImpl<>(listSchedules,pageable,listSchedules.size());
    }

    @PostMapping
    TransferDTO schedule(@RequestBody TransferDTO transferDTO){
        logger.info("Saving.");
        Transfer model = transferMapper.convertToModel(transferDTO);
        logger.info("Converted");
        Transfer persistedModel = transferUseCase.schedule(model);
        logger.info("PersistedModel %s...".formatted(persistedModel));
        return transferMapper.convertToDTO(persistedModel);
    }

}
