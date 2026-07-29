# Sub-Agent Delegation Rules (Android, Production-Grade)

## Core Mandate
When building or maintaining this Android app, the main agent **MUST** delegate
noisy, high-token, or specialized work to the custom sub-agents in
`.agents/agents/`. Doing this work inline blows the main context window and
defeats the entire purpose of this setup. Invoke sub-agents via the
`invoke_subagent` tool; the planner routes based on each agent's `description`.

## Delegation Triggers

| Signal in the main agent's next step | Delegate to | Workspace |
| --- | --- | --- |
| Any multi-file grep, symbol lookup, dependency scan, or string-resource search | `fast-search-indexer` | `inherit` |
| Any `./gradlew` build / assemble / test invocation | `build-diagnostics` | `inherit` |
| Any `./gradlew lint`, `detekt`, or `spotless` invocation | `android-lint-checker` | `inherit` |
| Any Clean Architecture / Compose state / ViewModel / Hilt / Room audit across files | `codebase-auditor` | `inherit` |
| Generating new `*Test.kt` unit tests (JUnit5, MockK, Turbine) | `unit-test-generator` | `branch` |
| Writing or running `ComposeTestRule` / Espresso instrumentation tests | `e2e-tester` | `branch` |
| Diagnosing `.hprof` heap dumps, LeakCanary traces, or Compose jank | `perf-memory-debugger` | `inherit` |

**Rule:** if a step matches a signal above, do not perform it inline — delegate.
Isolated code-writing agents (`unit-test-generator`, `e2e-tester`) must be
invoked in `Workspace: branch` so generated code lands in a git worktree.

## What the Main Agent Still Owns
The sub-agents are assistants, not an orchestrator. The main agent remains
responsible for:

- Reading the source spec (e.g. the PDF the user names in their prompt, such
  as `claim_form.pdf`) and extracting the screen inventory.
- Scaffolding the Gradle / Jetpack Compose project via `android-cli`.
- Authoring screens, ViewModels, navigation, and repositories.
- Driving high-level architecture decisions (MVVM/MVI, DI graph shape,
  persistence strategy).
- Sequencing sub-agent invocations to keep the delivery on track.

## Anti-Patterns (do not do)
- Running `./gradlew` yourself and pasting the log into context.
- Grepping the source tree in the main loop when `fast-search-indexer` exists.
- Writing `*Test.kt` files in the main workspace instead of a branch worktree.
- Emitting raw lint XML or heap-dump traces to the user.
