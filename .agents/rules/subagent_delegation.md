# Sub-Agent Delegation Rules (Android, Production-Grade)

## Core Mandate
When building or maintaining this Android app, the main agent **MUST** delegate
noisy, high-token, or specialized work to the custom sub-agents in
`.agents/agents/`. Doing this work inline blows the main context window and
defeats the entire purpose of this setup. Invoke sub-agents via the
`invoke_subagent` tool; the planner routes based on each agent's `description`.

## Activation
This rule is intended to be **Always On** — the main agent applies it on
every turn. Files dropped into `.agents/rules/` do not always default to
Always On; the first time you open this project in Antigravity 2.0, open
the Customizations panel and confirm this rule's activation mode is set to
**Always On** (not Manual, Glob, or Model Decision).

## Delegation Triggers

| Signal in the main agent's next step | Delegate to | Workspace |
| --- | --- | --- |
| Any multi-file grep, symbol lookup, dependency scan, or string-resource search | `fast-search-indexer` | `inherit` |
| Any `./gradlew` build / assemble / test invocation | `build-diagnostics` | `inherit` |
| Any `./gradlew lint`, `detekt`, or `spotless` invocation | `android-lint-checker` | `inherit` |
| Any `android` CLI subcommand (`create`, `sdk`, `emulator`, `run`, `layout`, `screen`, `describe`) or any `adb` command — **no exceptions** | `android-runner` | `inherit` |
| Any Clean Architecture / Compose state / ViewModel / Hilt / Room audit across files | `codebase-auditor` | `inherit` |
| Generating new `*Test.kt` unit tests (JUnit5, MockK, Turbine) | `unit-test-generator` | `branch` |
| Writing or running `ComposeTestRule` / Espresso instrumentation tests | `e2e-tester` | `branch` |
| Diagnosing `.hprof` heap dumps, LeakCanary traces, or Compose jank | `perf-memory-debugger` | `inherit` |

**Rule:** if a step matches a signal above, do not perform it inline — delegate.
Isolated code-writing agents (`unit-test-generator`, `e2e-tester`) must be
invoked in `Workspace: branch` so generated code lands in a git worktree.

**Context transfer for `android-runner`:** sub-agents start with a clean
context window. When invoking `android-runner` for `android create`, pass
the project name, output path, and min-SDK explicitly in the invoke prompt
— the sub-agent will not see the main conversation.

## What the Main Agent Still Owns
The sub-agents are assistants, not an orchestrator. The main agent remains
responsible for:

- Reading the source spec (e.g. the PDF the user attaches to their prompt,
  such as `claim_form.pdf`) and extracting the screen inventory.
- Authoring screens, ViewModels, navigation, and repositories.
- Driving high-level architecture decisions (MVVM/MVI, DI graph shape,
  persistence strategy).
- Sequencing sub-agent invocations to keep the delivery on track.
- **Visually verifying screenshots.** After `android-runner` returns a path
  from `android screen capture`, `view_file` the PNG and iterate if the UI
  does not match the intended screen. Do not treat a green build as UI
  verification.

## Anti-Patterns (do not do)
- Running `./gradlew` yourself and pasting the log into context.
- Running `adb` or `android emulator/run/screen` in the main agent's sandbox
  — those commands need real device / USB / emulator sockets that the
  sandbox blocks; hand them to `android-runner` instead.
- Grepping the source tree in the main loop when `fast-search-indexer` exists.
- Writing `*Test.kt` files in the main workspace instead of a branch worktree.
- Emitting raw lint XML, logcat dumps, or heap-dump traces to the user.
