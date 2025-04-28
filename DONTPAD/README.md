# 🚨 **Sistema Híbrido de Rastreamento e Gestão de Motocicletas em Pátios**  
## 🏍️ **Radar Motu**

## Índice
- [🚨 **Sistema Híbrido de Rastreamento e Gestão de Motocicletas em Pátios**](#-sistema-híbrido-de-rastreamento-e-gestão-de-motocicletas-em-pátios)
  - [🏍️ **Radar Motu**](#️-radar-motu)
  - [Índice](#índice)
  - [📍 **Objetivo Geral**](#-objetivo-geral)
  - [🌐 **Estrutura do Sistema**](#-estrutura-do-sistema)
    - [1. **Sistema Web (Gestão)**](#1-sistema-web-gestão)
    - [2. **App Mobile - Perfil Localização**](#2-app-mobile---perfil-localização)
    - [3. **App Mobile - Perfil Cadastro**](#3-app-mobile---perfil-cadastro)
  - [📏 **Rastreamento**](#-rastreamento)
    - [**Localização Externa:**](#localização-externa)
    - [**Localização Interna:**](#localização-interna)
  - [🖋️ **Editor de Mapas no Sistema Web**](#️-editor-de-mapas-no-sistema-web)
    - [**Funcionalidades:**](#funcionalidades)
    - [**Integração:**](#integração)
  - [📅 **Roadmap de Implantação**](#-roadmap-de-implantação)
  - [🛠️ **Tecnologias Sugeridas**](#️-tecnologias-sugeridas)
  - [📊 **Diferenciais do Sistema**](#-diferenciais-do-sistema)
  - [🧰 **Materiais para Uso**](#-materiais-para-uso)
  - [🔧 **Arquitetura do Sistema – Radar Motu**](#-arquitetura-do-sistema--radar-motu)

---

## 📍 **Objetivo Geral**

Criar um sistema web e mobile para rastreamento, gestão e navegação de motocicletas em pátios, utilizando tecnologias de localização híbridas (GPS/GSM + BLE), com um editor de mapas integrado e operação intuitiva.

---

## 🌐 **Estrutura do Sistema**

### 1. **Sistema Web (Gestão)**
- Interface administrativa para:
  - 📍 **Visualizar todos os pátios**
  - 🛠️ **Consultar localização e status de cada moto**
  - 🗺️ **Editar mapas dos pátios (zonas, boxes, caminhos)**
  - 🚀 **Ver rotas e movimentações em tempo real**
  - 🔍 **Filtrar por placa, status, zona, filial**

### 2. **App Mobile - Perfil Localização**
- Funcionalidades:
  - 🔢 **Digitar a placa ou código da moto**
  - 📍 **Receber posição atual da moto (Pátio, Zona, Box)**
  - 🧭 **Botão "Navegar até a moto"**
  - 📶 **Sistema traça caminho lógico pelo pátio**
  - 🔄 **BLE identifica aproximação da moto**

### 3. **App Mobile - Perfil Cadastro**
- Funcionalidades:
  - 📷 **Apontar câmera para placa/documento**
  - 🧠 **OCR identifica automaticamente: placa, modelo, ano**
  - 🔎 **Consulta API de veículo e preenche os dados**
  - 🔑 **Detecta o rastreador BLE mais próximo e associa automaticamente à moto**

---

## 📏 **Rastreamento**

### **Localização Externa:**
- 🌍 **GPS + GSM**: localiza a moto fora do pátio (ruas, cidades).
- 📍 **Exibe em mapa a filial e endereço aproximado.**

### **Localização Interna:**
- 📶 **BLE**: identifica aproximação dentro do pátio.
- 🗺️ **O app mostra zona e box da moto (mapa do pátio).**
- 🔄 **Permite estimar a distância e guiar o operador até a moto.**

---

## 🖋️ **Editor de Mapas no Sistema Web**

### **Funcionalidades:**
- 📥 **Upload de planta baixa do pátio (imagem).**
- 🧭 **Definição de zonas e boxes com drag-and-drop.**
- ➡️ **Criação de caminhos lógicos (Entrada → Zona A → Box 3).**
- 💾 **Salva layout como JSON para navegação e visualização.**

### **Integração:**
- 🌐 **Web e app usam o mesmo mapa.**
- 🛵 **Motos aparecem no mapa em tempo real com cor por status.**

---

## 📅 **Roadmap de Implantação**

| Etapa                 | Período           | Objetivo                                        |
| --------------------- | ----------------- | ----------------------------------------------- |
| MVP Web + App Básico  | Abril - Maio      | Busca por placa, dashboard e navegação lógica   |
| Integração BLE + OCR  | Junho             | Leitura de placa + associação de rastreador BLE |
| Editor de Mapas       | Junho             | Criação e edição de plantas de pátios           |
| Protótipo Operacional | Julho             | Tudo funcionando com pátio piloto               |
| Expansão de uso       | Agosto - Setembro | Início da escala para outras filiais            |

---

## 🛠️ **Tecnologias Sugeridas**
- **Frontend Web**: React.js + Leaflet.js (mapa).
- **App Mobile**: React Native (expo) + BLE + OCR + Maps.
- **Backend/API**: Node.js (NestJS) ou Python (FastAPI).
- **Banco de Dados**: PostgreSQL ou MongoDB.
- **OCR**: Google ML Kit / Tesseract.
- **BLE**: Native Bluetooth / Web Bluetooth.
- **Editor de mapa**: react-konva ou fabric.js (drag and drop sobre canvas).

---

## 📊 **Diferenciais do Sistema**
- 🚫 **Sem necessidade de antenas fixas.**
- 🔄 **Cadastro automático com OCR.**
- 🌐 **Acesso multi-filial com mapas independentes.**
- 🛍️ **Navegação indoor tipo shopping.**
- 🔗 **Integração completa entre dispositivos, mapa e status.**

---

## 🧰 **Materiais para Uso**

🧰 **Tabela de Hardware - MVP Rastreamento de Motos**

| Item                  | Função                              | Quantidade (MVP) | Preço Estimado (R$) |
| --------------------- | ----------------------------------- | ---------------- | ------------------- |
| ST-901 GPS Tracker    | Localização geral via GPS/GSM       | 5 unidades       | R$ 100              |
| Chip SIM M2M (dados)  | Comunicação via rede celular        | 5 chips          | R$ 20               |
| Tag BLE (Minew i6)    | Localização de proximidade no pátio | 10 unidades      | R$ 40               |
| Bateria CR2032        | Alimentar as tags BLE               | 10 unidades      | R$ 20               |
| Power Bank (opcional) | Alimentar ST-901 fora da moto       | 1 unidade        | R$ 70               |

**TOTAL ESTIMADO**: |                  | **R$ 1.270**

---

## 🔧 **Arquitetura do Sistema – Radar Motu**

```plaintext
                   +-----------------------+
                   |     Usuário Web       |
                   |   (Administração)     |
                   +----------+------------+
                              |
                              v
+------------------+   API REST/GraphQL    +------------------+
|      App         | <-------------------> |     Back-end     |
|   Mobile - BLE   |                       | (Node.js / Python)|
+------------------+                       +--------+---------+
                              ▲                     |
+------------------+          |                     |
|  App - Cadastro  | <--------+                     v
+------------------+                              Banco de Dados
                                                    (PostgreSQL/MongoDB)
                                                             |
                                                             v
                                               +--------------------------+
                                               |    Serviços Externos     |
                                               | - FIPE / Detran / OCR    |
                                               | - Google Maps / BLE UUID |
                                               +--------------------------+
