# Terminal Velocity - Agent Context

## 🚨 PRIMARY MANDATE: The Conductor Workflow
**ALL development tasks must be managed via the Conductor file system.**

Instead of relying on CLI commands, you must manually read, update, and manage the markdown files in `conductor/`.

### 1. Incoming Request Protocol
When you receive a request, determine its scope:

*   **Existing Feature:**
    *   **Action:** Locate the track in `conductor/tracks.md`.
    *   **File:** Read `conductor/tracks/<track-id>/plan.md`.
    *   **Execute:** Update task status (`[ ]` -> `[~]`), implement changes, verify, and mark done (`[x]`).
    *   **Sync:** Ensure the task has a `<!-- linear-issue: ID -->` comment. If missing, create the Linear issue and add the ID.

*   **New Feature / Major Refactor:**
    *   **Action:** Create a new track directory: `conductor/tracks/<slug>/`.
    *   **Files:** Create `metadata.json`, `spec.md`, and `plan.md`.
    *   **Sync:** Create a **Linear Project** for this track and add the ID to `metadata.json`.
    *   **Register:** Add the new track to `conductor/tracks.md`.

*   **Bug Fix / Small Task:**
    *   **Action:** Use the **Backlog Track**.
    *   **File:** `conductor/tracks/backlog/plan.md`.
    *   **Sync:** Add the task with a `<!-- linear-issue: ID -->` comment. If ID is missing, create the Linear issue.

### 2. Linear Synchronization Policy
**The Repository is the Source of Truth.**
*   **IDs are Mandatory:** Every task in a `plan.md` MUST have a `<!-- linear-issue: ID -->` comment.
*   **Status Updates:** When you mark a task as `[x]` in a `plan.md`, you MUST update the corresponding Linear issue status to "Done".
*   **Project Linking:** Every Track (except Backlog) MUST have a `linear_project_id` in its `metadata.json`.

---

## Project Knowledge Base
Refer to these documents for detailed context:

*   **Product Vision:** [conductor/product.md](conductor/product.md)
*   **Technical Stack:** [conductor/tech-stack.md](conductor/tech-stack.md)
*   **Workflow Rules:** [conductor/workflow.md](conductor/workflow.md)
*   **Active Tracks:** [conductor/tracks.md](conductor/tracks.md)

## Key Conventions
*   **Language:** Kotlin (JVM)
*   **UI:** Mosaic (Jetpack Compose for Terminal)
*   **Architecture:** Circuit (UDF/Navigation) + Metro (DI)
*   **Testing:** Kotlin Test + Google Truth
*   **Formatting:** `ktfmt` (Run `./gradlew ktfmtFormat` before committing)

## File Structure
*   `src/main/java/xyz/alaniz/aaron/`: Application source code.
*   `src/main/resources/passages/`: Markdown content for typing games.
*   `conductor/`: Project management and documentation.
