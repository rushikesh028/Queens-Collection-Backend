package com.queenscollection.service;

import com.queenscollection.dto.request.LookbookRequest;
import com.queenscollection.dto.response.LookbookResponse;
import com.queenscollection.entity.Lookbook;
import com.queenscollection.entity.LookbookImage;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.LookbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LookbookService {

    private final LookbookRepository repository;

    @Cacheable("lookbooks")
    @Transactional(readOnly = true)
    public List<LookbookResponse> getActive() {
        return repository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LookbookResponse getById(Long id) {
        Lookbook lookbook = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lookbook", "id", id));
        return toResponse(lookbook);
    }

    @Transactional
    @CacheEvict(value = "lookbooks", allEntries = true)
    public LookbookResponse create(LookbookRequest request) {
        Lookbook lookbook = Lookbook.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .collectionName(request.getCollectionName())
                .coverImage(request.getCoverImage())
                .active(request.getActive() != null ? request.getActive() : true)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        if (request.getImages() != null) {
            List<LookbookImage> images = request.getImages().stream()
                    .map(img -> LookbookImage.builder()
                            .lookbook(lookbook)
                            .imageUrl(img.getImageUrl())
                            .caption(img.getCaption())
                            .displayOrder(img.getDisplayOrder() != null ? img.getDisplayOrder() : 0)
                            .build())
                    .collect(Collectors.toList());
            lookbook.setImages(images);
        }

        return toResponse(repository.save(lookbook));
    }

    @Transactional
    @CacheEvict(value = "lookbooks", allEntries = true)
    public LookbookResponse update(Long id, LookbookRequest request) {
        Lookbook lookbook = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lookbook", "id", id));

        if (request.getTitle() != null) lookbook.setTitle(request.getTitle());
        if (request.getDescription() != null) lookbook.setDescription(request.getDescription());
        if (request.getCollectionName() != null) lookbook.setCollectionName(request.getCollectionName());
        if (request.getCoverImage() != null) lookbook.setCoverImage(request.getCoverImage());
        if (request.getActive() != null) lookbook.setActive(request.getActive());
        if (request.getDisplayOrder() != null) lookbook.setDisplayOrder(request.getDisplayOrder());

        return toResponse(repository.save(lookbook));
    }

    @Transactional
    @CacheEvict(value = "lookbooks", allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Lookbook", "id", id);
        }
        repository.deleteById(id);
    }

    private LookbookResponse toResponse(Lookbook lookbook) {
        return LookbookResponse.builder()
                .id(lookbook.getId())
                .title(lookbook.getTitle())
                .description(lookbook.getDescription())
                .collectionName(lookbook.getCollectionName())
                .coverImage(lookbook.getCoverImage())
                .active(lookbook.getActive())
                .displayOrder(lookbook.getDisplayOrder())
                .createdAt(lookbook.getCreatedAt())
                .images(lookbook.getImages().stream()
                        .map(img -> LookbookResponse.LookbookImageResponse.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .caption(img.getCaption())
                                .displayOrder(img.getDisplayOrder())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
