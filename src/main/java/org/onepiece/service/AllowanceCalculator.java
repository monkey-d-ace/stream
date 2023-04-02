package org.onepiece.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("allowance")
@Order(1995)
@Slf4j
public class AllowanceCalculator implements Calculator {
    @Override
    public void calculate() {
        log.info("complete allowance calculation");
    }
}
