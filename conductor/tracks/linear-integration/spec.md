# Specification: Conductor-Linear Integration

## Objective
Establish a seamless workflow integration between Conductor (repository-based project management) and Linear (visual project management). The goal is to leverage Linear's UI for planning and visibility while maintaining the repository as the single source of truth for execution and tracking.

## Core Principles
1.  **Repository is Truth:** The `plan.md` files in Conductor tracks are the authoritative source for task status and progress.
2.  **Linear is Reflection:** Linear issues reflect the state of Conductor tracks and tasks. Updates flow primarily from Repo -> Linear.
3.  **Bi-directional Creation:** New work can originate in Linear (as issues) or in the Repo (as new tracks), but execution state is mastered in the Repo.
4.  **Automation First:** Jules automations handle the synchronization to minimize manual overhead.

## Data Model Mapping

| Conductor Entity | Linear Entity | Relationship | Notes |
| :--- | :--- | :--- | :--- |
| **Track** (Folder) | **Project** | 1:1 | A Conductor Track maps to a Linear Project. |
| **Phase** (Heading) | **Epic** or **Issue** | 1:N | Phases are groupings; often map to Epics if complex, or just grouping of issues. |
| **Task** (Checklist) | **Issue** | 1:1 | Each task in `plan.md` corresponds to a Linear Issue. |
| **Status** | **Workflow State** | 1:1 | `[ ]` -> Todo, `[~]` -> In Progress, `[x]` -> Done. |

### Mapping Mechanism
To maintain the repository as the source of truth while linking to Linear, we will use HTML comments within the `plan.md` files:
*   **Track Mapping:** Stored in `metadata.json` as `linear_project_id`.
*   **Task Mapping:** Append an HTML comment to the task line in `plan.md`.
    *   *Example:* `- [ ] **Task:** Implement login <!-- linear-issue: TVELO-42 -->`

## Local Developer Requirements
For developers (and agents) to work locally with this system:
1.  **Gemini CLI Conductor Extension:** Must be installed and enabled.
2.  **Linear MCP:** Must be configured in the local `.gemini/settings.json` or global configuration to allow local agents to interact with Linear directly.
3.  **Authentication:** Developers need a Linear API key configured in their environment.

## Strict Linking Policy (No Untracked Work)
To ensure the visual surface (Linear) always matches the repository's intent:
1.  **ID Requirement:** Every task entry in a `plan.md` file must eventually contain a `<!-- linear-issue: ID -->` comment.
2.  **Auto-Creation:** The sync automation is responsible for detecting tasks without IDs, creating the corresponding issue in Linear, and updating the `plan.md` with the new ID.
3.  **Validation Gate:** Tasks without Linear IDs are considered "invalid" and should not be moved to "In Progress" (`[~]`) until an ID is assigned.

## Workflows

### 1. Sync: Repo -> Linear (Progress Updates)
*   **Trigger:** Push to `main` or specific schedule.
*   **Action:**
    1.  Scan all active `plan.md` files.
    2.  For each task with a Linear Issue ID (stored in markdown comment or metadata), update the Linear issue status.
    3.  If a task is marked `[x]`, move Linear issue to "Done".

### 2. Sync: Linear -> Repo (New Work)
*   **Trigger:** Scheduled Automation (e.g., Hourly or "Sprint Planning").
*   **Action:**
    1.  Fetch all "Active" Projects in Linear Team `TVELO`.
    2.  Check for corresponding Conductor Track.
    3.  If missing, create a new Track:
        *   `conductor/tracks/<linear-project-slug>/`
        *   `spec.md`: Populated from Project Description.
        *   `plan.md`: Populated from Project Issues.
        *   `metadata.json`: Stores Project ID.
    4.  If present, update `spec.md` if description changed (optional).

### 3. Sprint Planning (Weekly)
*   **Trigger:** Monday Morning Automation.
*   **Action:**
    1.  Fetch current Cycle from Linear.
    2.  Identify all Issues in the Cycle.
    3.  Ensure all corresponding Conductor Tracks/Tasks are active.
    4.  Generate a `conductor/sprints/current_sprint.md` summary.
    5.  Post summary to Slack/Discord or email (if configured).

## Implementation Strategy
We will implement this using **Agent-based Synchronization**. Instead of a standalone script, we rely on the **Linear MCP** tools.

### Sync Workflow (Jules/Local Agent)
1.  **Scan:** Read all `plan.md` files in `conductor/tracks/`.
2.  **Verify IDs:** For any task without a `<!-- linear-issue: ID -->` comment, use the `linear_mcp.create_issue` tool and write the resulting ID back to the file.
3.  **Update Status:** For tasks with IDs, compare the markdown status (`[ ]`, `[~]`, `[x]`) with the Linear issue state. Use `linear_mcp.update_issue` to align them.
4.  **Monday Sprint Planning:** A scheduled Jules task fetches the "Current Cycle" via Linear MCP and scaffolds the weekly sprint file in `conductor/sprints/`.

### Quality Gates
- No task is marked "In Progress" without a Linear ID.
- Every commit that updates a task status in `plan.md` must be followed by a sync check.
