package com.queenscollection.service;

import com.queenscollection.dto.request.InstagramPostRequest;
import com.queenscollection.dto.response.InstagramPostResponse;
import com.queenscollection.entity.InstagramPost;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.InstagramPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramService {

    private final InstagramPostRepository repository;

    @Cacheable("instagram")
    @Transactional(readOnly = true)
    public List<InstagramPostResponse> getActivePosts() {
        return repository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "instagram", allEntries = true)
    public InstagramPostResponse create(InstagramPostRequest request) {
        InstagramPost post = InstagramPost.builder()
                .imageUrl(request.getImageUrl())
                .postUrl(request.getPostUrl())
                .caption(request.getCaption())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return toResponse(repository.save(post));
    }

    @Transactional
    @CacheEvict(value = "instagram", allEntries = true)
    public InstagramPostResponse update(Long id, InstagramPostRequest request) {
        InstagramPost post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstagramPost", "id", id));

        if (request.getImageUrl() != null) post.setImageUrl(request.getImageUrl());
        if (request.getPostUrl() != null) post.setPostUrl(request.getPostUrl());
        if (request.getCaption() != null) post.setCaption(request.getCaption());
        if (request.getDisplayOrder() != null) post.setDisplayOrder(request.getDisplayOrder());
        if (request.getActive() != null) post.setActive(request.getActive());

        return toResponse(repository.save(post));
    }

    @Transactional
    @CacheEvict(value = "instagram", allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("InstagramPost", "id", id);
        }
        repository.deleteById(id);
    }

    private InstagramPostResponse toResponse(InstagramPost post) {
        return InstagramPostResponse.builder()
                .id(post.getId())
                .imageUrl(post.getImageUrl())
                .postUrl(post.getPostUrl())
                .caption(post.getCaption())
                .displayOrder(post.getDisplayOrder())
                .active(post.getActive())
                .build();
    }
}
