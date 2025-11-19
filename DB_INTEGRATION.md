# Integração MySQL — Conecta Bairro

Este documento descreve como configurar e executar a base MySQL e integrar o front-end, back-end Node (Prisma) e back-end Java (Spring Boot).

Pré-requisitos
- MySQL 8+ instalado e rodando
- Java 21 (JDK 21) instalado (ou use SDKMAN / Adoptium)
- Maven (para backend Java)
- Node.js 18+ (para backend Node e front-end)

Passos gerais

1) Criar banco e usuário MySQL

Há um script PowerShell que automatiza a criação do banco e do usuário: `scripts/create_mysql_db.ps1`.
Execute no PowerShell (ele solicitará a senha do root de forma segura):

```powershell
cd .\PROJETOFINAL
.\scripts\create_mysql_db.ps1
```

Se preferir executar manualmente, use o cliente MySQL:

```powershell
mysql -u root -p
# no prompt mysql:
CREATE DATABASE IF NOT EXISTS conecta_bairro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'conecta_user'@'localhost' IDENTIFIED BY 'conecta_pass';
GRANT ALL PRIVILEGES ON conecta_bairro.* TO 'conecta_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

2) Java (Spring Boot)
- `conecta-bairro-java/src/main/resources/application.yml` já aponta para MySQL. Edite variáveis de ambiente se necessário:
  - `DB_PASSWORD` (se usar `root` com senha)
  - `JWT_SECRET`

- Para compilar/rodar com Maven (PowerShell):

```powershell
cd .\conecta-bairro-java
mvn -U clean package
mvn spring-boot:run
```

- Ou execute o `schema.sql` manualmente para criar o esquema (opcional se usar JPA/Hibernate auto-ddl):

```powershell
mysql -u conecta_user -p conecta_bairro < src/main/resources/schema.sql
```

3) Node (Prisma)

Existe um script para configurar o Node/Prisma automaticamente: `scripts/setup_node_prisma.ps1`.
Ele instala dependências, gera o Prisma Client e roda `prisma migrate dev` para criar as tabelas.

Execute:

```powershell
cd .\PROJETOFINAL
.\scripts\setup_node_prisma.ps1
```

Alternativamente manualmente:

```powershell
cd .\conecta-bairro-api
copy .env.example .env
# edite .env colocando DATABASE_URL para MySQL (ex: mysql://conecta_user:conecta_pass@localhost:3306/conecta_bairro)

npm install
npx prisma generate --schema=prisma/schema.prisma
npx prisma migrate dev --name init --schema=prisma/schema.prisma

npm run dev
```
4) Front-end (React)
- `conecta-bairro-web` usa `REACT_APP_API_URL` para apontar a API. Exemplo `.env` na raiz do front:

```
REACT_APP_API_URL=http://localhost:3000/api
```

- Inicie o front:

```powershell
cd .\conecta-bairro-web
npm install
npm start
```

