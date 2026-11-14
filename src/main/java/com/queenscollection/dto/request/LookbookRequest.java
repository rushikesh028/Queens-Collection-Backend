package com.queenscollection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class LookbookRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    private String description;

    private String collectionName;

    private String coverImage;

    private Boolean active;

    private Integer displayOrder;

    private List<LookbookImageRequest> images;

    @Data
    public static class LookbookImageRequest {
        private String imageUrl;
        private String caption;
        private Integer displayOrder;
    }
}
