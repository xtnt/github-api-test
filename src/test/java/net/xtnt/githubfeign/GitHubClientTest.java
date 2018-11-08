package net.xtnt.githubfeign;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nimbusds.jose.JOSEException;

import net.xtnt.githubfeign.GitHubClient.AppAccessToken;
import net.xtnt.githubfeign.GitHubClient.Direction;
import net.xtnt.githubfeign.GitHubClient.Filter;
import net.xtnt.githubfeign.GitHubClient.Issue;
import net.xtnt.githubfeign.GitHubClient.Sort;
import net.xtnt.githubfeign.GitHubClient.State;

public class GitHubClientTest extends AbstractTest {

	@Autowired
	protected GitHubClient gitHubClient;
	
	@Autowired
	protected GitHubAppTokenGenerator gitHubAppTokenGenerator;
	
	@Test
	public void getAllIssues_HasIssues_ReturnsSuccess() throws JOSEException {
		//First get an app accessToken
		//The orgId pretty much must be determined at runtime from either manual entry or discover via the GitHub api
		String orgId = "440300"; //This is extants app install orgId
		AppAccessToken appAccessToken = gitHubAppTokenGenerator.getAppAccessToken(orgId);
		String authorizationHeader = "token " + appAccessToken.getToken();
		List<Issue> issues = gitHubClient.getIssues(authorizationHeader,Filter.all, State.all, null, Sort.created, Direction.desc, null);
		Assert.assertNotNull(issues);
	}
	
}
