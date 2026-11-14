package com.queenscollection.service;

import com.queenscollection.dto.request.ArticleRequest;
import com.queenscollection.dto.response.ArticleResponse;
import com.queenscollection.dto.response.CategoryResponse;
import com.queenscollection.dto.response.PageResponse;
import com.queenscollection.dto.response.UserResponse;
import com.queenscollection.entity.Article;
import com.queenscollection.entity.Category;
import com.queenscollection.entity.User;
import com.queenscollection.enums.ArticleStatus;
import com.queenscollection.exception.BadRequestException;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.ArticleRepository;
import com.queenscollection.repository.CategoryRepository;
import com.queenscollection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Transactional
    @CacheEvict(value = {"articles", "featuredArticles"}, allEntries = true)
    public ArticleResponse create(ArticleRequest request, String authorEmail) {
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authorEmail));

        String slug = generateSlug(request.getTitle());
        if (articleRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        ArticleStatus status = request.getStatus() != null ? request.getStatus() : ArticleStatus.DRAFT;

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(slug)
                .excerpt(request.getExcerpt())
                .content(request.getContent())
                .coverImage(request.getCoverImage())
                .author(author)
                .category(category)
                .status(status)
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .publishedAt(status == ArticleStatus.PUBLISHED ? LocalDateTime.now() : null)
                .build();

        article = articleRepository.save(article);
        return toResponse(article);
    }

    @Transactional
    @CacheEvict(value = {"articles", "featuredArticles"}, allEntries = true)
    public ArticleResponse update(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        if (request.getTitle() != null) {
            article.setTitle(request.getTitle());
            String newSlug = generateSlug(request.getTitle());
            if (!newSlug.equals(article.getSlug()) && articleRepository.existsBySlug(newSlug)) {
                newSlug = newSlug + "-" + System.currentTimeMillis();
            }
            article.setSlug(newSlug);
        }
        if (request.getExcerpt() != null) article.setExcerpt(request.getExcerpt());
        if (request.getContent() != null) article.setContent(request.getContent());
        if (request.getCoverImage() != null) article.setCoverImage(request.getCoverImage());
        if (request.getFeatured() != null) article.setFeatured(request.getFeatured());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            article.setCategory(category);
        }

        if (request.getStatus() != null) {
            if (request.getStatus() == ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
                article.setPublishedAt(LocalDateTime.now());
            }
            article.setStatus(request.getStatus());
        }

        article = articleRepository.save(article);
        return toResponse(article);
    }

    @Transactional
    @CacheEvict(value = {"articles", "featuredArticles"}, allEntries = true)
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        articleRepository.deleteById(id);
    }

    @Cacheable(value = "articles", key = "#slug")
    @Transactional(readOnly = true)
    public ArticleResponse getBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "slug", slug));
        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Article", "slug", slug);
        }
        return toResponse(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        return toResponse(article);
    }

    @Cacheable(value = "articles")
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getPublished(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Article> articles = articleRepository.findByStatus(ArticleStatus.PUBLISHED, pageable);
        return PageResponse.from(articles.map(this::toResponse));
    }

    @Cacheable(value = "featuredArticles")
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getFeatured(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<Article> articles = articleRepository.findByStatusAndFeaturedTrue(ArticleStatus.PUBLISHED, pageable);
        return PageResponse.from(articles.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<Article> articles = articleRepository.findByStatusAndCategoryId(ArticleStatus.PUBLISHED, categoryId, pageable);
        return PageResponse.from(articles.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<Article> articles = articleRepository.searchByKeyword(ArticleStatus.PUBLISHED, keyword, pageable);
        return PageResponse.from(articles.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getLatest() {
        return articleRepository.findTop5ByStatusOrderByPublishedAtDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getAllForAdmin(ArticleStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Article> articles = status != null
                ? articleRepository.findByStatus(status, pageable)
                : articleRepository.findAll(pageable);
        return PageResponse.from(articles.map(this::toResponse));
    }

    private String generateSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }

    private ArticleResponse toResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .excerpt(article.getExcerpt())
                .content(article.getContent())
                .coverImage(article.getCoverImage())
                .author(UserResponse.builder()
                        .id(article.getAuthor().getId())
                        .fullName(article.getAuthor().getFullName())
                        .email(article.getAuthor().getEmail())
                        .role(article.getAuthor().getRole())
                        .build())
                .category(article.getCategory() != null ? CategoryResponse.builder()
                        .id(article.getCategory().getId())
                        .name(article.getCategory().getName())
                        .slug(article.getCategory().getSlug())
                        .build() : null)
                .status(article.getStatus())
                .featured(article.getFeatured())
                .publishedAt(article.getPublishedAt())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
