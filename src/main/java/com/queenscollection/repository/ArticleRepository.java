package com.queenscollection.repository;

import com.queenscollection.entity.Article;
import com.queenscollection.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    Page<Article> findByStatusAndFeaturedTrue(ArticleStatus status, Pageable pageable);

    Page<Article> findByStatusAndCategoryId(ArticleStatus status, Long categoryId, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.status = :status AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Article> searchByKeyword(@Param("status") ArticleStatus status,
                                  @Param("keyword") String keyword,
                                  Pageable pageable);

    List<Article> findTop5ByStatusOrderByPublishedAtDesc(ArticleStatus status);

    long countByStatus(ArticleStatus status);
}
