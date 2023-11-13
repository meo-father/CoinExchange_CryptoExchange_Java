package com.bizzan.bitrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;


@EnableRedisHttpSession
public class HttpSessionConfig {

	@Bean
	public HeaderHttpSessionIdResolver httpSessionStrategy() {
		return new HeaderHttpSessionIdResolver("x-auth-token");
	}
}
