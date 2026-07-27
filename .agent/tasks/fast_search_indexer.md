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
