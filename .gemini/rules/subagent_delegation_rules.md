# Production Android Sub-Agent Delegation Rules

## Core Mandate
To achieve a **production-grade Android application**, the primary AI agent MUST delegate tasks to specialized sub-agents. Utilize Gemini model tiers (`3.5-flash-lite` and `3.6-flash`) to minimize token cost, maximize execution speed, and maintain code quality.

---

## Model Allocation Tier Matrix

### Tier 1: `3.5-flash-lite` (High Frequency, Ultra-Fast, Minimal Token Cost)
- **Fast Search & Code Indexer:** Spawns [.agent/tasks/fast_search_indexer.md](file://.agent/tasks/fast_search_indexer.md) to grep source files, JSON schemas, or string resources.
- **Build & Log Debugger:** Spawns [.agent/tasks/build_diagnostics.md](file://.agent/tasks/build_diagnostics.md) to run `./gradlew` builds and extract stack trace errors silently.
- **Android Lint & Formatter:** Spawns [.agent/tasks/android_lint_checker.md](file://.agent/tasks/android_lint_checker.md) to run `./gradlew lint` / Detekt / Spotless formatting checks.

### Tier 2: `3.6-flash` (High Speed & Deep Architectural Reasoning)
- **Architecture & Code Quality Auditor:** Spawns [.agent/tasks/codebase_audit.md](file://.agent/tasks/codebase_audit.md) to audit Jetpack Compose state, ViewModel null safety, Hilt DI, and Room DB models.
- **Unit Test Generator:** Spawns [.agent/tasks/unit_test_generator.md](file://.agent/tasks/unit_test_generator.md) in `Workspace: "branch"` to generate JUnit5 / MockK / Turbine coroutine tests.
- **End-to-End (E2E) UI Tester:** Spawns [.agent/tasks/e2e_testing.md](file://.agent/tasks/e2e_testing.md) in `Workspace: "branch"` to write `ComposeTestRule` instrumentation tests and verify multi-screen navigation journeys.
- **Performance & Memory Leak Debugger:** Spawns [.agent/tasks/perf_memory_debugger.md](file://.agent/tasks/perf_memory_debugger.md) to analyze Heap Dumps (`.hprof`), LeakCanary traces, or frame render drops (jank).
