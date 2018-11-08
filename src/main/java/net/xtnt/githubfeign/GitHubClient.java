package net.xtnt.githubfeign;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@FeignClient(name = "github-service", configuration = GitHubAppFeignConfiguration.class)
public interface GitHubClient {

	@GetMapping(path="/issues", produces="application/vnd.github.machine-man-preview+json")
	public List<Issue> getIssues(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("filter") Filter filter, @RequestParam("state") State state,
			@RequestParam("labels") String csvLabels, @RequestParam("sort") Sort sort,
			@RequestParam("direction") Direction direction, @RequestParam("since") String iso8601String);

	@PostMapping(path="/app/installations/{app_id}/access_tokens", produces="application/vnd.github.machine-man-preview+json") 
	public AppAccessToken getAppAccessToken(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("app_id") String appId);
	
	@Getter
	@Setter
	public static class AppAccessToken {
		private String token;
		@JsonProperty("expires_at")
		private Date expiresAt;
	}
		
	@Getter
	@Setter
	public static class Issue {
		private String id;
		@JsonProperty("node_id")
		private String nodeId;
		private String url;
		// etc
	}

	public static enum Filter {
		assigned, created, mentioned, subscribed, all
	}

	public static enum State {
		open, closed, all
	}

	public static enum Sort {
		created, updated, comments
	}

	public static enum Direction {
		asc, desc
	}

}
