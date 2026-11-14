package com.queenscollection.dto.request;

import com.queenscollection.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @Size(max = 500)
    private String excerpt;

    private String content;

    private String coverImage;

    private Long categoryId;

    private ArticleStatus status;

    private Boolean featured;
}
