// Menu Mobile
const navToggle = document.querySelector('.nav-toggle');
const navMenu = document.querySelector('.nav-menu');

navToggle.addEventListener('click', () => {
    navMenu.classList.toggle('active');
});

// Fechar menu ao clicar em um link
document.querySelectorAll('.nav-menu a').forEach(link => {
    link.addEventListener('click', () => {
        navMenu.classList.remove('active');
    });
});

// Filtros de Serviços
const filterButtons = document.querySelectorAll('.filter-btn');
const servicesGrid = document.querySelector('.services-grid');

// Lista de serviços exemplo
const services = [
    { name: 'Limpeza Residencial', category: 'domesticos', icon: 'fas fa-broom' },
    { name: 'Eletricista', category: 'manutencao', icon: 'fas fa-bolt' },
    { name: 'Cabeleireiro', category: 'beleza', icon: 'fas fa-cut' },
    { name: 'Aulas de Inglês', category: 'aulas', icon: 'fas fa-book' },
    { name: 'Encanador', category: 'manutencao', icon: 'fas fa-wrench' },
    { name: 'Manicure', category: 'beleza', icon: 'fas fa-hand-sparkles' }
];

// Função para renderizar serviços
function renderServices(category = 'todos') {
    const filteredServices = category === 'todos' 
        ? services 
        : services.filter(service => service.category === category);

    servicesGrid.innerHTML = filteredServices.map(service => `
        <div class="service-card">
            <i class="${service.icon}"></i>
            <h3>${service.name}</h3>
            <a href="#" class="btn btn-primary">Contratar</a>
        </div>
    `).join('');
}

// Inicializar serviços
renderServices();

// Event listeners para filtros
filterButtons.forEach(button => {
    button.addEventListener('click', () => {
        // Remover classe active de todos os botões
        filterButtons.forEach(btn => btn.classList.remove('active'));
        // Adicionar classe active ao botão clicado
        button.classList.add('active');
        // Filtrar serviços
        renderServices(button.dataset.filter);
    });
});

// Smooth scroll para links internos
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            window.scrollTo({
                top: target.offsetTop - 80,
                behavior: 'smooth'
            });
        }
    });
});

// Animação de números nas estatísticas
const stats = document.querySelectorAll('.stat-number');
let animated = false;

function animateStats() {
    if (animated) return;

    stats.forEach(stat => {
        const target = parseInt(stat.textContent);
        let current = 0;
        const increment = target / 60; // Animar em 60 passos
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                stat.textContent = target.toString() + '+';
                clearInterval(timer);
            } else {
                stat.textContent = Math.floor(current).toString() + '+';
            }
        }, 30);
    });

    animated = true;
}

// Observer para animar estatísticas quando visíveis
const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            animateStats();
        }
    });
});

const statsSection = document.querySelector('.stats');
if (statsSection) {
    observer.observe(statsSection);
}

// Validação do formulário de contato
const contactForm = document.querySelector('.contact-form');

contactForm.addEventListener('submit', (e) => {
    e.preventDefault();
    // Aqui você pode adicionar a lógica para enviar o formulário
    alert('Mensagem enviada com sucesso!');
    contactForm.reset();
});