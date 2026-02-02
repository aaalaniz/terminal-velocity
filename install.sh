#!/bin/bash
#
# Terminal Velocity Installer
#
# This script downloads the latest release of Terminal Velocity from GitHub,
# extracts the standalone executable, and installs it to ~/.local/share/terminal-velocity.
# It also creates a symlink in ~/.local/bin for easy access.
#
# If the application is already installed, this script will update it to the latest version.
#
set -e

REPO="aaalaniz/terminal-velocity"
INSTALL_DIR="$HOME/.local/bin"
SHARE_DIR="$HOME/.local/share/terminal-velocity"
BINARY_NAME="terminal-velocity"

echo "Finding latest release for $REPO..."
LATEST_URL=$(curl -s -L -I -o /dev/null -w '%{url_effective}' "https://github.com/$REPO/releases/latest")

case "$LATEST_URL" in
  *"/tag/"*) ;;
  *)
    echo "Error: Could not find latest release (no tag found in URL)."
    exit 1
    ;;
esac

LATEST_TAG=$(basename "$LATEST_URL")

if [ -z "$LATEST_TAG" ]; then
  echo "Error: Could not determine latest version."
  exit 1
fi

echo "Latest version: $LATEST_TAG"

OS=$(uname -s)

case "$OS" in
  Linux*)     OS_TYPE="linux" ;;
  Darwin*)    OS_TYPE="mac" ;;
  CYGWIN*|MINGW*|MSYS*) OS_TYPE="windows" ;;
  *)          echo "Unsupported OS: $OS"; exit 1 ;;
esac

if [ "$OS_TYPE" == "windows" ]; then
  ASSET_NAME="terminal-velocity-windows.zip"
else
  ASSET_NAME="terminal-velocity-${OS_TYPE}.tar.gz"
fi

DOWNLOAD_URL="https://github.com/$REPO/releases/download/$LATEST_TAG/$ASSET_NAME"

echo "Downloading $DOWNLOAD_URL..."
TMP_DIR=$(mktemp -d)
trap "rm -rf $TMP_DIR" EXIT

ASSET_FILE="$TMP_DIR/asset"
curl -f -L -o "$ASSET_FILE" "$DOWNLOAD_URL"

echo "Installing..."
mkdir -p "$INSTALL_DIR"
mkdir -p "$(dirname "$SHARE_DIR")"
rm -rf "$SHARE_DIR"

if [ "$OS_TYPE" == "windows" ]; then
  unzip -q "$ASSET_FILE" -d "$TMP_DIR"
  mv "$TMP_DIR/terminal-velocity" "$SHARE_DIR"
  echo "Windows installation complete."
  echo "Please add $SHARE_DIR to your PATH."
  exit 0
elif [ "$OS_TYPE" == "mac" ]; then
  tar -xzf "$ASSET_FILE" -C "$TMP_DIR"
  if [ -d "$TMP_DIR/terminal-velocity.app" ]; then
      mv "$TMP_DIR/terminal-velocity.app" "$SHARE_DIR"
      TARGET_BIN="$SHARE_DIR/Contents/MacOS/$BINARY_NAME"
  else
      mv "$TMP_DIR/$BINARY_NAME" "$SHARE_DIR"
      TARGET_BIN="$SHARE_DIR/bin/$BINARY_NAME"
  fi
else
  # Linux
  tar -xzf "$ASSET_FILE" -C "$TMP_DIR"
  mv "$TMP_DIR/$BINARY_NAME" "$SHARE_DIR"
  TARGET_BIN="$SHARE_DIR/bin/$BINARY_NAME"
fi

ln -sf "$TARGET_BIN" "$INSTALL_DIR/$BINARY_NAME"

echo "Installation complete!"
echo "Installed to $SHARE_DIR"
echo "Symlinked to $INSTALL_DIR/$BINARY_NAME"
echo ""
echo "Make sure $INSTALL_DIR is in your PATH."
