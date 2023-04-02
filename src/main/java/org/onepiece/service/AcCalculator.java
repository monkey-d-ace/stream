package org.onepiece.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("ac")
@Order(1992)
@Slf4j
public class AcCalculator implements Calculator {
    @Override
    public void calculate() {
        log.info("complete AC calculation");
    }
}
