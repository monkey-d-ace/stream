package org.onepiece.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.onepiece.model.Notification;
import org.onepiece.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.util.List;

@Data
@Slf4j
public class ConsumeStreamListener implements StreamListener<String, ObjectRecord<String, Notification>> {
    private RedisTemplate<String, Object> redisTemplate;
    private List<Calculator> calculators;
    public ConsumeStreamListener(List<Calculator> calculators) {
        this.calculators = calculators;
    }
    @Override
    public void onMessage(ObjectRecord<String, Notification> message) {
        String topic = message.getStream();
        RecordId recordId = message.getId();
        Notification notification = message.getValue();
        log.info("receive a notification topic: [{}], id: [{}], value: [{}]", topic, recordId, notification);
        for (Calculator calculator : calculators) {
            calculator.calculate();
        }
    }
}
