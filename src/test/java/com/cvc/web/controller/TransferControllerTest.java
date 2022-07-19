package com.cvc.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cvc.domain.exception.BusinessException;
import com.cvc.domain.services.mockfactory.TransferFactory;
import com.cvc.web.usecase.TransferUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferControllerTest {

    @MockBean
    private TransferUseCase transferUseCase;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void postBusinessException(@Autowired MockMvc mvc) throws Exception {

        given(transferUseCase.schedule(any())).willThrow(BusinessException.class);
        var request= TransferFactory.createTransferDTORequest();
        mvc.perform(post("/v1/schedule").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isUnprocessableEntity()).andExpect(content().contentType("application/json"));
    }

    @Test
    void postException(@Autowired MockMvc mvc) throws Exception {

        given(transferUseCase.schedule(any())).willThrow(RuntimeException.class);
        var request= TransferFactory.createTransferDTORequest();
        mvc.perform(post("/v1/schedule").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(request))).andExpect(status().is5xxServerError()).andExpect(content().contentType("application/json"));
    }

    @Test
    void postSucess(@Autowired MockMvc mvc) throws Exception {

        given(transferUseCase.schedule(any())).willReturn(TransferFactory.createTransferComplete());
        var request= TransferFactory.createTransferDTORequest();
        mvc.perform(post("/v1/schedule").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.taxAmount").value(BigDecimal.ONE));
    }
}
