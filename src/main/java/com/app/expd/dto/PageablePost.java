package com.app.expd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageablePost {

    private int totalElements;
    private int totalPages;
    private List<PostResposne> postResposnes;
}
