package com.queenscollection.controller;

import com.queenscollection.dto.request.InstagramPostRequest;
import com.queenscollection.dto.response.InstagramPostResponse;
import com.queenscollection.service.InstagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instagram")
@RequiredArgsConstructor
@Tag(name = "Instagram", description = "Instagram feed management")
public class InstagramController {

    private final InstagramService instagramService;

    @GetMapping
    @Operation(summary = "Get active Instagram posts")
    public ResponseEntity<List<InstagramPostResponse>> getActive() {
        return ResponseEntity.ok(instagramService.getActivePosts());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add Instagram post")
    public ResponseEntity<InstagramPostResponse> create(@Valid @RequestBody InstagramPostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instagramService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update Instagram post")
    public ResponseEntity<InstagramPostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InstagramPostRequest request) {
        return ResponseEntity.ok(instagramService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete Instagram post")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        instagramService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
