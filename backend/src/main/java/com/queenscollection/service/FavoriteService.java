package com.queenscollection.service;

import com.queenscollection.dto.response.ArticleResponse;
import com.queenscollection.dto.response.MessageResponse;
import com.queenscollection.entity.Article;
import com.queenscollection.entity.FavoriteArticle;
import com.queenscollection.entity.User;
import com.queenscollection.enums.ArticleStatus;
import com.queenscollection.exception.BadRequestException;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.ArticleRepository;
import com.queenscollection.repository.FavoriteArticleRepository;
import com.queenscollection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteArticleRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Transactional
    public MessageResponse addFavorite(String userEmail, Long articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));

        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new BadRequestException("Only published articles can be favorited");
        }

        if (favoriteRepository.existsByUserIdAndArticleId(user.getId(), articleId)) {
            throw new BadRequestException("Article is already in favorites");
        }

        FavoriteArticle favorite = FavoriteArticle.builder()
                .user(user)
                .article(article)
                .build();
        favoriteRepository.save(favorite);

        return MessageResponse.builder()
                .message("Article added to favorites")
                .success(true)
                .build();
    }

    @Transactional
    public MessageResponse removeFavorite(String userEmail, Long articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        if (!favoriteRepository.existsByUserIdAndArticleId(user.getId(), articleId)) {
            throw new ResourceNotFoundException("Favorite not found");
        }

        favoriteRepository.deleteByUserIdAndArticleId(user.getId(), articleId);
        return MessageResponse.builder()
                .message("Article removed from favorites")
                .success(true)

                .build();
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        return favoriteRepository.findByUserId(user.getId()).stream()
                .map(f -> articleService.getById(f.getArticle().getId()))
                .collect(Collectors.toList());
    }
}
