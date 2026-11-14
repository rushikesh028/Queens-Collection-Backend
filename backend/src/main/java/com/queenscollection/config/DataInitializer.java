package com.queenscollection.config;

import com.queenscollection.entity.*;
import com.queenscollection.enums.ArticleStatus;
import com.queenscollection.enums.ProductAvailability;
import com.queenscollection.enums.Role;
import com.queenscollection.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;
    private final HeroSectionRepository heroSectionRepository;
    private final InstagramPostRepository instagramPostRepository;
    private final LookbookRepository lookbookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initialization.");
            return;
        }

        log.info("Seeding database with initial data...");

        // Admin user
        User admin = User.builder()
                .fullName("Admin User")
                .email("admin@queenscollection.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();
        userRepository.save(admin);

        // Editor user
        User editor = User.builder()
                .fullName("Editorial Team")
                .email("editor@queenscollection.com")
                .password(passwordEncoder.encode("Editor@123"))
                .role(Role.ROLE_EDITOR)
                .enabled(true)
                .build();
        userRepository.save(editor);

        // Categories matching frontend
        Category fashion = categoryRepository.save(Category.builder()
                .name("Fashion").slug("fashion")
                .description("Mindful fashion and intentional style")
                .imageUrl("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=800&q=80")
                .build());

        Category lifestyle = categoryRepository.save(Category.builder()
                .name("Lifestyle").slug("lifestyle")
                .description("Slow living and daily rituals")
                .imageUrl("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=800&q=80")
                .build());

        Category beauty = categoryRepository.save(Category.builder()
                .name("Beauty").slug("beauty")
                .description("Natural beauty and organic skincare")
                .imageUrl("https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=800&q=80")
                .build());

        Category mindfulness = categoryRepository.save(Category.builder()
                .name("Mindfulness").slug("mindfulness")
                .description("Finding peace in simple moments")
                .imageUrl("https://images.unsplash.com/photo-1502716119720-b23a93e5fe1b?w=800&q=80")
                .build());

        categoryRepository.save(Category.builder()
                .name("Sustainability").slug("sustainability")
                .description("Conscious choices for a better planet")
                .build());

        // Product categories
        Category clothing = categoryRepository.save(Category.builder()
                .name("Clothing").slug("clothing")
                .description("Timeless wardrobe essentials")
                .imageUrl("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=800&q=80")
                .build());

        Category home = categoryRepository.save(Category.builder()
                .name("Home").slug("home")
                .description("Beautiful spaces for mindful living")
                .imageUrl("https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=800&q=80")
                .build());

        Category accessories = categoryRepository.save(Category.builder()
                .name("Accessories").slug("accessories")
                .description("Finishing touches that elevate")
                .imageUrl("https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=800&q=80")
                .build());

        Category beautyProd = categoryRepository.save(Category.builder()
                .name("Beauty Products").slug("beauty-products")
                .description("Clean beauty essentials")
                .imageUrl("https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=800&q=80")
                .build());

        // Featured Articles matching frontend stories
        articleRepository.save(Article.builder()
                .title("The Minimalist Wardrobe")
                .slug("the-minimalist-wardrobe")
                .excerpt("Discovering the art of intentional dressing and capsule collections for the modern woman")
                .content("In a world of endless options, true style is found in intentional choices. A minimalist wardrobe is not about having less for the sake of less—it is about curating pieces that serve you beautifully, season after season.")
                .coverImage("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=1000&q=80")
                .author(editor)
                .category(fashion)
                .status(ArticleStatus.PUBLISHED)
                .featured(true)
                .publishedAt(LocalDateTime.now().minusDays(2))
                .build());

        articleRepository.save(Article.builder()
                .title("Natural Beauty Rituals")
                .slug("natural-beauty-rituals")
                .excerpt("Organic skincare and mindful beauty practices")
                .content("Beauty is not a destination. It is the quiet ritual of caring for yourself—clean ingredients, gentle textures, and the gift of slowing down.")
                .coverImage("https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&q=80")
                .author(editor)
                .category(beauty)
                .status(ArticleStatus.PUBLISHED)
                .featured(true)
                .publishedAt(LocalDateTime.now().minusDays(5))
                .build());

        articleRepository.save(Article.builder()
                .title("Embracing Slow Living")
                .slug("embracing-slow-living")
                .excerpt("Finding peace in daily rituals and moments")
                .content("Slow living is an invitation to return to what matters. Morning light, a warm cup, the feel of linen against skin—these are the moments that compose a beautiful life.")
                .coverImage("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=800&q=80")
                .author(editor)
                .category(mindfulness)
                .status(ArticleStatus.PUBLISHED)
                .featured(true)
                .publishedAt(LocalDateTime.now().minusDays(7))
                .build());

        // Products for shop categories
        productRepository.save(Product.builder()
                .name("Linen Oversized Shirt")
                .slug("linen-oversized-shirt")
                .description("Breathable pure linen shirt in soft ivory. Effortless elegance for everyday.")
                .price(new BigDecimal("128.00"))
                .imageUrl("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=800&q=80")
                .category(clothing)
                .brand("Queen's Collection")
                .availability(ProductAvailability.IN_STOCK)
                .featured(true)
                .build());

        productRepository.save(Product.builder()
                .name("Ceramic Vase Set")
                .slug("ceramic-vase-set")
                .description("Hand-thrown ceramic vases in warm earth tones.")
                .price(new BigDecimal("89.00"))
                .imageUrl("https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=800&q=80")
                .category(home)
                .brand("Queen's Collection")
                .availability(ProductAvailability.IN_STOCK)
                .featured(true)
                .build());

        productRepository.save(Product.builder()
                .name("Woven Leather Tote")
                .slug("woven-leather-tote")
                .description("Timeless tote in natural leather with subtle woven detail.")
                .price(new BigDecimal("245.00"))
                .imageUrl("https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=800&q=80")
                .category(accessories)
                .brand("Queen's Collection")
                .availability(ProductAvailability.IN_STOCK)
                .featured(true)
                .build());

        productRepository.save(Product.builder()
                .name("Botanical Face Oil")
                .slug("botanical-face-oil")
                .description("Nourishing face oil with rosehip and jojoba.")
                .price(new BigDecimal("48.00"))
                .imageUrl("https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=800&q=80")
                .category(beautyProd)
                .brand("Queen's Collection")
                .availability(ProductAvailability.IN_STOCK)
                .featured(true)
                .build());

        // Hero Section matching frontend
        heroSectionRepository.save(HeroSection.builder()
                .label("EST. 2024")
                .title("Natural Elegance")
                .subtitle("Celebrating the beauty of slow living, mindful fashion, and timeless style")
                .backgroundImage("https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=1600&q=80")
                .primaryButtonText("Explore Stories")
                .primaryButtonLink("#blog")
                .secondaryButtonText("Shop Collection")
                .secondaryButtonLink("#shop")
                .active(true)
                .build());

        // Instagram posts
        List<String> instagramImages = List.of(
                "https://images.unsplash.com/photo-1558769132-cb1aea3c892d?w=600&q=80",
                "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=600&q=80",
                "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=600&q=80",
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=600&q=80",
                "https://images.unsplash.com/photo-1479936343636-73cdc5aae0c3?w=600&q=80",
                "https://images.unsplash.com/photo-1502716119720-b23a93e5fe1b?w=600&q=80"
        );
        for (int i = 0; i < instagramImages.size(); i++) {
            instagramPostRepository.save(InstagramPost.builder()
                    .imageUrl(instagramImages.get(i))
                    .postUrl("https://instagram.com")
                    .caption("Queen's Collection moment")
                    .displayOrder(i)
                    .active(true)
                    .build());
        }

        // Lookbook
        Lookbook lookbook = Lookbook.builder()
                .title("Effortless Elegance")
                .description("Spring Collection 2024")
                .collectionName("SPRING COLLECTION 2024")
                .coverImage("https://images.unsplash.com/photo-1529139574466-a303027c1d8b?w=1600&q=80")
                .active(true)
                .displayOrder(0)
                .build();
        lookbookRepository.save(lookbook);

        log.info("Database seeding completed successfully.");
        log.info("Admin credentials: admin@queenscollection.com / Admin@123");
        log.info("Editor credentials: editor@queenscollection.com / Editor@123");
    }
}
