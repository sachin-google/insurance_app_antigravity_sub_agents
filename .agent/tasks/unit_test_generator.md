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
