package net.xtnt.githubfeign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger.Level;
import feign.RequestInterceptor;

@Configuration
public class GitHubAppFeignConfiguration {

	 //@Bean
	 public RequestInterceptor requestTokenBearerInterceptor() {
	   //TODO implement a RequestInterceptor that adds our custom token, probabaly should inject security context into this method.
	   return null; 
	   //something like OAuth2FeignRequestInterceptor
	 }

	 @Bean
	 Level feignLoggerLevel() {
	   return Level.FULL;
	 }
	
}
