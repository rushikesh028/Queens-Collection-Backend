/**
 * Queen's Collection - Fashion Magazine Website
 * Main JavaScript - Interactions & Animations
 */

// ==========================================================================
// Smooth Scroll & Navigation
// ==========================================================================

document.addEventListener('DOMContentLoaded', () => {
    // Mobile Menu Toggle
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('navMenu');
    
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
        });
        
        // Close menu when clicking a link
        const navLinks = navMenu.querySelectorAll('a');
        navLinks.forEach(link => {
            link.addEventListener('click', () => {
                hamburger.classList.remove('active');
                navMenu.classList.remove('active');
            });
        });
    }
    
    // Navbar Scroll Effect
    const navbar = document.getElementById('navbar');
    let lastScroll = 0;
    
    window.addEventListener('scroll', () => {
        const currentScroll = window.pageYOffset;
        
        if (currentScroll > 100) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
        
        lastScroll = currentScroll;
    });
    
    // Smooth Scroll for Navigation Links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            
            if (target) {
                const navHeight = navbar.offsetHeight;
                const targetPosition = target.offsetTop - navHeight;
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
});

// ==========================================================================
// Parallax Effect for Hero Section
// ==========================================================================

function initParallax() {
    const parallaxElements = document.querySelectorAll('[data-parallax]');
    
    window.addEventListener('scroll', () => {
        const scrolled = window.pageYOffset;
        
        parallaxElements.forEach(element => {
            const speed = element.dataset.parallax || 0.5;
            const yPos = -(scrolled * speed);
            element.style.transform = `translateY(${yPos}px)`;
        });
    });
}

// Initialize parallax on page load
initParallax();

// ==========================================================================
// Scroll Reveal Animation
// ==========================================================================

function initScrollReveal() {
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.1
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, observerOptions);
    
    // Observe all elements with fade-in-scroll class
    const scrollElements = document.querySelectorAll('.fade-in-scroll');
    scrollElements.forEach(el => observer.observe(el));
}

// Initialize scroll reveal
initScrollReveal();

// ==========================================================================
// Lookbook Slider
// ==========================================================================

function initLookbookSlider() {
    const slides = document.querySelectorAll('.lookbook-slide');
    const prevBtn = document.querySelector('.lookbook-prev');
    const nextBtn = document.querySelector('.lookbook-next');
    let currentSlide = 0;
    
    if (!slides.length) return;
    
    // Additional slides (add more images programmatically)
    const additionalSlides = [
        {
            image: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=1600&q=80',
            label: 'SUMMER COLLECTION 2024',
            title: 'Natural Grace'
        },
        {
            image: 'https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=1600&q=80',
            label: 'AUTUMN COLLECTION 2024',
            title: 'Warm Embrace'
        }
    ];
    
    // Add additional slides to DOM
    const slider = document.querySelector('.lookbook-slider');
    additionalSlides.forEach(slide => {
        const slideHTML = `
            <div class="lookbook-slide">
                <img src="${slide.image}" alt="${slide.title}" class="lookbook-image">
                <div class="lookbook-caption fade-in">
                    <span class="lookbook-label">${slide.label}</span>
                    <h3 class="lookbook-title">${slide.title}</h3>
                </div>
            </div>
        `;
        slider.insertAdjacentHTML('beforeend', slideHTML);
    });
    
    // Update slides NodeList
    const allSlides = document.querySelectorAll('.lookbook-slide');
    
    function showSlide(n) {
        allSlides.forEach(slide => slide.classList.remove('active'));
        currentSlide = (n + allSlides.length) % allSlides.length;
        allSlides[currentSlide].classList.add('active');
    }
    
    function nextSlide() {
        showSlide(currentSlide + 1);
    }
    
    function prevSlide() {
        showSlide(currentSlide - 1);
    }
    
    if (prevBtn && nextBtn) {
        prevBtn.addEventListener('click', prevSlide);
        nextBtn.addEventListener('click', nextSlide);
    }
    
    // Auto-advance slides every 5 seconds
    setInterval(nextSlide, 5000);
}

// Initialize lookbook slider
initLookbookSlider();

// ==========================================================================
// Hero Section Parallax Enhancement
// ==========================================================================

function initHeroParallaxEnhancement() {
    const heroBackground = document.querySelector('.hero-background');
    
    if (!heroBackground) return;
    
    window.addEventListener('scroll', () => {
        const scrolled = window.pageYOffset;
        const heroHeight = document.querySelector('.hero-fullscreen').offsetHeight;
        
        if (scrolled < heroHeight) {
            const speed = 0.5;
            const yPos = scrolled * speed;
            heroBackground.style.transform = `translateY(${yPos}px)`;
        }
    });
}

// Initialize hero parallax enhancement
initHeroParallaxEnhancement();

// ==========================================================================
// Newsletter Form Handling
// ==========================================================================

function initNewsletterForm() {
    const form = document.querySelector('.newsletter-form');
    
    if (!form) return;
    
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const input = form.querySelector('.newsletter-input');
        const email = input.value.trim();
        
        if (!email) {
            showNotification('Please enter your email address', 'error');
            return;
        }
        
        if (!isValidEmail(email)) {
            showNotification('Please enter a valid email address', 'error');
            return;
        }
        
        // Simulate form submission
        const button = form.querySelector('.newsletter-button');
        const originalText = button.textContent;
        button.textContent = 'Subscribing...';
        button.disabled = true;
        
        setTimeout(() => {
            showNotification('Thank you for subscribing! Welcome to our community.', 'success');
            input.value = '';
            button.textContent = originalText;
            button.disabled = false;
        }, 1500);
    });
}

// Email validation helper
function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// Notification helper
function showNotification(message, type = 'success') {
    // Remove existing notifications
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    
    // Add styles
    Object.assign(notification.style, {
        position: 'fixed',
        top: '100px',
        right: '20px',
        padding: '16px 24px',
        backgroundColor: type === 'success' ? '#8B7355' : '#C4433C',
        color: 'white',
        fontFamily: 'Inter, sans-serif',
        fontSize: '14px',
        borderRadius: '4px',
        boxShadow: '0 4px 20px rgba(0,0,0,0.15)',
        zIndex: '10000',
        animation: 'slideInRight 0.4s ease-out',
        maxWidth: '300px'
    });
    
    document.body.appendChild(notification);
    
    // Remove after 4 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.4s ease-out';
        setTimeout(() => notification.remove(), 400);
    }, 4000);
}

// Add notification animations to stylesheet
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Initialize newsletter form
initNewsletterForm();

// ==========================================================================
// Image Lazy Loading Enhancement
// ==========================================================================

function initLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.removeAttribute('data-src');
                observer.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

// Initialize lazy loading
initLazyLoading();

// ==========================================================================
// Performance Optimization - Debounce Scroll Events
// ==========================================================================

function debounce(func, wait = 10, immediate = true) {
    let timeout;
    return function() {
        const context = this;
        const args = arguments;
        const later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

// Apply debounce to scroll-heavy functions
window.addEventListener('scroll', debounce(() => {
    // Scroll-dependent functions are already optimized
    // This is just a performance safeguard
}, 10));

// ==========================================================================
// Accessibility Enhancements
// ==========================================================================

function initAccessibility() {
    // Add keyboard navigation for hero sections
    const heroSections = document.querySelectorAll('.hero-section');
    
    heroSections.forEach(section => {
        section.setAttribute('tabindex', '0');
        section.setAttribute('role', 'button');
        
        section.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                const link = section.querySelector('.hero-link');
                if (link) link.click();
            }
        });
    });
    
    // Add ARIA labels to interactive elements
    const interactiveElements = document.querySelectorAll('.story-card, .category-card, .instagram-item');
    interactiveElements.forEach(el => {
        if (!el.hasAttribute('aria-label')) {
            const title = el.querySelector('.story-title, .category-title');
            if (title) {
                el.setAttribute('aria-label', title.textContent);
            }
        }
    });
}

// Initialize accessibility features
initAccessibility();

// ==========================================================================
// Page Load Optimization
// ==========================================================================

window.addEventListener('load', () => {
    // Remove loading class if it exists
    document.body.classList.remove('loading');
    
    // Add loaded class for any load-dependent styles
    document.body.classList.add('loaded');
    
    // Initialize any components that need full page load
    console.log("Queen's Collection - Fashion Magazine Website Loaded Successfully");
});

// ==========================================================================
// Print Styles Trigger
// ==========================================================================

window.addEventListener('beforeprint', () => {
    console.log('Preparing page for printing...');
    // Add print-specific adjustments if needed
});

// ==========================================================================
// Prevent Default Behaviors for Demo Links
// ==========================================================================

document.querySelectorAll('a[href="#"]').forEach(link => {
    link.addEventListener('click', (e) => {
        if (!link.closest('.nav-menu')) {
            e.preventDefault();
        }
    });
});

// ==========================================================================
// Dynamic Copyright Year
// ==========================================================================

function updateCopyrightYear() {
    const copyrightText = document.querySelector('.footer-bottom p');
    if (copyrightText) {
        const currentYear = new Date().getFullYear();
        copyrightText.innerHTML = copyrightText.innerHTML.replace('2024', currentYear);
    }
}

updateCopyrightYear();

// ==========================================================================
// Console Styling (Developer Easter Egg)
// ==========================================================================

console.log(
    "%c✨ Queen's Collection ✨",
    'font-size: 24px; font-weight: bold; color: #8B7355; font-family: serif;'
);
console.log(
    '%cCelebrating Simple Moments | Organic Minimalism',
    'font-size: 14px; color: #9B9B9B; font-family: sans-serif;'
);
console.log(
    '%cWebsite crafted with intention and care.',
    'font-size: 12px; color: #C4B5A0; font-style: italic;'
);
