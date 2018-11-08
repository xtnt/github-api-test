package net.xtnt.githubfeign;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nimbusds.jose.JOSEException;

import lombok.extern.slf4j.Slf4j;
import net.xtnt.githubfeign.GitHubClient.AppAccessToken;

@Slf4j
public class GitHubAppTokenGeneratorTest extends AbstractTest {

	@Autowired
	protected GitHubAppTokenGenerator gitHubAppTokenGenerator;
	
	@Test
	public void generateAccessToken_validSigningKey_returnsAccessToken() throws JOSEException {
		String jwt = gitHubAppTokenGenerator.generateJwtAccessToken();
	    Assert.assertNotNull(jwt);
		log.info(jwt);
	}
	
	@Test
	public void getAppAccessToken_validSigningKey_returnsAccessToken() throws JOSEException {
		//The orgId pretty much must be determined at runtime from either manual entry or discover via the GitHub api
		String orgId = "440300"; //This is extants app install orgId
		AppAccessToken appAccessToken = gitHubAppTokenGenerator.getAppAccessToken(orgId);
	    Assert.assertNotNull(appAccessToken.getToken());
		log.info(appAccessToken.getToken());
	}
}
