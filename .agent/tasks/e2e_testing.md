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
