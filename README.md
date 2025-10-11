# Automação de Vagas de Estágio

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9-red)
![Docker](https://img.shields.io/badge/Docker-blue)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-green)
![Fly.io](https://img.shields.io/badge/Deploy-Fly.io-purple)

Este projeto é um robô automatizado que busca vagas de estágio e trainee em portais de emprego e envia notificações em tempo real para um canal do Discord.

## 🚀 Funcionalidades

- **Busca Contínua:** O robô opera em um loop infinito, procurando por novas vagas periodicamente.
- **Web Scraping Inteligente:** Utiliza scrapers para extrair informações de vagas diretamente de APIs de portais de emprego, como a Gupy.
- **Filtragem Flexível:** Possui um sistema de filtragem que analisa os títulos das vagas para garantir que apenas as mais relevantes sejam selecionadas, mesmo que não correspondam exatamente ao termo de busca.
- **Notificações no Discord:** Integra-se com o Discord através de webhooks para notificar sobre novas vagas encontradas, enviando título, descrição e link.
- **Evita Duplicidade:** Mantém um histórico das vagas já enviadas para não notificar a mesma vaga múltiplas vezes.
- **CI/CD com Deploy Automático:** Configurado com GitHub Actions para fazer o deploy automático da aplicação na plataforma Fly.io a cada `push` na branch `master`.
- **Containerizado:** Utiliza Docker para criar um ambiente consistente e facilitar o deploy.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Build:** Apache Maven
- **Bibliotecas:**
  - Jsoup (para requisições HTTP e parsing)
  - Gson (para manipulação de JSON)
  - OkHttp (cliente HTTP para o Discord)
- **Infraestrutura:**
  - Docker
  - Fly.io (Hospedagem)
  - GitHub Actions (CI/CD)

## ⚙️ Configuração

Para executar este projeto, é necessário configurar a seguinte variável de ambiente:

- `DISCORD_URL`: A URL do webhook do canal do Discord onde as notificações serão enviadas.

O projeto está configurado no `fly.toml` para utilizar um volume persistente montado em `/var/data`, onde o arquivo `vagas_enviadas.txt` é armazenado para manter o histórico entre os deploys.

## 🔧 Como Executar Localmente

1.  Clone o repositório:
    ```bash
    git clone https://github.com/vitinh0z/AutomacaoVagasEstagio.git
    ```
2.  Navegue até o diretório do projeto:
    ```bash
    cd AutomacaoVagasEstagio
    ```
3.  Defina a variável de ambiente com a sua URL do Discord.
4.  Compile e empacote o projeto com o Maven:
    ```bash
    mvn clean package
    ```
5.  Execute o arquivo `.jar` gerado:
    ```bash
    java -jar target/AutomacaoVagasEstagio-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```
