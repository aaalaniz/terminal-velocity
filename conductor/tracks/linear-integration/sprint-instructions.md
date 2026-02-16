# Linear Integration: Weekly Sprint Instructions

This document defines the procedures for managing weekly sprints. These instructions are intended to be executed by an AI agent (Jules) on a weekly schedule (e.g., Monday 8:00 AM).

## Procedure: Start New Sprint

**Goal:** Create a consolidated view of the week's work based on the active Linear Cycle.

**Steps:**
1.  **Determine Current Cycle:**
    -   Use `linear.list_cycles` to find the "Active" cycle for team `TVELO`.
    -   Extract the Cycle Number (e.g., `Cycle 12`) and the Week (e.g., `2026-W08`).

2.  **Create Sprint File:**
    -   Create a new file: `conductor/sprints/<YYYY-WXX>.md` (e.g., `2026-W08.md`).
    -   **Header:** `# Sprint: <Cycle Name> (<Date Range>)`

3.  **Populate Tasks:**
    -   **Fetch:** Use `linear.list_issues` with filtering for the current cycle ID.
    -   **Format:** For each issue in the cycle:
        -   `- [ ] **Task:** <Issue Title> <!-- linear-issue: <Identifier> --> (Track: <Project Name/Backlog>)`
    -   **Group:** Organize tasks by their Project (Track).

4.  **Review Previous Sprint:**
    -   Read the *previous* sprint file (e.g., `2026-W07.md`).
    -   Calculate completion stats (e.g., "8/10 tasks completed").
    -   Add a summary comment to the top of the *new* sprint file.

5.  **Commit:**
    -   Commit the new sprint file with message: `chore(sprint): Initialize Sprint <YYYY-WXX>`.

## Procedure: Mid-Week Update (Optional)

**Goal:** Refresh the sprint file with latest statuses.

**Steps:**
1.  Read the current sprint file.
2.  For each task, check its status in Linear (or the corresponding `plan.md`).
3.  Update the markdown checkbox (`[ ]` -> `[x]`) to match reality.
