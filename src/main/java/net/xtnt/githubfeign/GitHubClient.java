package net.xtnt.githubfeign;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
	
	@GetMapping(path="/repos/{org_name}/{repo_name}/issues", produces="application/vnd.github.machine-man-preview+json")
	public List<Issue> getIssuesByRepo(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("org_name") String orgName, @PathVariable("repo_name") String repoName);

	@GetMapping(path="/app/installations", produces="application/vnd.github.machine-man-preview+json")
	public List<Installation> getInstallations(@RequestHeader("Authorization") String authorizationHeader);

	@PostMapping(path="/repos/{org_name}/{repo_name}/issues")
	public Issue createIssue(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("org_name") String orgName, @PathVariable("repo_name") String repoName, Issue issue);
	
	@PatchMapping(path="/repos/{org_name}/{repo_name}/issues/{issue_number}")
	public Issue editIssue(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("org_name") String orgName, @PathVariable("repo_name") String repoName, @PathVariable("issue_number") String issueNumber,Issue issue);

	
	
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
		@JsonProperty("html_url")
		private String htmlUrl;
		private String body;
		private String title;
		private List<String> labels;
		private String state;
		@JsonProperty("created_at")
		private Date createdAt;
		private String number;
		private String url;
		
		private GithubUser assignee;
		private GithubUser user;
		

	
	
	}
	
	@Getter
	@Setter
	public static class GithubUser {

		private String id;
		private String login;
		
		
	}

	@Getter
	@Setter
	public static class Installation {
		@JsonProperty("app_id")
		private long appId;
		private long id;
		@JsonProperty("html_url")
		private String htmlUrl;
		// etc
	}

	
	
	@Getter
	@Setter
	public static class Repository {
		private String id;
		@JsonProperty("node_id")
		private String nodeId;
		private String fullName;
		private String htmlUrl;
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
