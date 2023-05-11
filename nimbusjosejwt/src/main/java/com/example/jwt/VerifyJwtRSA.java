package com.example.jwt;

import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class VerifyJwtRSA implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);

		KeyPair kp = kpg.genKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			.subject("jose")
			.issueTime(new Date(123456L))
			.issuer("https://www.example.com")
			.claim("scope", "openid")
			.build();

		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).
			keyID("1").
			jwkURL(new URI("https://www.example.com/jwks.json")).
			build();

		SignedJWT signedJWT = new SignedJWT(header, claimsSet);

		System.out.println("signedJWT.getState() = " + signedJWT.getState());
		System.out.println("signedJWT.getHeader() = " + signedJWT.getHeader());
		System.out.println("signedJWT.getJWTClaimsSet().getSubject() = " + signedJWT.getJWTClaimsSet().getSubject());
		System.out.println("signedJWT.getJWTClaimsSet().getIssueTime().getTime() = " + signedJWT.getJWTClaimsSet().getIssueTime().getTime());
		System.out.println("signedJWT.getJWTClaimsSet().getIssuer() = " + signedJWT.getJWTClaimsSet().getIssuer());
		System.out.println("signedJWT.getJWTClaimsSet().getStringClaim(\"scope\") = " + signedJWT.getJWTClaimsSet().getStringClaim("scope"));
		System.out.println("signedJWT.getSignature() = " + signedJWT.getSignature());

		Base64URL sigInput = Base64URL.encode(signedJWT.getSigningInput());

		JWSSigner signer = new RSASSASigner(privateKey);

		signedJWT.sign(signer);

		System.out.println("signedJWT.getState() = " + signedJWT.getState());

		String serializedJWT = signedJWT.serialize();

		System.out.println("serializedJWT = " + serializedJWT);
		
		SignedJWT signedJWT1 = SignedJWT.parse(serializedJWT);
		String serializedJWT1 = signedJWT1.serialize();
		System.out.println("serializedJWT.equals(serializedJWT1)?" + serializedJWT.equals(serializedJWT1));
		System.out.println("signedJWT1.getParsedString() = " + signedJWT1.getParsedString());

		System.out.println(sigInput.equals(Base64URL.encode(signedJWT.getSigningInput())));

		JWSVerifier verifier = new RSASSAVerifier(publicKey);
		System.out.println("signedJWT.verify(verifier) = " + signedJWT.verify(verifier));
		
	}

}
