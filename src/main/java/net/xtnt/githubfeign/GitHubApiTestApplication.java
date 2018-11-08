package net.xtnt.githubfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients()
public class GitHubApiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubApiTestApplication.class, args);
	}
}
