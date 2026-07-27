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
