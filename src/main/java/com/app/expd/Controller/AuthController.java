package com.app.expd.Controller;

import com.app.expd.dto.*;
import com.app.expd.service.AuthService;
import com.app.expd.service.RefreshTokenServie;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenServie refreshTokenServie;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token){
        authService.verifyToken(token);
        return new ResponseEntity<>("Token verification is Successful",
                HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){

       return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refeshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        return authService.refreshToken(refreshTokenRequest);

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        refreshTokenServie.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return  ResponseEntity.status(HttpStatus.OK)
                .body("Refresh Token Deleted Successfully");
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestParam String username) {
        this.authService.resetUserPassoword(username);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
