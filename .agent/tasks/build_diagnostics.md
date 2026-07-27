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
