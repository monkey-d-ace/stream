package org.onepiece.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("creditLoss")
@Order(1994)
@Slf4j
public class CreditLossCalculator implements Calculator {

    @Override
    public void calculate() {
        log.info("complete credit loss calculation");
    }
}
