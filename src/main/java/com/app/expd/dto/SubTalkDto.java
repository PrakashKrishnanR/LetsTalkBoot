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
public class SubTalkDto {
    @NotBlank
    @Size(max = 255, min = 4, message = "Title should be min-4 and max-255 characters")
    private String name;
    private Long id;
    @NotBlank
    @Size(max = 255, min = 7, message = "Description should be min-7 and max-255 characters")
    private String description;
    private Integer noOfPosts;
    private Boolean deleteEnabled;
}
