package net.xtnt.githubfeign;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;

import net.xtnt.githubfeign.GitHubClient.AppAccessToken;
import net.xtnt.githubfeign.GitHubClient.Direction;
import net.xtnt.githubfeign.GitHubClient.Filter;
import net.xtnt.githubfeign.GitHubClient.Installation;
import net.xtnt.githubfeign.GitHubClient.Issue;
import net.xtnt.githubfeign.GitHubClient.Sort;
import net.xtnt.githubfeign.GitHubClient.State;

public class GitHubClientTest extends AbstractTest {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	protected GitHubClient gitHubClient;
	
	@Autowired
	protected GitHubAppTokenGenerator gitHubAppTokenGenerator;

	@Test
	public void getInstallations() throws JOSEException {
		String authorizationHeader = gitHubAppTokenGenerator.getJwtAuthHeader();
		List<Installation> results= gitHubClient.getInstallations(authorizationHeader);
		Assert.assertNotNull(results);
		Installation first = results.get(0);
		Assert.assertEquals("https://github.com/organizations/xtnt/settings/installations/440300", first.getHtmlUrl());
		Assert.assertEquals(20387, first.getAppId());
		Assert.assertEquals(440300,first.getId());
	}
	
	@Test
	public void getIssesByRepo() throws JOSEException, JsonProcessingException {
		//First get an app accessToken
		//The orgId pretty much must be determined at runtime from either manual entry or discover via the GitHub api
		String orgId = "440300"; //This is extants app install orgId
		AppAccessToken appAccessToken = gitHubAppTokenGenerator.getAppAccessToken(orgId);
		String authorizationHeader = "token " + appAccessToken.getToken();
		
		
		//Get the issues for /xtnt/github-api-test repo 
		List<Issue> results = gitHubClient.getIssuesByRepo(authorizationHeader, "xtnt","github-api-test");
		
		log.info("found " + results.size() + " issues");
		
		Issue firstIssue = results.get(results.size()-1);
		Assert.assertNotNull(results);
		Assert.assertEquals("1", firstIssue.getNumber());
		Assert.assertEquals("barretttucker", firstIssue.getUser().getLogin());
		
		
	}
	
	
	@Test
	public void createAndCloseIssue() throws JOSEException, JsonProcessingException {
		//First get an app accessToken
		//The orgId pretty much must be determined at runtime from either manual entry or discover via the GitHub api
		String orgId = "440300"; //This is extants app install orgId
		AppAccessToken appAccessToken = gitHubAppTokenGenerator.getAppAccessToken(orgId);
		String authorizationHeader = "token " + appAccessToken.getToken();
		
		Issue issue = new Issue();
		issue.setTitle("An issue created by the GitHubApp");
		issue.setBody("Here is the body of the issue!");
		issue.setLabels(new ArrayList<String>());
		Issue createdIssue = gitHubClient.createIssue(authorizationHeader, "xtnt", "github-api-test", issue);
		Assert.assertNotNull(createdIssue);
		Assert.assertEquals(issue.getTitle(), createdIssue.getTitle());
		
		
		createdIssue.setState("closed");
		gitHubClient.editIssue(authorizationHeader, "xtnt", "github-api-test", createdIssue.getNumber(),createdIssue);
		
		
	}
	
	
}
