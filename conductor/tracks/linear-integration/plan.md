# Plan: Conductor-Linear Integration

## Phase 1: Data Model & Prototyping
This phase focuses on defining how Conductor tracks map to Linear projects and verifying API access.

- [x] Task: Define the extended metadata schema for Conductor tracks (add `linear_project_id`, `linear_team_id`).
- [x] Task: Verify Linear MCP connectivity and tool access (create, update status).
- [x] Task: Configure Linear MCP in the project's `.gemini/settings.json` for local agent usage.

## Phase 2: Sync Logic Definition
Define the instructions for agent-based synchronization.

- [x] Task: Create `conductor/tracks/linear-integration/sync-instructions.md`.
    -   Detailed prompt for an agent to perform the `Linear -> Repo` sync.
    -   Detailed prompt for an agent to perform the `Repo -> Linear` sync.
- [x] Task: Test the sync instructions manually with a trial task (Backlog & Audio Feedback).
- [x] Task: Create `conductor/tracks/linear-integration/sprint-instructions.md` for weekly cycle management.

## Phase 3: Automation Setup
Set up the automated triggers to run the sync instructions via Jules.

- [ ] Task: Configure the "Monday Morning Sprint" Jules automation using `sprint-instructions.md`.
- [ ] Task: Configure the periodic "Daily Sync" Jules automation using `sync-instructions.md`.

## Phase 4: Documentation & Rollout
Finalize documentation and enable the integration for the team.

- [x] Task: Update `README.md` with explicit instructions for local developers (Conductor extension + Linear MCP).
- [x] Task: Update `conductor/workflow.md` to document the new Linear integration process.
- [x] Task: Create `conductor/sprints/` directory structure.
- [x] Task: Initialize `backlog` track for loose issues.
- [ ] Task: Announce the new workflow to the team (update README/AGENTS.md).
