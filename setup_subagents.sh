#!/usr/bin/env bash
# ==============================================================================
# setup_subagents.sh
# Bootstraps Production-Grade Sub-Agent rules & task templates for Android.
# Model Tiers: 3.5-flash-lite & 3.6-flash
# ==============================================================================

set -e

echo "🚀 Setting up Production Android AI Sub-Agent rules and task templates..."

# 1. Create directory structure
mkdir -p .gemini/rules
mkdir -p .agent/tasks

# 2. Create .gemini/rules/subagent_delegation_rules.md
cat << 'EOF' > .gemini/rules/subagent_delegation_rules.md
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
EOF

# Copy rules to .agent/rules.md
cp .gemini/rules/subagent_delegation_rules.md .agent/rules.md

# 3. Create .agent/tasks/fast_search_indexer.md
cat << 'EOF' > .agent/tasks/fast_search_indexer.md
# Task: Fast Search & Codebase Indexer Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.5-flash-lite` (Ultra-fast, minimal token cost)
- **Role:** Codebase Indexer & File Grepper
- **Workspace:** `inherit`

## Execution Instructions
1. Search `app/src` for target file patterns, string resources, or data models.
2. Extract method signatures, class hierarchies, or dependency lists.
3. DO NOT output entire source files into the main context.
4. Return a structured list or table of file paths and matching lines.
EOF

# 4. Create .agent/tasks/build_diagnostics.md
cat << 'EOF' > .agent/tasks/build_diagnostics.md
# Task: Build & Test Diagnostics Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.5-flash-lite` (Ultra-fast, lowest token cost)
- **Role:** Build Debugger
- **Workspace:** `inherit`

## Execution Instructions
1. Run `./gradlew test` or `./gradlew assembleDebug` in the background.
2. Read the terminal execution output and log files.
3. **DO NOT** output the entire build log to the main context.
4. Extract only:
   - The failing file path and line number.
   - The exact exception / error message.
   - The minimal proposed code fix.

## Output Specification
Return a clean summary in this format:
- **Status:** PASSED / FAILED
- **Error Location:** `[FileName.kt:LineNumber](file:///path/to/file#L12)`
- **Root Cause:** Brief 1-2 sentence explanation.
- **Suggested Fix:** Minimal code snippet.
EOF

# 5. Create .agent/tasks/android_lint_checker.md
cat << 'EOF' > .agent/tasks/android_lint_checker.md
# Task: Android Lint & Formatting Checker Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.5-flash-lite` (Ultra-fast, rule-based)
- **Role:** Code Formatter & Static Analyzer
- **Workspace:** `inherit`

## Execution Instructions
1. Run `./gradlew lint` or `./gradlew detekt` / `./gradlew spotlessCheck` in the background.
2. Read the resulting XML/HTML lint reports internally.
3. Extract only high-severity warnings, deprecated API calls, or formatting errors.
4. Return a bulleted list of failing files and recommended auto-fixes.
EOF

# 6. Create .agent/tasks/codebase_audit.md
cat << 'EOF' > .agent/tasks/codebase_audit.md
# Task: Architecture & Code Quality Audit Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.6-flash` (Balanced speed & reasoning)
- **Role:** Codebase Auditor
- **Workspace:** `inherit`

## Execution Instructions
1. Search the `app/` directory for ViewModel classes, repository layers, and data models.
2. Check for:
   - Missing null-safety handles or unsafe `!!` operators.
   - Hardcoded strings or raw constants in UI classes.
   - Missing unit tests for critical business logic.
3. Keep all intermediate search results internal to your sub-agent context.

## Output Specification
Provide a concise Markdown table:
| File | Issue | Severity | Proposed Action |
EOF

# 7. Create .agent/tasks/unit_test_generator.md
cat << 'EOF' > .agent/tasks/unit_test_generator.md
# Task: Isolated Unit Test Generator

## Sub-Agent Configuration
- **Model Tier:** `3.6-flash` (Balanced speed & reasoning)
- **Role:** Test Engineer
- **Workspace:** `branch` (isolated sandbox)

## Execution Instructions
1. Identify untested Kotlin classes in `app/src/main/java`.
2. Generate corresponding unit test files under `app/src/test/java`.
3. Execute `./gradlew test` inside the branch workspace to ensure all generated tests pass.
4. Return a diff summary once all tests pass cleanly.

## Output Specification
- **Tests Added:** List of new test files created.
- **Pass Rate:** 100% verified.
- **Branch Ready for Merge:** Yes/No.
EOF

# 8. Create .agent/tasks/e2e_testing.md
cat << 'EOF' > .agent/tasks/e2e_testing.md
# Task: End-to-End (E2E) UI Testing Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.6-flash` (Balanced speed & reasoning)
- **Role:** E2E Automation Engineer
- **Workspace:** `branch` (isolated sandbox workspace)

## Execution Instructions
1. Identify primary user flows (e.g., Form Part A input -> Form Part B input -> Claim Summary & Submission).
2. Generate Compose UI instrumentation tests (`ComposeTestRule` / Espresso tests) under `app/src/androidTest/java/`.
3. Execute `./gradlew connectedCheck` or `./gradlew testDebugUnitTest` to verify end-to-end user journeys.
4. **DO NOT** output raw instrumentation logs into the main context.
5. Capture test results, assertion status, and error stack traces internally.

## Output Specification
Provide a concise E2E summary:
- **User Flows Tested:** List of completed end-to-end user journeys.
- **Overall Status:** PASSED / FAILED.
- **Failed Assertions:** (If failed) Failing composable or screen assertion + exact line link `[ScreenTest.kt:L45](file:///path/to/file#L45)`.
- **Branch Ready for Merge:** Yes/No.
EOF

# 9. Create .agent/tasks/perf_memory_debugger.md
cat << 'EOF' > .agent/tasks/perf_memory_debugger.md
# Task: Performance & Memory Leak Debugger Sub-Agent

## Sub-Agent Configuration
- **Model Tier:** `3.6-flash` (Balanced speed & reasoning)
- **Role:** Memory & Performance Diagnostic Engineer
- **Workspace:** `inherit`

## Execution Instructions
1. Inspect Heap Dumps (`.hprof`), LeakCanary trace logs, or Android Vitals rendering metrics.
2. Analyze Compose `remember` state retention, un-cancelled coroutine jobs, or static Activity context references.
3. Identify memory leaks, retained objects, or junk re-compositions causing dropped frames (jank).
4. Return a root-cause diagnosis report with the exact leaking reference tree and resolution fix.
EOF

chmod +x setup_subagents.sh

echo "✅ Production sub-agent setup complete!"
echo "📁 Configured Model Tiers:"
echo "   - 3.5-flash-lite: Fast Search, Build Diagnostics, Android Lint"
echo "   - 3.6-flash: Architecture Audit, Unit Test Generator, E2E UI Testing, Performance & Memory Debugger"
