# Desafios Android 📱

Repositório contendo soluções e desafios de desenvolvimento de aplicativos Android desenvolvidos com **Kotlin**, **Jetpack Compose**, **Arquitetura MVVM**, **Room Database**, consumo de APIs REST e **Testes Unitários**.

> 📌 **Organização do Repositório:** Cada pasta neste repositório contém o código-fonte de um aplicativo específico e possui o nome oficial do projeto.

---

## 📁 Aplicativos Desenvolvidos

### 1. 💰 [`FinanceTracker`](./FinanceTracker)
- **Nome do App:** App de Monitoramento de Gastos Pessoais
- **Tecnologias:** 
  - **Linguagem & UI:** Kotlin, Jetpack Compose (Material 3)
  - **Arquitetura:** MVVM (Model-View-ViewModel) com `LiveData` e `StateFlow`
  - **Banco de Dados Local:** Room Database (`ExpenseEntity`, `ExpenseDao`)
  - **Consumo de API:** Retrofit + Gson (AwesomeAPI - Cotação de Câmbio de Moedas em tempo real para BRL, USD, EUR, GBP)
  - **Testes Unitários:** JUnit4, MockK, Coroutines Test, InstantTaskExecutorRule (`ExpenseViewModelTest.kt`)
  - **Design System & Telemetria:** Tema Dark Mode Glassmorphism com acentos Neon, gráfico Donut Chart em Canvas, gráficos Spline preditivos, histórico com busca/filtros multi-tag e comprovante analítico com hash fiscal.
- **Prompt para Stitch.io:** Artefato de design gerado com especificações completas de componentes e temas.

---

## 🚀 Como Executar

1. Clone este repositório:
   ```bash
   git clone https://github.com/dierlisson/Desafios-ENET.git
   ```
2. Abra o diretório do aplicativo desejado no **Android Studio**:
   - Exemplo: Navegue até a pasta `FinanceTracker`.
3. Para compilar e rodar os testes unitários da ViewModel via linha de comando:
   ```bash
   cd FinanceTracker
   ./gradlew testDebugUnitTest
   ```
