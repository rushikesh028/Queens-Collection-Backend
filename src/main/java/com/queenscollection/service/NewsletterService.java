package com.queenscollection.service;

import com.queenscollection.dto.request.NewsletterSubscribeRequest;
import com.queenscollection.dto.response.MessageResponse;
import com.queenscollection.entity.NewsletterSubscriber;
import com.queenscollection.exception.BadRequestException;
import com.queenscollection.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    private final NewsletterSubscriberRepository repository;

    @Transactional
    public MessageResponse subscribe(NewsletterSubscribeRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (repository.existsByEmail(email)) {
            NewsletterSubscriber existing = repository.findByEmail(email).orElseThrow();
            if (existing.getActive()) {
                throw new BadRequestException("This email is already subscribed");
            }
            existing.setActive(true);
            repository.save(existing);
            return MessageResponse.builder()
                    .message("Welcome back! Your subscription has been reactivated.")
                    .success(true)
                    .build();
        }

        NewsletterSubscriber subscriber = NewsletterSubscriber.builder()
                .email(email)
                .active(true)
                .build();
        repository.save(subscriber);

        return MessageResponse.builder()
                .message("Successfully subscribed to our newsletter")
                .success(true)
                .build();
    }

    @Transactional(readOnly = true)
    public List<NewsletterSubscriber> getAllSubscribers() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public long getActiveCount() {
        return repository.countByActiveTrue();
    }
}
