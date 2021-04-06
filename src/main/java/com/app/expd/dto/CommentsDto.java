package com.app.expd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDto {

    private Long id;
    @NotNull
    private Long postId;
    @NotBlank
    private String text;
    private String username;
    private Instant createdDate;
    private String duration;
    private Boolean canDeleteComment;
}
