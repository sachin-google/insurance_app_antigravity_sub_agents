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
