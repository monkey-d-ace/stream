package org.onepiece.config;

import org.onepiece.listener.ConsumeStreamListener;
import org.onepiece.model.Notification;
import org.onepiece.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.onepiece.constant.Constants.TOPIC;

@Configuration
public class RedisStreamConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private List<Calculator> calculatorList;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors);
        executor.setKeepAliveSeconds(0);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(3);
        return executor;
    }
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, ObjectRecord<String, Notification>> streamMessageListenerContainer(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, Notification>>
                options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().batchSize(10)
                .executor(threadPoolTaskExecutor)
                .keySerializer(RedisSerializer.string())
                .hashKeySerializer(RedisSerializer.string())
                .hashValueSerializer(RedisSerializer.string())
                .pollTimeout(Duration.ofSeconds(1))
                .objectMapper(new ObjectHashMapper())
                .targetType(Notification.class)
                .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, Notification>> streamMessageListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);
        streamMessageListenerContainer.receiveAutoAck(Consumer.from("cecl-calculator-group", "cecl-calculator"),
                StreamOffset.create(TOPIC, ReadOffset.lastConsumed()), new ConsumeStreamListener(calculatorList));
        return streamMessageListenerContainer;
    }
}
