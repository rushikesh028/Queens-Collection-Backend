package com.queenscollection.controller;

import com.queenscollection.dto.response.ArticleResponse;
import com.queenscollection.dto.response.MessageResponse;
import com.queenscollection.dto.response.UserResponse;
import com.queenscollection.entity.User;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.UserRepository;
import com.queenscollection.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile and favorites")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserRepository userRepository;
    private final FavoriteService favoriteService;

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build());
    }

    @GetMapping("/me/favorites")
    @Operation(summary = "Get current user's favorite articles")
    public ResponseEntity<List<ArticleResponse>> getFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getFavorites(authentication.getName()));
    }

    @PostMapping("/me/favorites/{articleId}")
    @Operation(summary = "Add article to favorites")
    public ResponseEntity<MessageResponse> addFavorite(
            @PathVariable Long articleId,
            Authentication authentication) {
        return ResponseEntity.ok(favoriteService.addFavorite(authentication.getName(), articleId));
    }

    @DeleteMapping("/me/favorites/{articleId}")
    @Operation(summary = "Remove article from favorites")
    public ResponseEntity<MessageResponse> removeFavorite(
            @PathVariable Long articleId,
            Authentication authentication) {
        return ResponseEntity.ok(favoriteService.removeFavorite(authentication.getName(), articleId));
    }
}
