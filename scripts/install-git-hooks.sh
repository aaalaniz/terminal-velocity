#!/bin/bash
GIT_DIR=$(git rev-parse --git-dir)
echo "Installing hooks..."
cp scripts/pre-commit $GIT_DIR/hooks/pre-commit
chmod +x $GIT_DIR/hooks/pre-commit
echo "Done!"
