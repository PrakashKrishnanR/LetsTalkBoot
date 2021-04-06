package com.app.expd.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String oldPassword;
    @NotBlank
    @Size(min = 7, message = "Not a valid password")
    private String newPassword;
}
