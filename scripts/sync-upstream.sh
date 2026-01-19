#!/bin/bash

# Fetch the latest changes from origin
echo "Fetching latest changes from origin..."
git fetch origin main

# Try to rebase
echo "Attempting to rebase on origin/main..."
if git rebase origin/main; then
    echo "Successfully rebased on origin/main."
    exit 0
else
    echo "Conflict detected during rebase."
    git rebase --abort
    echo "Rebase aborted. Please manually resolve conflicts."
    exit 1
fi
