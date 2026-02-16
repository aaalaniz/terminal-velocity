#!/bin/bash
set -e

echo "Starting Jules Environment Setup..."

# 1. Install Gemini CLI
if ! command -v gemini &> /dev/null; then
    echo "Gemini CLI not found. Installing..."
    npm install -g @google/gemini-cli
else
    echo "Gemini CLI is already installed."
fi

# 2. Install Conductor Extension
echo "Installing/Updating Conductor Extension..."
# We use '|| true' to prevent failure if it's already installed or if there's a temporary registry issue,
# but ideally we want it to succeed.
gemini extension install conductor

echo "Jules Environment Setup Complete."
