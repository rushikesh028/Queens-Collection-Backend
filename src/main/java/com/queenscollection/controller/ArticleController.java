package com.queenscollection.controller;

import com.queenscollection.dto.request.ArticleRequest;
import com.queenscollection.dto.response.ArticleResponse;
import com.queenscollection.dto.response.PageResponse;
import com.queenscollection.enums.ArticleStatus;
import com.queenscollection.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Editorial content management")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Get published articles (paginated)")
    public ResponseEntity<PageResponse<ArticleResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(articleService.getPublished(page, size, sortBy, direction));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured articles")
    public ResponseEntity<PageResponse<ArticleResponse>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(articleService.getFeatured(page, size));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest 5 published articles")
    public ResponseEntity<List<ArticleResponse>> getLatest() {
        return ResponseEntity.ok(articleService.getLatest());
    }

    @GetMapping("/search")
    @Operation(summary = "Search articles by keyword")
    public ResponseEntity<PageResponse<ArticleResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.search(q, page, size));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get articles by category")
    public ResponseEntity<PageResponse<ArticleResponse>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getByCategory(categoryId, page, size));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get article by slug")
    public ResponseEntity<ArticleResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getBySlug(slug));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new article")
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody ArticleRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.create(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update an article")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(articleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete an article")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all articles for admin (including drafts)")
    public ResponseEntity<PageResponse<ArticleResponse>> getAllForAdmin(
            @RequestParam(required = false) ArticleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(articleService.getAllForAdmin(status, page, size));
    }
}
