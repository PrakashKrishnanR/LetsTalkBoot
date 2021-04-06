package com.app.expd.security;

import com.app.expd.exceptions.LetsTalkException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long tokenExpirationTime;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceasStream = getClass().getResourceAsStream("/prakashkrishnan.jks");
            keyStore.load(resourceasStream, "prakash".toCharArray());

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername().toUpperCase())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivatKey())
                .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationTime)))
                .compact();

    }

    public String generateTokenWithUsername(String userName){

        return Jwts.builder().
                setSubject(userName.toUpperCase())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivatKey())
                .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationTime)))
                .compact();

    }

    public PrivateKey getPrivatKey() {
        try{
            return (PrivateKey) keyStore.getKey("alias_name", "prakash".toCharArray());
        }
        catch (KeyStoreException  | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new LetsTalkException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateJWTtoken(String jwt){
        Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
           return keyStore.getCertificate("alias_name").getPublicKey();
        } catch (KeyStoreException e) {
            throw new LetsTalkException("Error occurred during retrival of public key");
        }
    }

    public String getUserNameFromToken(String jwt){
        Claims claims = Jwts.parserBuilder().setSigningKey(getPublickey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }

    public Long getTokenExpirationTime() {
        return tokenExpirationTime;
    }
}


