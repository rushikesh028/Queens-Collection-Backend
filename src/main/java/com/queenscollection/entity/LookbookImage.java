package com.queenscollection.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lookbook_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LookbookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookbook_id", nullable = false)
    private Lookbook lookbook;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 300)
    private String caption;

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}
