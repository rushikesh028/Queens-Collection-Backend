package com.queenscollection.controller;

import com.queenscollection.dto.response.MessageResponse;
import com.queenscollection.enums.Role;
import com.queenscollection.repository.ArticleRepository;
import com.queenscollection.repository.NewsletterSubscriberRepository;
import com.queenscollection.repository.ProductRepository;
import com.queenscollection.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin", description = "Admin dashboard statistics")
public class AdminController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;
    private final NewsletterSubscriberRepository newsletterRepository;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("adminCount", userRepository.countByRole(Role.ROLE_ADMIN));
        stats.put("editorCount", userRepository.countByRole(Role.ROLE_EDITOR));
        stats.put("userCount", userRepository.countByRole(Role.ROLE_USER));
        stats.put("totalArticles", articleRepository.count());
        stats.put("publishedArticles", articleRepository.countByStatus(com.queenscollection.enums.ArticleStatus.PUBLISHED));
        stats.put("totalProducts", productRepository.count());
        stats.put("activeSubscribers", newsletterRepository.countByActiveTrue());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    @Operation(summary = "Admin health check")
    public ResponseEntity<MessageResponse> health() {
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Admin API is healthy")
                .success(true)
                .build());
    }
}
