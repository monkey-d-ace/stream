package org.onepiece.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("acd")
@Order(1993)
@Slf4j
public class AcdCalculator implements Calculator {
    @Override
    public void calculate() {
        log.info("complete acd calculation");
    }
}
