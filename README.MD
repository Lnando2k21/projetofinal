# PROJETO FINAL - Conecta Bairro

## Estrutura do Projeto
- `index.html` - Landing page estática (frontend simples)
- `conecta-bairro-api/` - Backend (Express + Prisma skeleton)
- `conecta-bairro-web/` - Frontend (React app skeleton)
- `css/` - Arquivos de estilo, incluindo acessibilidade
- `js/` - Scripts JavaScript, incluindo funcionalidades de acessibilidade

## Recursos de Acessibilidade

### Componentes de Acessibilidade
- **Tema Escuro/Claro**: Sistema de temas com alto contraste e transições suaves
- **Leitor de Tela**: Suporte a narrativa de conteúdo com instruções específicas por página
- **VLibras**: Integração com tradutor de Libras do Governo Federal
- **Navegação por Teclado**: Suporte completo a navegação via teclado
- **Links de Atalho**: Opção "Pular para conteúdo principal"

### Arquivos de Acessibilidade
- `css/accessibility.css`: Estilos e variáveis de tema
- `js/accessibility.js`: Gerenciamento de acessibilidade

### Como Usar os Recursos
1. **Alternar Tema**:
   - Botão no topo direito da página
   - Atalho de teclado (configurável)
   - Persiste preferência do usuário

2. **Áudio e Narração**:
   - Botão de áudio para ouvir conteúdo
   - Instruções específicas por página
   - Controle de velocidade de narração

3. **VLibras**:
   - Widget disponível no canto direito
   - Tradução automática do conteúdo
   - Interface personalizável

4. **Navegação**:
   - Use Tab para navegar entre elementos
   - Enter para ativar links e botões
   - Esc para fechar modais
   - Alt + 1 para pular para conteúdo principal

## Configuração do Backend
1. Acesse `conecta-bairro-api`
2. Copie `.env.example` para `.env` e configure:
   ```
   DATABASE_URL="seu_banco_de_dados"
   JWT_SECRET="sua_chave_secreta"
   ```
3. Instale as dependências:
   ```bash
   npm install
   ```
4. Execute as migrações:
   ```bash
   npx prisma migrate dev --name init
   ```
5. Inicie o servidor:
   ```bash
   npm run dev
   ```

## Configuração do Frontend
1. Acesse `conecta-bairro-web`
2. Copie `.env.example` para `.env` e configure:
   ```
   REACT_APP_API_URL="http://localhost:3000"
   ```
3. Instale as dependências:
   ```bash
   npm install
   ```
4. Inicie o aplicativo:
   ```bash
   npm start
   ```

## Diretrizes de Acessibilidade
- WCAG 2.1 Nível AA
- eMAG (Modelo de Acessibilidade em Governo Eletrônico)
- Suporte a leitores de tela (NVDA, JAWS, VoiceOver)
- Compatível com diferentes dispositivos de entrada

## Testes de Acessibilidade
- Use navegadores diferentes
- Teste com leitores de tela
- Verifique contraste de cores
- Confirme navegação por teclado
- Valide tradução em Libras

## Observações
- Os serviços e controllers do backend são skeletons com armazenamento em memória (para testes rápidos)
- Recomendado conectar o Prisma e converter os serviços para usar `@prisma/client`
- Ajuste imagens, estilos e lógica no frontend conforme design final
- Mantenha os padrões de acessibilidade ao fazer modificações

## Contribuição
Ao contribuir com o projeto, certifique-se de:
1. Manter os padrões de acessibilidade
2. Testar com diferentes dispositivos de assistência
3. Documentar novos recursos de acessibilidade
4. Seguir as diretrizes WCAG e eMAG
