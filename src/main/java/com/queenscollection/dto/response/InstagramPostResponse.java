package com.queenscollection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramPostResponse {

    private Long id;
    private String imageUrl;
    private String postUrl;
    private String caption;
    private Integer displayOrder;
    private Boolean active;
}
