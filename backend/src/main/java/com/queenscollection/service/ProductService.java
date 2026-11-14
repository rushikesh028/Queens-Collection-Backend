package com.queenscollection.service;

import com.queenscollection.dto.request.ProductRequest;
import com.queenscollection.dto.response.CategoryResponse;
import com.queenscollection.dto.response.PageResponse;
import com.queenscollection.dto.response.ProductResponse;
import com.queenscollection.entity.Category;
import com.queenscollection.entity.Product;
import com.queenscollection.enums.ProductAvailability;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.CategoryRepository;
import com.queenscollection.repository.ProductRepository;
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
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse create(ProductRequest request) {
        String slug = generateSlug(request.getName());
        if (productRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(category)
                .brand(request.getBrand())
                .availability(request.getAvailability() != null ? request.getAvailability() : ProductAvailability.IN_STOCK)
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .build();

        return toResponse(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (request.getName() != null) {
            product.setName(request.getName());
            product.setSlug(generateSlug(request.getName()));
        }
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getBrand() != null) product.setBrand(request.getBrand());
        if (request.getAvailability() != null) product.setAvailability(request.getAvailability());
        if (request.getFeatured() != null) product.setFeatured(request.getFeatured());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id)));
    }

    @Transactional(readOnly = true)
    public ProductResponse getBySlug(String slug) {
        return toResponse(productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug)));
    }

    @Cacheable("products")
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return PageResponse.from(productRepository.findAll(pageable).map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return PageResponse.from(productRepository.findByCategoryId(categoryId, pageable).map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getFeatured(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.from(productRepository.findByFeaturedTrue(pageable).map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.from(productRepository.searchByKeyword(keyword, pageable).map(this::toResponse));
    }

    private String generateSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory() != null ? CategoryResponse.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .slug(product.getCategory().getSlug())
                        .build() : null)
                .brand(product.getBrand())
                .availability(product.getAvailability())
                .featured(product.getFeatured())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
