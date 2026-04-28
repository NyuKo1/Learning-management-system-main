// Navbar scroll effect
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 20);
});

// Burger menu
const burger = document.getElementById('burger');
burger.addEventListener('click', () => {
  const links = document.querySelector('.nav-links');
  const actions = document.querySelector('.nav-actions');
  links?.classList.toggle('mobile-open');
  actions?.classList.toggle('mobile-open');
});

// Dark mode
const darkToggle = document.getElementById('darkToggle');
const applyDark = (dark) => {
  document.body.classList.toggle('dark', dark);
  darkToggle.textContent = dark ? '☀️' : '🌙';
};

const savedDark = localStorage.getItem('sec-dark') === 'true';
applyDark(savedDark);

darkToggle.addEventListener('click', () => {
  const isDark = document.body.classList.toggle('dark');
  localStorage.setItem('sec-dark', isDark);
  darkToggle.textContent = isDark ? '☀️' : '🌙';
});

// Contact form
function handleSubmit(e) {
  e.preventDefault();
  const btn = e.target.querySelector('button[type="submit"]');
  btn.textContent = 'Отправлено ✓';
  btn.style.background = '#16a34a';
  btn.disabled = true;
  setTimeout(() => {
    btn.textContent = 'Отправить заявку';
    btn.style.background = '';
    btn.disabled = false;
    e.target.reset();
  }, 3000);
}

// Intersection observer for fade-up animations
const observer = new IntersectionObserver((entries) => {
  entries.forEach(el => {
    if (el.isIntersecting) {
      el.target.classList.add('fade-up');
      observer.unobserve(el.target);
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.about-card, .module-card, .benefit-item, .price-card, .tech-item')
  .forEach(el => observer.observe(el));
