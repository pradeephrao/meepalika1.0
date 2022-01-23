package com.meepalika;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.meepalika.threads.config.ThreadTaskDecorator;

@Configuration
@EnableAsync
public class ThreadConfigurer extends AsyncConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(ThreadConfigurer.class);

	@Override
	public Executor getAsyncExecutor() {
		logger.info("initalizing getAsyncExecutor()");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setTaskDecorator(new ThreadTaskDecorator());
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("send-mailer");
		executor.initialize();
		return executor;
	}
}
