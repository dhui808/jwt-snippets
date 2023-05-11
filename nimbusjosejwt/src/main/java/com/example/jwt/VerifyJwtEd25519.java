package com.example.jwt;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class VerifyJwtEd25519 implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		// Generate a key pair with Ed25519 curve
		OctetKeyPair jwk = new OctetKeyPairGenerator(Curve.Ed25519)
		    .keyID("123")
		    .generate();
		OctetKeyPair publicJWK = jwk.toPublicJWK();

		// Create the EdDSA signer
		JWSSigner signer = new Ed25519Signer(jwk);

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			.subject("jose")
			.issueTime(new Date(123456L))
			.issuer("https://www.example.com")
			.claim("scope", "openid")
			.build();

		SignedJWT signedJWT = new SignedJWT(
			    new JWSHeader.Builder(JWSAlgorithm.EdDSA).keyID(jwk.getKeyID()).build(),
			    claimsSet);

		System.out.println("signedJWT.getState() = " + signedJWT.getState());
		System.out.println("signedJWT.getHeader() = " + signedJWT.getHeader());
		System.out.println("signedJWT.getJWTClaimsSet().getSubject() = " + signedJWT.getJWTClaimsSet().getSubject());
		System.out.println("signedJWT.getJWTClaimsSet().getIssueTime().getTime() = " + signedJWT.getJWTClaimsSet().getIssueTime().getTime());
		System.out.println("signedJWT.getJWTClaimsSet().getIssuer() = " + signedJWT.getJWTClaimsSet().getIssuer());
		System.out.println("signedJWT.getJWTClaimsSet().getStringClaim(\"scope\") = " + signedJWT.getJWTClaimsSet().getStringClaim("scope"));
		System.out.println("signedJWT.getSignature() = " + signedJWT.getSignature());

		// Compute the Ed25519 signature
		signedJWT.sign(signer);

		System.out.println("signedJWT.getState() = " + signedJWT.getState());

		// Serialize the JWS to compact form
		String serializedJWT = signedJWT.serialize();
		System.out.println("serializedJWT = " + serializedJWT);
		
		// On the consumer side, parse the JWS and verify its EdDSA signature
		SignedJWT signedJWT1 = SignedJWT.parse(serializedJWT);
		String serializedJWT1 = signedJWT1.serialize();
		System.out.println("serializedJWT.equals(serializedJWT1)?" + serializedJWT.equals(serializedJWT1));
		System.out.println("signedJWT1.getParsedString() = " + signedJWT1.getParsedString());

		JWSVerifier verifier = new Ed25519Verifier(publicJWK);
		System.out.println("signedJWT.verify(verifier) = " + signedJWT.verify(verifier));
	}

}
