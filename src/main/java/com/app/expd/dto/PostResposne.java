package com.app.expd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResposne {
    private Long postId;
    private String subtalkname;
    private String postname;
    private String description;
    private String url;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private String username;
    private Boolean upVote;
    private Boolean downVote;
    private Long subtalkId;
    private Boolean createdByUser;
}
