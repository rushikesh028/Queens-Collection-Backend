package com.queenscollection.repository;

import com.queenscollection.entity.FavoriteArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticle, Long> {

    List<FavoriteArticle> findByUserId(Long userId);

    Optional<FavoriteArticle> findByUserIdAndArticleId(Long userId, Long articleId);

    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    void deleteByUserIdAndArticleId(Long userId, Long articleId);
}
