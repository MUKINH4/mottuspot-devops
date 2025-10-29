# Mottu Spot - Sistema de Gestão de Pátios e Motos

## Integrantes
- Samuel Heitor – RM 556731
- Lucas Nicolini – RM 557613
- Renan Olivi – RM 557680

## 📋 Descrição da Solução

O **Mottu Spot** é uma aplicação web desenvolvida em Spring Boot para gerenciamento eficiente de pátios de motocicletas. O sistema permite o cadastro, visualização, edição e exclusão de pátios e suas respectivas motocicletas, oferecendo uma interface intuitiva para controle operacional de frotas.

### Principais Funcionalidades:
- ✅ **Gestão de Pátios**: CRUD completo para pátios com informações de endereço
- ✅ **Gestão de Motocicletas**: CRUD completo para motos associadas aos pátios
- ✅ **Sistema de Autenticação**: Controle de acesso com login/logout
- ✅ **Rastreamento IoT**: Integração com dispositivos para monitoramento das motos
- ✅ **Interface Responsiva**: Design moderno com Thymeleaf e Bootstrap

## 🏢 Benefícios para o Negócio

### Otimização Operacional
- **Controle de Localização**: Visualização em tempo real da distribuição de motos por pátio
- **Gestão de Frota**: Acompanhamento do status e disponibilidade das motocicletas
- **Redução de Custos**: Otimização da alocação de recursos e manutenção preventiva

### Eficiência Administrativa
- **Centralização de Dados**: Todas as informações de pátios e motos em um só lugar
- **Relatórios Automatizados**: Geração de relatórios sobre ocupação e utilização
- **Rastreabilidade**: Histórico completo de movimentações e alterações

### Escalabilidade
- **Arquitetura em Nuvem**: Preparado para crescimento da operação
- **Integração IoT**: Monitoramento automatizado através de dispositivos conectados
- **Flexibilidade**: Fácil adaptação para novos requisitos de negócio

## ☁️ Banco de Dados em Nuvem

O sistema utiliza **PostgreSQL** como banco de dados.


## 🔧 Operações CRUD Implementadas

### 📍 **Pátios**
- **Create**: Cadastro de novos pátios com endereço completo
- **Read**: Listagem e visualização detalhada de pátios
- **Update**: Edição de informações dos pátios
- **Delete**: Remoção de pátios

### 🏍️ **Motocicletas**
- **Create**: Registro de novas motos associadas a pátios
- **Read**: Consulta de motos por pátio e detalhes individuais
- **Update**: Atualização de dados das motocicletas
- **Delete**: Remoção de motos do sistema

### 📊 **Dados de Teste**
O sistema inclui pelo menos **2 registros reais** em cada tabela principal através das migrações Flyway, garantindo dados consistentes para demonstração.

## 🚀 Como Usar a Aplicação

### Pré-requisitos
- Java 17 ou superior
- PostgreSQL 12+ (em nuvem)
- Gradle 7+ ou usar o wrapper incluído

### 1. Acesso à Aplicação
Após o deploy, acesse a aplicação através da URL fornecida:
```
http://aci-app-mottu-spot.eastus.azurecontainer.io:8080
```

### 2. Interface Principal
- **Página Inicial**: Lista todos os pátios cadastrados
- **Adicionar Pátio**: Botão para cadastrar novos pátios
- **Visualizar Motos**: Click em um pátio para ver suas motocicletas

### 3. Gerenciamento de Pátios
1. **Cadastrar Pátio**:
   - Clique em "Adicionar Pátio"
   - Preencha os dados do endereço
   - Clique em "Salvar"

2. **Editar Pátio**:
   - Na lista de pátios, clique em "Editar"
   - Modifique os dados necessários
   - Salve as alterações

3. **Excluir Pátio**:
   - Clique em "Excluir" (disponível apenas para pátios sem motos)

### 4. Gerenciamento de Motocicletas
1. **Adicionar Moto**:
   - Acesse um pátio específico
   - Clique em "Adicionar Moto"
   - Preencha placa, descrição e status
   - Salve a moto

2. **Editar Moto**:
   - Na lista de motos do pátio, clique em "Editar"
   - Atualize as informações
   - Confirme as alterações

3. **Excluir Moto**:
   - Clique em "Excluir" na lista de motos

## 📦 Deploy com Scripts - Passo a Passo

### Pré-requisitos para Deploy
- Azure CLI instalado e configurado
- Conta Azure ativa
- Docker instalado (opcional para build local)
- Git para clonar o repositório

### 1. Clone do Repositório
```bash
git clone https://github.com/fiap-2tds-dtcc-fev25/2tdsa-cs-3-mottu-spot.git
cd 2tdsa-cs-3-mottu-spot
```

### 2. Build e Deploy da Infraestrutura
```bash
# Executar script de build (cria ACR e recursos)
chmod +x scripts/build.sh
./scripts/build.sh
```

**O que o script build.sh faz:**
- Cria Resource Group `rg-mottu-spot`
- Cria Azure Container Registry `mottuspot`
- Constrói e faz push da imagem Docker
- Configura autenticação do registry

### 3. Deploy da Aplicação
```bash
# Executar script de deploy
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

**O que o script deploy.sh faz:**
- Cria Azure Container Instances para PostgreSQL
- Configura banco de dados com usuário e senha
- Faz deploy da aplicação Spring Boot
- Configura variáveis de ambiente
- Expõe a aplicação na porta 80

### 4. Limpeza (Opcional)
```bash
# Script para remover todos os recursos
chmod +x scripts/cleanup.sh
./scripts/cleanup.sh
```

### 5. Verificação do Deploy
Após a execução dos scripts, verifique:

```bash
# Verificar status dos recursos
az group show --name rg-mottu-spot
```

### 🔧 Configurações dos Scripts

Os scripts utilizam as seguintes configurações padrão:
- **Resource Group**: `rg-mottu-spot`
- **Registry**: `mottuspot.azurecr.io`
- **Região**: `East US`
- **Banco**: PostgreSQL 17-alpine
- **Porta da Aplicação**: 8080

## 📊 Arquitetura do Sistema

<!-- Espaço reservado para o diagrama de arquitetura -->
![Arquitetura do Sistema](/src/main/resources/static/diagrama.png)

*Diagrama mostrando a arquitetura da solução com os componentes Azure e fluxo de dados*

## 🛠️ Tecnologias Utilizadas

### Backend
- **Spring Boot 3.x**: Framework principal
- **Spring MVC**: Controladores web
- **Spring Data JPA**: Persistência de dados
- **Spring Security**: Autenticação e autorização
- **Flyway**: Migração de banco de dados

### Frontend
- **Thymeleaf**: Template engine
- **Bootstrap 5**: Framework CSS
- **HTML5/CSS3**: Estrutura e estilo

### Database
- **PostgreSQL**: Banco de dados principal
- **H2**: Banco para testes (quando aplicável)

### DevOps
- **Docker**: Containerização
- **Azure Container Registry**: Registry de imagens
- **Azure Container Instances**: Hospedagem
- **Azure CLI**: Automação de deploy

## 📝 Estrutura do Projeto

```
2tdsa-cs-3-mottu-spot/
├── src/
│   ├── main/
│   │   ├── java/mottu_spot/mvc/
│   │   │   ├── controller/          # Controladores REST
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── service/             # Lógica de negócio
│   │   │   ├── repository/          # Repositórios de dados
│   │   │   └── config/              # Configurações
│   │   └── resources/
│   │       ├── templates/           # Templates Thymeleaf
│   │       ├── static/              # Recursos estáticos
│   │       └── db/migration/        # Scripts Flyway
├── scripts/
│   ├── build.sh                     # Script de build
│   ├── deploy.sh                    # Script de deploy
│   └── cleanup.sh                   # Script de limpeza
├── Dockerfile                       # Configuração Docker
├── build.gradle                     # Configuração Gradle
└── README.md                        # Documentação
```