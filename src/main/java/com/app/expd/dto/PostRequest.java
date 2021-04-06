package com.app.expd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    private Long postId;
    @NotBlank
    @Size(min = 4, max = 255, message = "Invalid Subject name")
    private String subtalkname;
    @NotBlank
    @Size(min = 4, max = 255, message = "Invalid Postname. Number of characters should be min-4 and max-255")
    private String postname;
    @NotBlank
    private String description;
    private String url;

}
