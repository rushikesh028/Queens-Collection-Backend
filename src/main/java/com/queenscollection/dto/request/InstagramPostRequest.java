package com.queenscollection.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstagramPostRequest {

    @NotBlank
    private String imageUrl;

    private String postUrl;

    private String caption;

    private Integer displayOrder;

    private Boolean active;
}
