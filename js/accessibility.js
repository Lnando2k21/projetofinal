// Configuração de acessibilidade
const AccessibilityManager = {
    // Configurações de áudio
    audioConfig: {
        lang: 'pt-BR',
        rate: 0.9,
        defaultInstructions: `
            Bem-vindo ao Conecta Bairro.
            Use Tab para navegar entre elementos.
            Use Enter para ativar botões e links.
            No topo da página, você encontra botões para alternar o modo escuro e ouvir instruções.
            O widget de Libras está disponível no canto direito da tela.
        `
    },

    // Inicialização
    init(pageSpecificInstructions = '') {
        this.initAudio(pageSpecificInstructions);
        this.initKeyboardNav();
        this.initVLibras();
        this.setupSkipLink();
    },

    // Sistema de áudio
    initAudio(pageSpecificInstructions) {
        const audioButton = document.getElementById('audioButton');
        if (!audioButton) return;

        const synth = window.speechSynthesis;
        const instructions = pageSpecificInstructions + this.audioConfig.defaultInstructions;
        
        audioButton.addEventListener('click', () => {
            if (synth.speaking) {
                synth.cancel();
                audioButton.querySelector('.theme-text').textContent = 'Ouvir Página';
                return;
            }

            const utterance = new SpeechSynthesisUtterance(instructions);
            utterance.lang = this.audioConfig.lang;
            utterance.rate = this.audioConfig.rate;
            
            utterance.onstart = () => {
                audioButton.querySelector('.theme-text').textContent = 'Parar Áudio';
                audioButton.setAttribute('aria-pressed', 'true');
            };
            
            utterance.onend = () => {
                audioButton.querySelector('.theme-text').textContent = 'Ouvir Página';
                audioButton.setAttribute('aria-pressed', 'false');
            };
            
            synth.speak(utterance);
        });
    },

    // Navegação por teclado
    initKeyboardNav() {
        const focusableElements = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
        
        document.addEventListener('keydown', (e) => {
            // Atalho para pular para o conteúdo principal
            if (e.key === 'Enter' && document.activeElement.classList.contains('skip-to-main')) {
                e.preventDefault();
                const main = document.querySelector('main, [role="main"]');
                if (main) {
                    main.focus();
                    main.setAttribute('tabindex', '-1');
                }
            }

            // Loop de foco
            if (e.key === 'Tab') {
                const focusable = [...document.querySelectorAll(focusableElements)];
                const firstFocusable = focusable[0];
                const lastFocusable = focusable[focusable.length - 1];

                if (e.shiftKey && document.activeElement === firstFocusable) {
                    e.preventDefault();
                    lastFocusable.focus();
                } else if (!e.shiftKey && document.activeElement === lastFocusable) {
                    e.preventDefault();
                    firstFocusable.focus();
                }
            }
        });
    },

    // Inicialização do VLibras
    initVLibras() {
        if (window.VLibras) {
            new window.VLibras.Widget('https://vlibras.gov.br/app');
        }
    },

    // Link para pular para o conteúdo principal
    setupSkipLink() {
        const skipLink = document.createElement('a');
        skipLink.href = '#main';
        skipLink.className = 'skip-to-main';
        skipLink.textContent = 'Pular para o conteúdo principal';
        document.body.insertBefore(skipLink, document.body.firstChild);
    },

    // Anúncio para leitores de tela
    announceMessage(message) {
        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.setAttribute('aria-atomic', 'true');
        announcement.className = 'visually-hidden';
        announcement.textContent = message;
        document.body.appendChild(announcement);
        setTimeout(() => announcement.remove(), 1000);
    }
};

// Inicializar quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
    // Verificar se estamos em uma página específica e passar instruções apropriadas
    let pageSpecificInstructions = '';
    if (window.location.pathname.includes('login.html')) {
        pageSpecificInstructions = 'Esta é a página de login. Use o formulário para entrar com seu email e senha. ';
    } else if (window.location.pathname.includes('registro.html')) {
        pageSpecificInstructions = 'Esta é a página de registro. Preencha o formulário para criar sua conta. ';
    }
    // Inicializar com instruções específicas da página
    AccessibilityManager.init(pageSpecificInstructions);
});