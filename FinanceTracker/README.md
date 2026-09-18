# 💰 FinanceTracker - App de Monitoramento de Gastos Pessoais

Aplicativo nativo Android moderno desenvolvido em **Kotlin** e **Jetpack Compose** para gestão e acompanhamento de finanças pessoais, conversão de moedas em tempo real via API pública, e telemetria financeira.

---

## 🚀 Tecnologias & Arquitetura

- **Linguagem:** Kotlin (Target Java 17)
- **UI Framework:** Jetpack Compose + Material 3 (Tema Dark Mode Glassmorphism)
- **Arquitetura:** MVVM (Model-View-ViewModel) com `LiveData` e `StateFlow`
- **Banco de Dados Local:** Room Database (`ExpenseEntity`, `ExpenseDao`, `AppDatabase`)
- **Consumo de API:** Retrofit 2 + Gson (AwesomeAPI `economia.awesomeapi.com.br` para cotação em tempo real de `USD-BRL`, `EUR-BRL` e `GBP-BRL`)
- **Testes Unitários:** JUnit 4, MockK, Coroutines Test, InstantTaskExecutorRule (`ExpenseViewModelTest.kt`)

---

## 📱 Telas e Funcionalidades

1. **Dashboard Overview (`DashboardScreen`)**:
   - Header com seletor de moedas (`BRL 🇧🇷`, `USD 🇺🇸`, `EUR 🇪🇺`, `GBP 🇬🇧`).
   - Tabela de Câmbio ao Vivo (AwesomeAPI).
   - Resumo Mensal com Gráfico Donut Chart em Canvas.
   - Lista de transações com busca e filtros por categoria.
2. **Telemetria & Projeções (`AnalyticsTelemetriaScreen`)**:
   - Gráficos Spline preditivos de fluxo de caixa, Burn Rate diário, Índice de poupança e envelopes orçamentários por categoria.
3. **Histórico Avançado (`TransactionsHistoryScreen`)**:
   - Filtros multi-tag, leitor QR Code simulado e exportação de relatórios (CSV/PDF).
4. **Configurações & Console (`SettingsScreen`)**:
   - Ajuste fino de efeitos visuais (slider de densidade de partículas neon 4 a 50), simulador de haptic/áudio e biometria.
5. **Comprovante Analítico (`ExpenseDetailDrawerScreen`)**:
   - Detalhes da transação, geolocalização simulada, hash fiscal (`TX-9842-88B-04`) e ações de repetição.
6. **Adicionar/Editar Gasto (`AddEditExpenseDialog`)**:
   - Modal glassmorphic para inclusão e alteração de despesas.

---

## 🧪 Como Executar os Testes Unitários

No diretório deste aplicativo (`FinanceTracker`), execute:

```bash
./gradlew testDebugUnitTest
```

---

## 🎨 Design System & Prompt Stitch.io

As diretrizes visuais e o prompt mestre para geração de UI no **Stitch.io** estão configurados para exportação rápida.
