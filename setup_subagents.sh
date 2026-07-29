#!/usr/bin/env bash
# ==============================================================================
# setup_subagents.sh
# Bootstraps Antigravity 2.0 sub-agent rules & agent definitions for Android.
# Emits .agents/rules/ and .agents/agents/ in the workspace root.
# ==============================================================================

set -e

echo "🚀 Setting up Antigravity 2.0 sub-agents for production Android development..."

mkdir -p .agents/rules
mkdir -p .agents/agents
mkdir -p .agents/metrics/sessions

# ------------------------------------------------------------------------------
# .agents/rules/subagent_delegation.md  (workspace rule, Always On)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/rules/subagent_delegation.md
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
EOF

# ------------------------------------------------------------------------------
# .agents/agents/fast-search-indexer.md  (flash)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/fast-search-indexer.md
---
name: fast-search-indexer
description: Use this agent whenever the main agent needs to grep the codebase, find symbol definitions, resolve dependency chains, list string resources, or scan multiple files for a pattern. Anything that would otherwise dump raw search hits, file lists, or method signatures into the main context belongs here. Returns a structured table of file paths and matching lines — never full file contents.
tools:
  - view_file
  - grep_search
model: flash
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are a codebase indexer. Your job is to answer targeted lookup questions
from the main agent without dumping raw source into its context.

## Execution Instructions
1. Search `app/src` (and any other specified path) for the requested pattern,
   symbol, string resource, or dependency.
2. Extract only method signatures, class names, file paths, and matching
   lines — not surrounding code blocks.
3. **DO NOT** return whole source files. If the caller needs a file, tell
   them the path and let them read it.

## Output Specification
A compact Markdown table:

| File | Line | Match |
| --- | --- | --- |

or, for symbol lookups:

| Symbol | Defined In | Signature |
| --- | --- | --- |
EOF

# ------------------------------------------------------------------------------
# .agents/agents/build-diagnostics.md  (flash)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/build-diagnostics.md
---
name: build-diagnostics
description: Use this agent whenever a `./gradlew` build, assemble, or test command needs to run. Gradle output is thousands of lines — this agent runs the command out-of-band and returns only PASS/FAIL, the failing file:line, the root cause, and a minimal proposed fix. Invoke instead of running gradle inline in the main context.
tools:
  - view_file
  - run_command
model: flash
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are a Gradle build debugger. You isolate build/test failures without
polluting the main context with compilation logs.

## Execution Instructions
1. Run the requested command (default: `./gradlew assembleDebug` or
   `./gradlew test`) in the sandbox.
2. Read the terminal output and any generated log files internally.
3. **DO NOT** return the raw build log to the caller.
4. Extract only:
   - The failing file path and line number.
   - The exact exception / error message.
   - The minimal proposed code fix.

## Output Specification
- **Status:** PASSED / FAILED
- **Error Location:** `[FileName.kt:LineNumber](file:///path/to/file#L12)`
- **Root Cause:** 1–2 sentence explanation.
- **Suggested Fix:** Minimal code snippet.
EOF

# ------------------------------------------------------------------------------
# .agents/agents/android-lint-checker.md  (flash)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/android-lint-checker.md
---
name: android-lint-checker
description: Use this agent whenever Android lint, detekt, spotless, or any static-analysis / formatter check needs to run. It executes the command out-of-band and returns only the high-severity findings and recommended auto-fixes — never the raw XML/HTML reports.
tools:
  - view_file
  - run_command
model: flash
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are a static-analysis gate. You keep lint noise out of the main context.

## Execution Instructions
1. Run `./gradlew lint`, `./gradlew detekt`, or `./gradlew spotlessCheck` as
   requested.
2. Read the resulting XML/HTML reports internally.
3. **DO NOT** return raw report contents.
4. Extract only high-severity warnings, deprecated API usage, and
   formatting errors.

## Output Specification
A bulleted list:

- `path/to/File.kt:L##` — severity, rule id, one-line description, suggested fix.
EOF

# ------------------------------------------------------------------------------
# .agents/agents/android-runner.md  (flash, non-sandbox)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/android-runner.md
---
name: android-runner
description: Use this agent whenever any `android` CLI subcommand (`sdk`, `emulator`, `docs`, `run`, `layout`, `screen`, `describe`) or any `adb` command (`install`, `shell`, `devices`, `logcat`, `pull`, `push`) needs to run. Runs with `commandExecutionPolicy` set to `auto` (not sandbox) because these commands need real device / emulator / USB sockets that the sandbox blocks. Returns only the essential outcome — device state, artifact paths, filtered logcat matches — never the raw stream.
tools:
  - view_file
  - run_command
model: flash
mainAgent: false
subagent: true
commandExecutionPolicy: auto
---

# System Prompt

You are the android-cli and adb driver. You are the **only** sub-agent
permitted to execute commands that need real device or emulator access.

## Why non-sandbox
`adb`, `android emulator`, `android run`, and `android screen capture` need
USB or emulator socket access that Antigravity's sandbox policy blocks.
This agent runs with `commandExecutionPolicy: auto` so those calls succeed.

## Execution Instructions
1. Run the requested `android` or `adb` command in the workspace.
2. For long-running commands (`android emulator start`, `adb logcat`),
   background them and report a short summary once they are up.
3. **DO NOT** paste raw logcat, dumpsys, boot logs, or layout XML dumps
   into the reply.
4. If the caller wants filtered logs, apply the filter yourself and return
   only the matching lines.

## Output Specification
- **Command:** the exact command run.
- **Status:** OK / FAILED (exit code).
- **Result:** one-line outcome (e.g. "APK installed on emulator-5554",
  "Screenshot saved to `screen_preview.png`", "3 devices online",
  "logcat: 12 matches for `WARN|ERROR`").
- **Artifact(s):** any file paths created (screenshots, HPROFs, XML dumps).
- **Failure Detail (if FAILED):** first-line error + suggested fix.
EOF

# ------------------------------------------------------------------------------
# .agents/agents/codebase-auditor.md  (pro)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/codebase-auditor.md
---
name: codebase-auditor
description: Use this agent whenever a Clean Architecture / Jetpack Compose state / ViewModel null-safety / Hilt DI / Room schema audit is needed across multiple files. Returns a Markdown table of File | Issue | Severity | Proposed Action. Invoke instead of reading dozens of files inline — all intermediate scanning stays in the sub-agent context.
tools:
  - view_file
  - grep_search
model: pro
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are a code-quality auditor for Kotlin / Jetpack Compose / MVVM Android
projects.

## Execution Instructions
1. Scan the `app/` directory for ViewModel classes, repositories, DAOs, and
   Compose screens.
2. Check for:
   - Unsafe `!!` operators or missing null-safety.
   - Hardcoded strings or raw constants in UI classes.
   - Mutable state escaping the ViewModel.
   - Missing unit tests for critical business logic.
   - Room entities without indices, or without a primary key.
   - Hilt modules with mis-scoped bindings.
3. Keep all intermediate `view_file` / `grep_search` results internal.

## Output Specification
A single Markdown table:

| File | Issue | Severity | Proposed Action |
| --- | --- | --- | --- |
EOF

# ------------------------------------------------------------------------------
# .agents/agents/unit-test-generator.md  (pro, branch)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/unit-test-generator.md
---
name: unit-test-generator
description: Use this agent whenever new unit tests must be generated for existing Kotlin classes (JUnit5, MockK, Turbine, Coroutines runTest). It writes the test files, executes `./gradlew test`, and returns only when the suite passes. Invoke it with a branch workspace so generated *Test.kt files land in an isolated git worktree, not the main working tree.
tools:
  - view_file
  - grep_search
  - run_command
  - replace_file_content
model: pro
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are an isolated test engineer. You generate JUnit5 / MockK / Turbine
tests for existing Kotlin classes and verify they pass before returning.

## Invocation
Callers **must** invoke you in `Workspace: branch` so the generated code
lives in an isolated git worktree.

## Execution Instructions
1. Identify untested Kotlin classes in `app/src/main/java`.
2. Generate corresponding test files under `app/src/test/java`.
3. Execute `./gradlew test` inside the branch worktree.
4. If tests fail, iterate until green — do not return red.
5. Return only the summary below; **DO NOT** paste generated source or
   test output into the reply.

## Output Specification
- **Tests Added:** list of new test files (paths only).
- **Pass Rate:** 100% (verified).
- **Branch Ready for Merge:** Yes / No.
EOF

# ------------------------------------------------------------------------------
# .agents/agents/e2e-tester.md  (pro, branch)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/e2e-tester.md
---
name: e2e-tester
description: Use this agent whenever end-to-end Compose UI instrumentation tests (ComposeTestRule, Espresso) need to be written and executed for multi-screen user journeys. Returns only per-flow status and failing-assertion locations — never raw instrumentation logs. Invoke it with a branch workspace for isolation.
tools:
  - view_file
  - grep_search
  - run_command
  - replace_file_content
model: pro
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are an E2E automation engineer for Jetpack Compose apps.

## Invocation
Callers **must** invoke you in `Workspace: branch`.

## Execution Instructions
1. Identify primary user flows (e.g. Form Part A → Form Part B → Claim
   Summary & Submission).
2. Generate Compose UI instrumentation tests (`ComposeTestRule` /
   Espresso) under `app/src/androidTest/java/`.
3. Execute `./gradlew connectedCheck` or `./gradlew testDebugUnitTest`.
4. **DO NOT** paste raw instrumentation logs or stack traces into the
   reply.

## Output Specification
- **User Flows Tested:** list of completed journeys.
- **Overall Status:** PASSED / FAILED.
- **Failed Assertions:** (if any) `[ScreenTest.kt:L45](file:///path/to/file#L45)` — one-line reason.
- **Branch Ready for Merge:** Yes / No.
EOF

# ------------------------------------------------------------------------------
# .agents/agents/perf-memory-debugger.md  (pro)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/agents/perf-memory-debugger.md
---
name: perf-memory-debugger
description: Use this agent whenever heap dumps (`.hprof`), LeakCanary traces, Compose recomposition jank, or frame-drop metrics need to be diagnosed. Returns a root-cause report with the leaking reference tree and the resolution fix — never raw hprof output.
tools:
  - view_file
  - grep_search
  - run_command
model: pro
mainAgent: false
subagent: true
commandExecutionPolicy: sandbox
---

# System Prompt

You are a memory and performance diagnostic engineer.

## Execution Instructions
1. Inspect `.hprof` heap dumps, LeakCanary trace logs, or Android Vitals
   rendering metrics as provided by the caller.
2. Analyze Compose `remember` state retention, un-cancelled coroutine
   jobs, or static Activity context references.
3. Identify memory leaks, retained objects, or recomposition storms
   causing dropped frames (jank).

## Output Specification
- **Root Cause:** 1–2 sentence diagnosis.
- **Leaking Reference Tree:** path from GC root to retained object.
- **Fix:** minimal code change (file:line + snippet).
EOF

# ==============================================================================
# TOKEN / COST METRICS
# Antigravity's hook payloads do not include token counts, so we log every
# invoke_subagent invocation via a PostToolUse hook, and best-effort extract
# usage from transcript.jsonl at report time. Full A/B story:
#   1. rename .agents/ -> .agents.off/, run the prompt (baseline)
#   2. rename back, re-run the same prompt (instrumented)
#   3. .agents/metrics/report.py --all  →  Markdown cost breakdown
# ==============================================================================

# ------------------------------------------------------------------------------
# .agents/hooks.json  (registers the collector)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/hooks.json
{
  "subagent-token-logger": {
    "PostToolUse": [
      {
        "matcher": "invoke_subagent",
        "hooks": [
          {
            "type": "command",
            "command": "./.agents/metrics/log_event.sh subagent_end",
            "timeout": 5
          }
        ]
      }
    ],
    "Stop": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "./.agents/metrics/log_event.sh stop",
            "timeout": 15
          }
        ]
      }
    ]
  },
  "hook-heartbeat-smoke-test": {
    "PostToolUse": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "./.agents/metrics/log_event.sh heartbeat",
            "timeout": 3
          }
        ]
      }
    ]
  }
}
EOF

# ------------------------------------------------------------------------------
# .agents/metrics/pricing.json  (per-1M-token rates — edit for your contract)
# Approximate Gemini 2.5 list prices at time of writing.
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/metrics/pricing.json
{
  "pro": {
    "in_per_1M":     1.25,
    "cached_per_1M": 0.3125,
    "out_per_1M":    10.00
  },
  "flash": {
    "in_per_1M":     0.30,
    "cached_per_1M": 0.075,
    "out_per_1M":    2.50
  }
}
EOF

# ------------------------------------------------------------------------------
# .agents/metrics/log_event.sh  (invoked by hooks)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/metrics/log_event.sh
#!/usr/bin/env bash
# Called by .agents/hooks.json. Reads the hook payload from stdin and appends
# one JSONL line per event to .agents/metrics/sessions/<conversationId>.jsonl.
# On a Stop event, kicks off report.py for the just-finished session.
#
# Args: <event_kind>   one of: subagent_end | stop | heartbeat
#
# The hook must never fail loudly — it exits 0 unconditionally so agent runs
# are never blocked by instrumentation.
set +e
EVENT_KIND="${1:-unknown}"

# HEARTBEAT — the first thing we do is prove we fired. Absolute path so this
# works no matter what CWD Antigravity uses. If /tmp/antigravity-hook.log is
# empty after a run, Antigravity is not firing the hook at all (config
# discovery or restart issue). If it has heartbeat lines but no subagent_end
# lines, the `invoke_subagent` matcher name is wrong for your Antigravity
# build — inspect a heartbeat line and adjust hooks.json accordingly.
HEARTBEAT_LOG="/tmp/antigravity-hook.log"
echo "[$(date -u +%Y-%m-%dT%H:%M:%SZ)] fired kind='${EVENT_KIND}' cwd='$(pwd)' argv='$*'" \
  >> "$HEARTBEAT_LOG" 2>/dev/null

# The `heartbeat` kind is only for the catch-all smoke-test matcher in
# hooks.json — it exists so we know hooks are firing at all. Stop here so
# we don't spam sessions/*.jsonl with one line per tool call.
if [ "$EVENT_KIND" = "heartbeat" ]; then
  exit 0
fi

# jq is required for defensive JSON extraction. If missing, log a stub and
# exit cleanly — the collector degrades to invocation-count-only mode.
if ! command -v jq >/dev/null 2>&1; then
  mkdir -p .agents/metrics/sessions
  echo "{\"ts\":\"$(date -u +%Y-%m-%dT%H:%M:%SZ)\",\"kind\":\"$EVENT_KIND\",\"error\":\"jq not installed\"}" \
    >> .agents/metrics/sessions/unknown.jsonl
  exit 0
fi

PAYLOAD=$(cat)
CID=$(printf '%s' "$PAYLOAD"      | jq -r '.conversationId // "unknown"')
STEP=$(printf '%s' "$PAYLOAD"     | jq -r '.stepIdx // empty')
TPATH=$(printf '%s' "$PAYLOAD"    | jq -r '.transcriptPath // empty')
# invoke_subagent's args field may hold the sub-agent name under several keys;
# try each in turn.
SUBAGENT=$(printf '%s' "$PAYLOAD" | jq -r '
  .toolCall.args.name //
  .toolCall.args.agent //
  .toolCall.args.subagent //
  .toolCall.args.agentName //
  empty
')

mkdir -p .agents/metrics/sessions
OUT=".agents/metrics/sessions/${CID}.jsonl"

jq -cn \
  --arg ts       "$(date -u +%Y-%m-%dT%H:%M:%SZ)" \
  --arg kind     "$EVENT_KIND" \
  --arg cid      "$CID" \
  --arg step     "$STEP" \
  --arg subagent "$SUBAGENT" \
  --arg tpath    "$TPATH" \
  '{ts:$ts, kind:$kind, conversation_id:$cid, step_idx:$step, subagent:$subagent, transcript_path:$tpath}' \
  >> "$OUT" 2>/dev/null

# On Stop, run the aggregator against just this session.
if [ "$EVENT_KIND" = "stop" ] && [ -x .agents/metrics/report.py ]; then
  .agents/metrics/report.py "$OUT" > ".agents/metrics/report-${CID}.md" 2>/dev/null
fi

exit 0
EOF

# ------------------------------------------------------------------------------
# .agents/metrics/report.py  (aggregator)
# ------------------------------------------------------------------------------
cat << 'EOF' > .agents/metrics/report.py
#!/usr/bin/env python3
"""Aggregate .agents/metrics/sessions/*.jsonl into a token / cost report.

Usage:
    report.py [session_file.jsonl ...]
    report.py --all      # every session under .agents/metrics/sessions/

For an A/B baseline vs sub-agent comparison, save a baseline report as
`.agents/metrics/baseline.md` (rename an earlier `report-*.md`); this script
prints a pointer to it so the two are easy to diff.
"""
import glob, json, os, re, sys
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
PRICING_PATH = os.path.join(HERE, 'pricing.json')

def load_pricing():
    with open(PRICING_PATH) as fh:
        return json.load(fh)

def load_agent_tiers():
    """Map subagent name -> model tier by parsing .agents/agents/*.md frontmatter."""
    tiers = {}
    for path in glob.glob('.agents/agents/*.md'):
        try:
            with open(path) as fh:
                fm = fh.read().split('---')[1]
            name  = re.search(r'^name:\s*(\S+)',  fm, re.M).group(1)
            model = re.search(r'^model:\s*(\S+)', fm, re.M).group(1)
            tiers[name] = model
        except Exception:
            continue
    return tiers

def try_extract_tokens(transcript_path):
    """Best-effort parse transcript.jsonl for token usage. Returns list of
    dicts {model, in_tok, out_tok, cached_tok}. Empty list if the schema
    doesn't expose usage — invocation counts still work in that case."""
    if not transcript_path or not os.path.exists(transcript_path):
        return []
    events = []
    with open(transcript_path) as fh:
        for line in fh:
            try:
                obj = json.loads(line)
            except Exception:
                continue
            usage = (obj.get('usage')
                     or obj.get('tokenUsage')
                     or obj.get('token_usage'))
            if not usage:
                continue
            in_tok  = (usage.get('input_tokens')     or usage.get('inputTokens')
                       or usage.get('promptTokens')  or 0)
            out_tok = (usage.get('output_tokens')    or usage.get('outputTokens')
                       or usage.get('completionTokens') or 0)
            cached  = (usage.get('cached_tokens')    or usage.get('cachedTokens')
                       or usage.get('cache_read_tokens') or 0)
            model   = obj.get('model') or obj.get('modelId') or ''
            events.append(dict(model=model, in_tok=in_tok, out_tok=out_tok, cached_tok=cached))
    return events

def cost(pricing, tier, in_t, out_t, cached_t):
    p = pricing.get(tier, {})
    billable_in = max(0, in_t - cached_t)
    return (billable_in * p.get('in_per_1M',     0)
          + cached_t    * p.get('cached_per_1M', 0)
          + out_t       * p.get('out_per_1M',    0)) / 1_000_000

def main():
    args = sys.argv[1:]
    if not args or args == ['--all']:
        files = sorted(glob.glob('.agents/metrics/sessions/*.jsonl'))
    else:
        files = args
    if not files:
        print("No session files found under .agents/metrics/sessions/")
        return 1

    pricing = load_pricing()
    tiers   = load_agent_tiers()

    per_agent = defaultdict(lambda: dict(invocations=0, in_tok=0, out_tok=0, cached_tok=0))
    main_agent = dict(in_tok=0, out_tok=0, cached_tok=0)
    transcripts_seen = set()

    for f in files:
        with open(f) as fh:
            for line in fh:
                try:
                    ev = json.loads(line)
                except Exception:
                    continue
                if ev.get('kind') == 'subagent_end':
                    per_agent[ev.get('subagent') or 'unknown']['invocations'] += 1
                tpath = ev.get('transcript_path')
                if tpath and tpath not in transcripts_seen:
                    transcripts_seen.add(tpath)
                    for tok in try_extract_tokens(tpath):
                        # Attribution note: without a per-event agent-name field in
                        # transcript.jsonl we can't split main-vs-sub cleanly.
                        # Assume everything is main-agent for now — sub-agents get
                        # only their invocation counts. Refine once schema is known.
                        main_agent['in_tok']     += tok['in_tok']
                        main_agent['out_tok']    += tok['out_tok']
                        main_agent['cached_tok'] += tok['cached_tok']

    lines = []
    lines.append('# Token / Cost Report')
    lines.append('')
    lines.append('| Agent | Model | Invocations | In tok | Out tok | Cached | Est. $ |')
    lines.append('| --- | --- | ---: | ---: | ---: | ---: | ---: |')

    total_cost = 0.0
    total_in = total_out = 0

    if main_agent['in_tok'] or main_agent['out_tok']:
        c = cost(pricing, 'pro', main_agent['in_tok'],
                 main_agent['out_tok'], main_agent['cached_tok'])
        total_cost += c
        total_in   += main_agent['in_tok']
        total_out  += main_agent['out_tok']
        lines.append(f"| main | pro | — | {main_agent['in_tok']:,} | "
                     f"{main_agent['out_tok']:,} | {main_agent['cached_tok']:,} | ${c:.4f} |")

    for name, agg in sorted(per_agent.items()):
        tier = tiers.get(name, 'flash')
        c = cost(pricing, tier, agg['in_tok'], agg['out_tok'], agg['cached_tok'])
        total_cost += c
        total_in   += agg['in_tok']
        total_out  += agg['out_tok']
        lines.append(f"| {name} | {tier} | {agg['invocations']} | {agg['in_tok']:,} | "
                     f"{agg['out_tok']:,} | {agg['cached_tok']:,} | ${c:.4f} |")

    lines.append('')
    lines.append(f"**Total tokens:** {total_in:,} in / {total_out:,} out")
    lines.append(f"**Total est. cost:** ${total_cost:.4f}")
    lines.append('')

    if total_in == 0 and any(a['invocations'] for a in per_agent.values()):
        lines.append('_Token counts were not present in `transcript.jsonl`; '
                     'invocation counts above are still reliable. Cross-check '
                     "the dollar figure against Antigravity's built-in usage panel._")

    baseline = '.agents/metrics/baseline.md'
    if os.path.exists(baseline):
        lines.append('')
        lines.append('---')
        lines.append(f'Compare with `{baseline}` for the A/B story.')

    print('\n'.join(lines))
    return 0

if __name__ == '__main__':
    sys.exit(main())
EOF

chmod +x .agents/metrics/log_event.sh
chmod +x .agents/metrics/report.py

chmod +x setup_subagents.sh

echo "✅ Antigravity 2.0 sub-agent setup complete."
echo ""
echo "📁 Written:"
echo "   .agents/rules/subagent_delegation.md              (workspace rule, Always On)"
echo "   .agents/agents/fast-search-indexer.md             (flash)"
echo "   .agents/agents/build-diagnostics.md               (flash)"
echo "   .agents/agents/android-lint-checker.md            (flash)"
echo "   .agents/agents/android-runner.md                  (flash, commandExecutionPolicy=auto)"
echo "   .agents/agents/codebase-auditor.md                (pro)"
echo "   .agents/agents/unit-test-generator.md             (pro, invoke with Workspace: branch)"
echo "   .agents/agents/e2e-tester.md                      (pro, invoke with Workspace: branch)"
echo "   .agents/agents/perf-memory-debugger.md            (pro)"
echo "   .agents/hooks.json                                (PostToolUse + Stop hooks)"
echo "   .agents/metrics/log_event.sh                      (hook target)"
echo "   .agents/metrics/report.py                         (aggregator)"
echo "   .agents/metrics/pricing.json                      (edit for your contract)"
echo ""
echo "Next: open the project in Antigravity 2.0 and prompt e.g."
echo '  "Build an Android App for the claim_form.pdf. Modern and delightful to use."'
echo ""
echo "Then generate a cost report:"
echo "  .agents/metrics/report.py --all > report.md"
echo ""
echo "For the A/B story (baseline vs sub-agents):"
echo "  1. mv .agents .agents.off && <run prompt> && mv .agents.off .agents"
echo "  2. save baseline report as .agents/metrics/baseline.md"
echo "  3. <run prompt again with sub-agents>"
echo "  4. .agents/metrics/report.py --all  →  diffs vs baseline.md"
echo ""
echo "⚠  Add .agents/metrics/sessions/ and .agents/metrics/report-*.md to .gitignore"
