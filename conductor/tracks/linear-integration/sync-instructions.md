# Linear Integration: Sync Instructions

This document defines the procedures for synchronizing the repository state with Linear. These instructions are intended to be executed by an AI agent with access to the `linear` MCP tools.

## Procedure 1: Repo -> Linear (The "Push" Sync)

**Goal:** Ensure every task in `plan.md` has a corresponding Linear Issue and that their statuses match.

**Steps:**
1.  **Scan for Unlinked Tasks:**
    -   Read `conductor/tracks/backlog/plan.md` (and other active tracks).
    -   Identify any task line (starting with `- [ ]`, `- [~]`, or `- [x]`) that *missing* the `<!-- linear-issue: ID -->` comment.
    -   **Action:** For each unlinked task:
        -   Use `linear.create_issue` to create a new issue in Team `TVELO`.
        -   Title: The text of the task.
        -   Description: "Imported from Conductor Backlog."
        -   **Crucial:** Append `<!-- linear-issue: NEW_ID -->` to the task line in the file.

2.  **Sync Statuses:**
    -   Identify tasks *with* an ID.
    -   Check the status in the file:
        -   `[ ]` = "Todo" (or "Backlog")
        -   `[~]` = "In Progress"
        -   `[x]` = "Done"
    -   **Action:** detailed check of the Linear issue's status. If they differ, update the Linear issue to match the repo (Repo is Source of Truth).

## Procedure 2: Linear -> Repo (The "Pull" Sync)

**Goal:** Ensure new issues created in Linear appear in the repository.

**Steps:**
1.  **Fetch New Issues:**
    -   Use `linear.list_issues` (or search) to find issues in Team `TVELO` that are *not* present in any `plan.md` file.
    -   *Note: This requires the agent to maintain a mental or temporary list of all IDs currently in the repo.*

2.  **Append to Backlog:**
    -   **Action:** For each new external issue:
        -   Append a new task line to `conductor/tracks/backlog/plan.md`.
        -   Format: `- [ ] **Task:** Title of the issue <!-- linear-issue: ID -->`

## Execution
Run these procedures sequentially. Always start with "Push" to stabilize the repo's current state before pulling in new work.
