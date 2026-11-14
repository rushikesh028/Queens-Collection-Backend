package com.queenscollection.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hero_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeroSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String label;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 300)
    private String subtitle;

    @Column(length = 500)
    private String backgroundImage;

    @Column(length = 50)
    private String primaryButtonText;

    @Column(length = 200)
    private String primaryButtonLink;

    @Column(length = 50)
    private String secondaryButtonText;

    @Column(length = 200)
    private String secondaryButtonLink;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
