package com.app.expd.Controller;

import com.app.expd.dto.ResetPasswordRequest;
import com.app.expd.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/changePassowrd")
@AllArgsConstructor
public class PasswordController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        this.authService.changePassword(resetPasswordRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
