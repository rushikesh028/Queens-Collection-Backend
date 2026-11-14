package com.queenscollection.controller;

import com.queenscollection.dto.request.LookbookRequest;
import com.queenscollection.dto.response.LookbookResponse;
import com.queenscollection.service.LookbookService;
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
@RequestMapping("/api/v1/lookbooks")
@RequiredArgsConstructor
@Tag(name = "Lookbooks", description = "Editorial lookbook management")
public class LookbookController {

    private final LookbookService lookbookService;

    @GetMapping
    @Operation(summary = "Get active lookbooks")
    public ResponseEntity<List<LookbookResponse>> getActive() {
        return ResponseEntity.ok(lookbookService.getActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lookbook by ID")
    public ResponseEntity<LookbookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lookbookService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a lookbook")
    public ResponseEntity<LookbookResponse> create(@Valid @RequestBody LookbookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lookbookService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update a lookbook")
    public ResponseEntity<LookbookResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LookbookRequest request) {
        return ResponseEntity.ok(lookbookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a lookbook")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lookbookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
