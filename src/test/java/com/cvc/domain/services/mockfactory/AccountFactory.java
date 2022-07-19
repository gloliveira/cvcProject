package com.cvc.domain.services.mockfactory;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.cvc.domain.entities.Account;
import com.cvc.domain.entities.Transfer;
import com.cvc.domain.util.TypeTransfer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public class AccountFactory {

    private AccountFactory(){
    }

    public static Account createAccountOrigin(){
        return new Account("884429","João Origin","23443253");
    }

    public static Account createAccountDestination(){
        return new Account("3344223","Maria Destination","123464");
    }
}
