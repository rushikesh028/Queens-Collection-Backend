package com.queenscollection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookbookResponse {

    private Long id;
    private String title;
    private String description;
    private String collectionName;
    private String coverImage;
    private Boolean active;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private List<LookbookImageResponse> images;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LookbookImageResponse {
        private Long id;
        private String imageUrl;
        private String caption;
        private Integer displayOrder;
    }
}
