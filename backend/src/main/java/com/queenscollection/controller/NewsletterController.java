package com.queenscollection.controller;

import com.queenscollection.dto.request.NewsletterSubscribeRequest;
import com.queenscollection.dto.response.MessageResponse;
import com.queenscollection.entity.NewsletterSubscriber;
import com.queenscollection.service.NewsletterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/newsletter")
@RequiredArgsConstructor
@Tag(name = "Newsletter", description = "Newsletter subscription management")
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe to newsletter")
    public ResponseEntity<MessageResponse> subscribe(@Valid @RequestBody NewsletterSubscribeRequest request) {
        return ResponseEntity.ok(newsletterService.subscribe(request));
    }

    @GetMapping("/subscribers")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all subscribers (Admin only)")
    public ResponseEntity<List<NewsletterSubscriber>> getSubscribers() {
        return ResponseEntity.ok(newsletterService.getAllSubscribers());
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get active subscriber count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(newsletterService.getActiveCount());
    }
}
