package com.example.adaca.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.*;

class AsyncConfigTest {

    @Test
    void taskExecutor_configuredCorrectly() {
        AsyncConfig config = new AsyncConfig();
        TaskExecutor executor = config.taskExecutor();

        assertInstanceOf(ThreadPoolTaskExecutor.class, executor);
        ThreadPoolTaskExecutor threadPool = (ThreadPoolTaskExecutor) executor;
        assertEquals(5, threadPool.getCorePoolSize());
        assertEquals(10, threadPool.getMaxPoolSize());
        assertEquals("EmailSender-", threadPool.getThreadNamePrefix());
    }
}
