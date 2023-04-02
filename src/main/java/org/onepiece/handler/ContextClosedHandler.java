package org.onepiece.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        executor.destroy();
    }
}
