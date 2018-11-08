package net.xtnt.githubfeign;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.xtnt.githubfeign.GitHubClient.AppAccessToken;

@Component
@ConfigurationProperties(prefix = "application.github.token-generator")
@Slf4j
public class GitHubAppTokenGenerator implements InitializingBean {

	@Autowired
	protected GitHubClient gitHubClient;
	
	private JWSSigner signer;
	private JWSAlgorithm algorithm = JWSAlgorithm.RS256;

	@Setter
	private Integer expireSeconds;
	@Setter
	private String issuer;
	@Setter
	private Resource signingPemFile;

	@Override
	public void afterPropertiesSet() throws Exception {
		String pem = IOUtils.toString(signingPemFile.getInputStream(), Charset.defaultCharset());
		JWK signingJwk = JWK.parseFromPEMEncodedObjects(pem);
		signer = new RSASSASigner((RSAKey) signingJwk);
	}

	/**
	 * Get an github application and organization specific access token.
	 * 
	 * @param organizationId
	 * @return
	 * @throws JOSEException 
	 */
	public AppAccessToken getAppAccessToken(String organizationId) throws JOSEException {
		String accessToken = generateJwtAccessToken();
		String authorizationHeader = "bearer " + accessToken;
		AppAccessToken appAccessToken = gitHubClient.getAppAccessToken(authorizationHeader, organizationId);
		return appAccessToken;
	}

	public String generateJwtAccessToken() throws JOSEException {
		// First generate a JWT that we can use to call github, we may just want to
		// reuse these until expiry later, but
		// signing a new one every time is fine for now
		JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder().jwtID(UUID.randomUUID().toString())
				.issuer(issuer).expirationTime(DateTime.now().plusSeconds(Integer.valueOf(expireSeconds)).toDate())
				.issueTime(new Date())
				;
		JWTClaimsSet claimsSet = claimsSetBuilder.build();
		JWSHeader header = new JWSHeader.Builder(algorithm).type(JOSEObjectType.JWT).build();
		SignedJWT signedJWT = new SignedJWT(header, claimsSet);
		signedJWT.sign(signer);
		String signedJwtString = signedJWT.serialize();
		return signedJwtString;
	}
}
