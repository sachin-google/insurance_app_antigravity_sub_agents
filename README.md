# Health Insurance Claim Android App

> A production-grade Android application built with Jetpack Compose, Kotlin Coroutines, StateFlow, and Clean Architecture — powered by **Automated AI Sub-Agents** for token-efficient, high-velocity development.

## 📱 Project Lifecycle via `android` CLI Commands

This project was developed and managed using Google's **`android` CLI tool**. Below is the complete command reference used across all phases of the project lifecycle:

### 1. Environment & Skill Initialization
```bash
# Initialize Android CLI environment and agent skills
android init
```

### 2. Project Scaffolding
```bash
# Create Jetpack Compose empty-activity template
android create empty-activity \
  --name="Claim Insurance App" \
  --output=./claim_insurance_app \
  --minSdk=24
```

### 3. SDK & Dependency Management
```bash
# List installed and available SDK packages
android sdk list --all

# Install required target Android SDK platform and build tools
android sdk install platforms/android-34 build-tools;34.0.0

# Update installed SDK components
android sdk update
```

### 4. Emulator & Device Management
```bash
# List available Android Virtual Devices (AVDs)
android emulator list

# Create a virtual device
android emulator create --name="Pixel_7_API_34"

# Launch emulator and wait until fully booted
android emulator start --name="Pixel_7_API_34"
```

### 5. Documentation Searching
```bash
# Search official Android Knowledge Base for Jetpack Compose & StateFlow guidance
android docs search "Jetpack Compose StateFlow navigation"
```

### 6. Application Deployment & Running
```bash
# Build, deploy, and launch debug APK on connected device/emulator
android run --debug
```

### 7. UI Layout & Visual Inspection
```bash
# Dump layout tree in formatted JSON for instant UI element debugging
android layout --pretty

# Capture device screen to file for visual verification
android screen capture -o=screen_preview.png
```

### 8. Project Metadata Analysis
```bash
# Generate descriptive metadata and build artifact locations
android describe --project_dir=.
```

---

## 🚀 Building Production-Grade Software with AI Sub-Agents

This project demonstrates an advanced AI pair-programming architecture using **Google Antigravity Sub-Agents**. By offloading high-frequency, noisy tasks (such as build compilation logs, multi-file grepping, static linting, and instrumentation testing) to background sub-agents, we achieve **80-90% token savings** and maintain a clean, zero-bloat primary context window.

---

### 🧠 Tiered Model Strategy

To maximize speed and accuracy while minimizing token costs, sub-agents are allocated to Gemini Flash model tiers based on task complexity:

| Model Tier | Sub-Agent Role | Task Scope | Assigned Task Template | Why This Model Tier? |
| :--- | :--- | :--- | :--- | :--- |
| **`3.5-flash-lite`** | **1. Fast Search & Indexer** | Grepping source files, finding string resources, listing dependencies. | [.agent/tasks/fast_search_indexer.md](file://.agent/tasks/fast_search_indexer.md) | High-frequency regex & file lookups requiring minimum latency and zero heavy reasoning. |
| **`3.5-flash-lite`** | **2. Build & Log Debugger** | Runs `./gradlew assembleDebug` or `./gradlew test`, extracts stack traces silently. | [.agent/tasks/build_diagnostics.md](file://.agent/tasks/build_diagnostics.md) | Keeps 5,000+ line Gradle compilation logs out of the main context window. |
| **`3.5-flash-lite`** | **3. Android Lint & Formatter** | Runs `./gradlew lint`, Detekt, or Spotless formatting checks. | [.agent/tasks/android_lint_checker.md](file://.agent/tasks/android_lint_checker.md) | Rule-based static code analysis checking with deterministic fixes. |
| **`3.6-flash`** | **4. Architecture & Quality Auditor** | Audits Jetpack Compose state isolation, ViewModel null-safety, Hilt DI, & Room DB schemas. | [.agent/tasks/codebase_audit.md](file://.agent/tasks/codebase_audit.md) | High-speed reasoning needed for Clean Architecture (MVVM/MVI) without unnecessary cost. |
| **`3.6-flash`** | **5. Unit Test Generator** | Generates JUnit5, MockK, Coroutines `runTest`, & Turbine StateFlow tests in a Git branch. | [.agent/tasks/unit_test_generator.md](file://.agent/tasks/unit_test_generator.md) | Requires understanding class contracts & mocking dependencies cleanly. |
| **`3.6-flash`** | **6. E2E & Compose UI Tester** | Writes & executes `ComposeTestRule` instrumentation tests for multi-screen user journeys. | [.agent/tasks/e2e_testing.md](file://.agent/tasks/e2e_testing.md) | High-level spatial & state tree reasoning required to test multi-screen UI flows. |
| **`3.6-flash`** | **7. Perf & Memory Leak Debugger** | Analyzes Heap Dumps (`.hprof`), LeakCanary traces, Compose re-composition jank, & rendering drops. | [.agent/tasks/perf_memory_debugger.md](file://.agent/tasks/perf_memory_debugger.md) | Deep diagnostic reasoning needed to trace un-cancelled jobs, retained Activity contexts, or memory leaks. |

---

## ⚙️ Automated Developer Workflow Rules

Sub-agent delegation is enforced automatically via workspace rules in [.gemini/rules/subagent_delegation_rules.md](file://.gemini/rules/subagent_delegation_rules.md) and [.agent/rules.md](file://.agent/rules.md):

1. **Automatic Build Verification:** Any code edit triggers a background `build_diagnostics` sub-agent (`3.5-flash-lite`). The main context receives only a 3-line status summary (Status, Error Line, Fix).
2. **Automatic Code Search:** Multi-file queries or dependency audits trigger a `fast_search_indexer` or `codebase_audit` sub-agent (`3.5-flash-lite` / `3.6-flash`). Raw search dumps never touch the main thread.
3. **Isolated Test Generation:** Unit and E2E Compose UI tests are drafted by sub-agents operating in isolated Git branch workspaces (`Workspace: "branch"`).

---

## 🛠️ Bootstrapping Sub-Agents for New Projects

To equip any new Android or Gradle project with this sub-agent framework, run the included setup script:

```bash
# Run in the root of your Android project
./setup_subagents.sh

# Commit the configuration to Git
git add .gemini/ .agent/
git commit -m "ci: setup production AI sub-agent workflows"
```

---

## 📱 Application Features

* **Part A (Policyholder Section):** Primary insured details, claim financial breakdown, enclosed bills dynamic manager, bank account NEFT details, and digital signature canvas.
* **Part B (Hospital Section):** Hospital registration details, treating doctor credentials, ICD-10 diagnosis codes, non-network infrastructure, and official seal/signature.
* **Claim Summary & Review:** Real-time financial calculations, checklist validation status, and single-click insurance claim submission.
* **Reimbursement Payout Confirmation:** Approved payout breakdown, direct NEFT bank payout tracker reference (`CLM-2026-889104`), and downloadable advice receipt.

---

## 🏗️ Project Architecture & Tech Stack

* **UI Framework:** Jetpack Compose (Material3 Design System)
* **Architecture:** Unidirectional Data Flow (UDF), ViewModel, Kotlin `StateFlow`
* **Concurrency:** Kotlin Coroutines & Structured Concurrency
* **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)
* **Testing:** JUnit, Compose Test Rule (`ComposeTestRule`), Sub-Agent E2E Verification
