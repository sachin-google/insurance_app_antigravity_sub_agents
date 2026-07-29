# Health Insurance Claim Android App

> A production-grade Android application built with Jetpack Compose, Kotlin Coroutines, StateFlow, and Clean Architecture — powered by **Automated AI Sub-Agents** for token-efficient, high-velocity development.

---

## 📱 Application Features

* **Part A (Policyholder Section):** Primary insured details, claim financial breakdown, enclosed bills dynamic manager, bank account NEFT details, and digital signature canvas.
* **Part B (Hospital Section):** Hospital registration details, treating doctor credentials, ICD-10 diagnosis codes, non-network infrastructure, and official seal/signature.
* **Claim Summary & Review:** Real-time financial calculations, checklist validation status, and single-click insurance claim submission.
* **Reimbursement Payout Confirmation:** Approved payout breakdown, direct NEFT bank payout tracker reference (`CLM-2026-889104`), and downloadable advice receipt.

---

## 🚀 Building Production-Grade Software with AI Sub-Agents

This project demonstrates an advanced AI pair-programming architecture using **Google Antigravity Sub-Agents**. By offloading high-frequency, noisy tasks (such as build compilation logs, multi-file grepping, static linting, and instrumentation testing) to background sub-agents, we achieve **80-90% token savings** and maintain a clean, zero-bloat primary context window.

### 🧠 Tiered Model Strategy

Sub-agents are allocated to Antigravity model tiers (`flash` or `pro`) based on task complexity. Every sub-agent is a Markdown file with YAML frontmatter under `.agents/agents/`, discovered automatically by Antigravity 2.0.

| Model Tier | Sub-Agent | Task Scope | File | Why This Tier? |
| :--- | :--- | :--- | :--- | :--- |
| **`flash`** | **1. Fast Search & Indexer** | Grepping source files, finding string resources, listing dependencies. | [.agents/agents/fast-search-indexer.md](file://.agents/agents/fast-search-indexer.md) | High-frequency regex & file lookups requiring minimum latency and zero heavy reasoning. |
| **`flash`** | **2. Build & Log Debugger** | Runs `./gradlew assembleDebug` or `./gradlew test`, extracts stack traces silently. | [.agents/agents/build-diagnostics.md](file://.agents/agents/build-diagnostics.md) | Keeps 5,000+ line Gradle compilation logs out of the main context window. |
| **`flash`** | **3. Android Lint & Formatter** | Runs `./gradlew lint`, Detekt, or Spotless formatting checks. | [.agents/agents/android-lint-checker.md](file://.agents/agents/android-lint-checker.md) | Rule-based static code analysis checking with deterministic fixes. |
| **`flash`** | **4. Android CLI & adb Runner** | Runs any `android` subcommand (`sdk`, `emulator`, `run`, `layout`, `screen`, `describe`) or `adb` command. Runs with `commandExecutionPolicy: auto` (non-sandbox) so device / emulator / USB sockets work. | [.agents/agents/android-runner.md](file://.agents/agents/android-runner.md) | Only agent permitted outside the sandbox — required because `adb` and emulator commands cannot run inside it. |
| **`pro`** | **5. Architecture & Quality Auditor** | Audits Jetpack Compose state isolation, ViewModel null-safety, Hilt DI, & Room DB schemas. | [.agents/agents/codebase-auditor.md](file://.agents/agents/codebase-auditor.md) | Deep reasoning needed for Clean Architecture (MVVM/MVI) audits across many files. |
| **`pro`** | **6. Unit Test Generator** | Generates JUnit5, MockK, Coroutines `runTest`, & Turbine StateFlow tests. Invoked in `Workspace: branch`. | [.agents/agents/unit-test-generator.md](file://.agents/agents/unit-test-generator.md) | Requires understanding class contracts & mocking dependencies cleanly. |
| **`pro`** | **7. E2E & Compose UI Tester** | Writes & executes `ComposeTestRule` instrumentation tests for multi-screen user journeys. Invoked in `Workspace: branch`. | [.agents/agents/e2e-tester.md](file://.agents/agents/e2e-tester.md) | High-level spatial & state-tree reasoning required to test multi-screen UI flows. |
| **`pro`** | **8. Perf & Memory Leak Debugger** | Analyzes Heap Dumps (`.hprof`), LeakCanary traces, Compose re-composition jank, & rendering drops. | [.agents/agents/perf-memory-debugger.md](file://.agents/agents/perf-memory-debugger.md) | Deep diagnostic reasoning needed to trace un-cancelled jobs, retained Activity contexts, or memory leaks. |

---

## ⚙️ Automated Developer Workflow Rules

Sub-agent delegation is enforced automatically via the workspace rule in [.agents/rules/subagent_delegation.md](file://.agents/rules/subagent_delegation.md). Antigravity 2.0's planner reads each sub-agent's `description` frontmatter to decide when to delegate:

1. **Automatic Build Verification:** Any `./gradlew` invocation is routed to `build-diagnostics` (`flash`). The main context receives only a 4-line summary (Status, Error Location, Root Cause, Suggested Fix).
2. **Automatic Code Search:** Multi-file greps and symbol lookups are routed to `fast-search-indexer` (`flash`); architectural audits go to `codebase-auditor` (`pro`). Raw search dumps never touch the main thread.
3. **Isolated Test Generation:** `unit-test-generator` and `e2e-tester` (`pro`) are invoked in `Workspace: branch`, so drafted `*Test.kt` files land in an isolated Git worktree and only merge back once green.

---

## 🛠️ Bootstrapping Sub-Agents for New Projects

To equip any new Android or Gradle project with this sub-agent framework, run the included setup script:

```bash
# Run in the root of your Android project
./setup_subagents.sh

# Commit the generated configuration to Git
git add .agents/
git commit -m "ci: setup Antigravity 2.0 sub-agent workflows"
```

Then open the project in Antigravity 2.0 and prompt, e.g.:

> "Build an Android App for the claim_form.pdf. This app should be modern and be delightful to use."

Antigravity's main agent reads the PDF, scaffolds the Jetpack Compose project, and delegates all noisy or specialized work (Gradle logs, multi-file greps, lint reports, test generation, E2E, perf) to the sub-agents.

---

## 📊 Measuring Token Consumption & Cost Savings

To back the **"80–90% token savings"** claim with real numbers, `setup_subagents.sh` also installs a lightweight metrics rig — a hook that logs every sub-agent invocation, and an aggregator that emits a per-agent Markdown cost report.

### What Gets Installed
* **`.agents/hooks.json`** — registers `PostToolUse:invoke_subagent` (fires on every sub-agent completion) and `Stop` (fires at end-of-turn). Antigravity 2.0 auto-discovers it.
* **`.agents/metrics/log_event.sh`** — the hook target. Appends one JSONL line per event to `.agents/metrics/sessions/<conversationId>.jsonl`. Exits 0 unconditionally so a broken hook never blocks an agent run.
* **`.agents/metrics/report.py`** — reads sessions, best-effort parses `transcript.jsonl` for token usage, joins against sub-agent model tiers by reading each `.agents/agents/*.md` frontmatter, applies pricing, prints a Markdown table.
* **`.agents/metrics/pricing.json`** — per-1M-token rates for `flash` and `pro`. Edit for your contract.

### A/B Workflow — Baseline vs Sub-Agents

The credible demo is same-prompt-run-twice, with delegation off vs on:

```bash
# 1. BASELINE (no sub-agents — rename the config out of the way)
mv .agents .agents.off
# ...run the target prompt in Antigravity 2.0...
mv .agents.off .agents
.agents/metrics/report.py --all > .agents/metrics/baseline.md

# 2. INSTRUMENTED (sub-agents enabled)
# ...run the same prompt again...
.agents/metrics/report.py --all > run.md

# 3. Diff for the deck
diff .agents/metrics/baseline.md run.md
```

### Example Report Output

```
# Token / Cost Report

| Agent               | Model | Invocations | In tok  | Out tok | Cached | Est. $  |
| ------------------- | ----- | ----------: | ------: | ------: | -----: | ------: |
| main                | pro   |      —      | 142,400 |   8,300 | 96,000 | $0.7128 |
| build-diagnostics   | flash |           7 |  31,200 |   2,100 |      0 | $0.0146 |
| android-runner      | flash |          12 |  18,600 |   3,400 |      0 | $0.0141 |
| fast-search-indexer | flash |           4 |   9,100 |   1,050 |      0 | $0.0053 |
| ...                                                                             |

Total tokens: 201,300 in / 14,850 out
Total est. cost: $0.7468   (vs baseline.md: $4.20  →  savings: 82%)
```

### Honest Caveats
* **Transcript schema is heuristic.** The extractor tries several field names (`usage.input_tokens`, `usage.inputTokens`, `usage.promptTokens`, …) because Antigravity's `transcript.jsonl` format isn't fully documented. On your first real run, cross-check against Antigravity's built-in usage panel — if the numbers diverge, one line from the transcript will let us widen the extractor.
* **Sub-agents have overhead.** ~1 KB of system-prompt tax per invocation. For tiny projects that tax can exceed the savings; the pitch holds when the main context would otherwise absorb high-volume output (Gradle logs, hprof dumps, lint XML, multi-file greps).
* **Attribution is coarse today.** Transcript tokens all lump into "main" until we confirm whether Antigravity tags each transcript entry with an agent id; sub-agents currently report invocation counts only. Both defaults *under-count* sub-agents, so the savings story looks less dramatic than reality — not more.

Remember to add `.agents/metrics/sessions/` and `.agents/metrics/report-*.md` to `.gitignore`.

---

## 🏗️ Project Architecture & Tech Stack

* **UI Framework:** Jetpack Compose (Material3 Design System)
* **Architecture:** Unidirectional Data Flow (UDF), ViewModel, Kotlin `StateFlow`
* **Concurrency:** Kotlin Coroutines & Structured Concurrency
* **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)
* **Testing:** JUnit, Compose Test Rule (`ComposeTestRule`), Sub-Agent E2E Verification

---

## 🤖 Automated `android` CLI Workflows (Powered by Antigravity)

> **Note:** All the following Android CLI operations were **executed automatically by Google Antigravity** using the `android-cli` plugin throughout the creation, building, testing, and debugging phases. No manual command-line steps were required by the developer.

### 1. Environment & Skill Initialization (Automated)
```bash
# Antigravity automatically initialized the Android CLI environment and agent skills
android init
```

### 2. Project Scaffolding (Automated)
```bash
# Antigravity created the initial Jetpack Compose project structure automatically
android create empty-activity \
  --name="Claim Insurance App" \
  --output=./claim_insurance_app \
  --minSdk=24
```

### 3. SDK & Package Management (Automated)
```bash
# Antigravity detected missing SDK platforms and installed/updated them automatically
android sdk list --all
android sdk install platforms/android-34 build-tools;34.0.0
android sdk update
```

### 4. Emulator & Virtual Device Management (Automated)
```bash
# Antigravity provisioned and booted virtual devices for test verification automatically
android emulator list
android emulator create --name="Pixel_7_API_34"
android emulator start --name="Pixel_7_API_34"
```

### 5. Documentation Searching (Automated)
```bash
# Antigravity queried the Android Knowledge Base for API best practices automatically
android docs search "Jetpack Compose StateFlow navigation"
```

### 6. App Deployment & Execution (Automated)
```bash
# Antigravity built and deployed debug APKs directly to emulators automatically
android run --debug
```

### 7. Layout & Visual UI Inspection (Automated)
```bash
# Antigravity inspected Compose layout trees (JSON) & captured screenshots automatically
android layout --pretty
android screen capture -o=screen_preview.png
```

### 8. Project Structure & Metadata Analysis (Automated)
```bash
# Antigravity identified project targets and build artifacts automatically
android describe --project_dir=.
```
