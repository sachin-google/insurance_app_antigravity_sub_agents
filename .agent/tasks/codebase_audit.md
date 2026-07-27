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
