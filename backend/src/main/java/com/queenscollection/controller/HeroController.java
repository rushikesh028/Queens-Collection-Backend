package com.queenscollection.controller;

import com.queenscollection.dto.request.HeroSectionRequest;
import com.queenscollection.dto.response.HeroSectionResponse;
import com.queenscollection.service.HeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hero")
@RequiredArgsConstructor
@Tag(name = "Hero Section", description = "Homepage hero content management")
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    @Operation(summary = "Get active hero section")
    public ResponseEntity<HeroSectionResponse> getActive() {
        return ResponseEntity.ok(heroService.getActive());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create or replace hero section")
    public ResponseEntity<HeroSectionResponse> createOrUpdate(@Valid @RequestBody HeroSectionRequest request) {
        return ResponseEntity.ok(heroService.createOrUpdate(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update hero section")
    public ResponseEntity<HeroSectionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody HeroSectionRequest request) {
        return ResponseEntity.ok(heroService.update(id, request));
    }
}
