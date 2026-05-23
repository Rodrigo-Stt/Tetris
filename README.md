## 🕹️ Como Jogar

| Tecla | Ação |
|---|---|
| ← → | Mover a peça |
| ↑ | Rotacionar |
| ↓ | Acelerar queda |
| Espaço | Queda instantânea |

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Apache Maven 3.x
- VS Code com Extension Pack for Java

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/tetris.git
```

2. Configure a conexão com o banco de dados:
```bash
# Renomeie o arquivo de exemplo
cp config.properties.exemplo config.properties

# Edite o config.properties com a URI do MongoDB Atlas
mongodb.uri=SUA_URI_AQUI
```

3. Compile e execute pelo VS Code ou pelo terminal:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="tetris.MainTetris"
```

---

## ⚙️ Configuração do Banco de Dados

O projeto utiliza o **MongoDB Atlas** (gratuito) para persistir o ranking de pontuações.

1. Crie uma conta em [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)
2. Crie um cluster gratuito (M0)
3. Crie um usuário com permissão de leitura e escrita
4. Libere o acesso de qualquer IP (`0.0.0.0/0`)
5. Copie a URI de conexão e cole no `config.properties`

---

## 🗂️ Estrutura do Projeto

```
tetris/
├── src/
│   └── main/
│       └── java/
│           └── tetris/
│               ├── MainTetris.java         # Ponto de entrada
│               ├── JanelaTetris.java       # Janela principal
│               ├── PainelJogo.java         # Lógica e renderização do jogo
│               ├── PainelProximaPeca.java  # Prévia da próxima peça
│               ├── Tetromino.java          # Enum com as 7 peças
│               ├── ModeloPontuacao.java    # Modelo de dados do ranking
│               ├── PontuacaoDAO.java       # Acesso ao MongoDB
│               ├── DialogRanking.java      # Tela de ranking
│               └── DialogFimDeJogo.java    # Tela de fim de jogo
├── pom.xml                                # Dependências Maven
└── README.md
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17** — Linguagem principal
- **Java Swing** — Interface gráfica
- **MongoDB Atlas** — Banco de dados em nuvem
- **MongoDB Driver Sync 5.1.0** — Conexão Java com MongoDB
- **Apache Maven** — Gerenciamento de dependências
- **Git / GitHub** — Controle de versão

---

## 📐 Conceitos de POO Aplicados

- **Encapsulamento** — Atributos `private final` em `ModeloPontuacao`
- **Abstração** — Enum `Tetromino` representando as 7 peças
- **Padrão Singleton** — Instância única do `PontuacaoDAO`
- **Padrão DAO** — Separação da lógica de acesso ao banco
- **Padrão Observer** — Callbacks (`Consumer` e `Runnable`) entre `PainelJogo` e `JanelaTetris`

---

## 📋 Funcionalidades

- ✅ 7 peças com cores distintas (I, O, T, L, J, S, Z)
- ✅ Movimentação e rotação em tempo real
- ✅ Detecção de colisão com bordas e peças fixadas
- ✅ Limpeza automática de linhas completas
- ✅ Sistema de níveis com aumento progressivo de velocidade
- ✅ Sidebar com pontuação, nível, linhas e próxima peça
- ✅ Ranking top 10 persistido no MongoDB Atlas
- ✅ Tela de fim de jogo personalizada

---
