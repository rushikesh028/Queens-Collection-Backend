package com.queenscollection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HeroSectionRequest {

    private String label;

    @NotBlank
    @Size(max = 150)
    private String title;

    private String subtitle;

    private String backgroundImage;

    private String primaryButtonText;

    private String primaryButtonLink;

    private String secondaryButtonText;

    private String secondaryButtonLink;

    private Boolean active;
}
