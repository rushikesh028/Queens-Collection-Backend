package com.queenscollection.service;

import com.queenscollection.dto.request.HeroSectionRequest;
import com.queenscollection.dto.response.HeroSectionResponse;
import com.queenscollection.entity.HeroSection;
import com.queenscollection.exception.ResourceNotFoundException;
import com.queenscollection.repository.HeroSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroSectionRepository repository;

    @Cacheable("hero")
    @Transactional(readOnly = true)
    public HeroSectionResponse getActive() {
        HeroSection hero = repository.findFirstByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active hero section found"));
        return toResponse(hero);
    }

    @Transactional
    @CacheEvict(value = "hero", allEntries = true)
    public HeroSectionResponse createOrUpdate(HeroSectionRequest request) {
        // Deactivate existing active heroes
        repository.findAll().forEach(h -> {
            h.setActive(false);
            repository.save(h);
        });

        HeroSection hero = HeroSection.builder()
                .label(request.getLabel())
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .backgroundImage(request.getBackgroundImage())
                .primaryButtonText(request.getPrimaryButtonText())
                .primaryButtonLink(request.getPrimaryButtonLink())
                .secondaryButtonText(request.getSecondaryButtonText())
                .secondaryButtonLink(request.getSecondaryButtonLink())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(repository.save(hero));
    }

    @Transactional
    @CacheEvict(value = "hero", allEntries = true)
    public HeroSectionResponse update(Long id, HeroSectionRequest request) {
        HeroSection hero = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HeroSection", "id", id));

        if (request.getLabel() != null) hero.setLabel(request.getLabel());
        if (request.getTitle() != null) hero.setTitle(request.getTitle());
        if (request.getSubtitle() != null) hero.setSubtitle(request.getSubtitle());
        if (request.getBackgroundImage() != null) hero.setBackgroundImage(request.getBackgroundImage());
        if (request.getPrimaryButtonText() != null) hero.setPrimaryButtonText(request.getPrimaryButtonText());
        if (request.getPrimaryButtonLink() != null) hero.setPrimaryButtonLink(request.getPrimaryButtonLink());
        if (request.getSecondaryButtonText() != null) hero.setSecondaryButtonText(request.getSecondaryButtonText());
        if (request.getSecondaryButtonLink() != null) hero.setSecondaryButtonLink(request.getSecondaryButtonLink());
        if (request.getActive() != null) hero.setActive(request.getActive());

        return toResponse(repository.save(hero));
    }

    private HeroSectionResponse toResponse(HeroSection hero) {
        return HeroSectionResponse.builder()
                .id(hero.getId())
                .label(hero.getLabel())
                .title(hero.getTitle())
                .subtitle(hero.getSubtitle())
                .backgroundImage(hero.getBackgroundImage())
                .primaryButtonText(hero.getPrimaryButtonText())
                .primaryButtonLink(hero.getPrimaryButtonLink())
                .secondaryButtonText(hero.getSecondaryButtonText())
                .secondaryButtonLink(hero.getSecondaryButtonLink())
                .active(hero.getActive())
                .build();
    }
}
