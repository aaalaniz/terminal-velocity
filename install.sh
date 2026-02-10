#!/bin/bash
set -e

REPO="aaalaniz/terminal-velocity"
INSTALL_DIR="$HOME/.local/bin"
SHARE_DIR="$HOME/.local/share/terminal-velocity"
BINARY_NAME="terminal-velocity"

# Detect OS
OS="$(uname -s)"
case "$OS" in
    Linux*)     OS_TYPE="linux"; EXT="tar.gz" ;;
    Darwin*)    OS_TYPE="mac"; EXT="tar.gz" ;;
    CYGWIN*|MINGW*|MSYS*) OS_TYPE="windows"; EXT="zip" ;;
    *)          echo "Unsupported OS: $OS"; exit 1 ;;
esac

echo "Detected OS: $OS_TYPE"

# Find latest release
echo "Finding latest release for $REPO..."
# Use the API to get the tag name
LATEST_TAG=$(curl -s "https://api.github.com/repos/$REPO/releases/latest" | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/')

if [ -z "$LATEST_TAG" ]; then
    echo "Error: Could not find latest release tag."
    # Fallback to checking redirect if API fails (e.g. rate limit)
    LATEST_URL=$(curl -s -L -I -o /dev/null -w '%{url_effective}' "https://github.com/$REPO/releases/latest")
    LATEST_TAG=$(basename "$LATEST_URL")
    if [ -z "$LATEST_TAG" ] || [ "$LATEST_TAG" = "releases" ]; then
         echo "Failed to determine version."
         exit 1
    fi
fi

echo "Latest version: $LATEST_TAG"

ASSET_NAME="terminal-velocity-${OS_TYPE}.${EXT}"
DOWNLOAD_URL="https://github.com/$REPO/releases/download/$LATEST_TAG/$ASSET_NAME"

echo "Downloading $DOWNLOAD_URL..."

TMP_DIR=$(mktemp -d)
trap "rm -rf $TMP_DIR" EXIT

ASSET_FILE="$TMP_DIR/asset.$EXT"
curl -f -L -o "$ASSET_FILE" "$DOWNLOAD_URL"

echo "Installing to $SHARE_DIR..."

# Prepare directories
mkdir -p "$INSTALL_DIR"
mkdir -p "$(dirname "$SHARE_DIR")"
rm -rf "$SHARE_DIR"

# Extract and Install
if [ "$OS_TYPE" == "windows" ]; then
    unzip -q "$ASSET_FILE" -d "$TMP_DIR"
    # Assuming zip contains a single folder named 'terminal-velocity'
    mv "$TMP_DIR/terminal-velocity" "$SHARE_DIR"

    echo "Installation complete."
    echo "Executable is located at: $SHARE_DIR/$BINARY_NAME.exe"
    echo "Please add $(dirname "$SHARE_DIR") to your PATH or create a shortcut."
    exit 0

elif [ "$OS_TYPE" == "mac" ]; then
    tar -xzf "$ASSET_FILE" -C "$TMP_DIR"
    # Tarball contains 'terminal-velocity.app'
    mv "$TMP_DIR/terminal-velocity.app" "$SHARE_DIR"

    # Path to the actual binary inside the .app
    TARGET_BIN="$SHARE_DIR/Contents/MacOS/$BINARY_NAME"

else # Linux
    tar -xzf "$ASSET_FILE" -C "$TMP_DIR"
    # Tarball contains 'terminal-velocity' folder
    mv "$TMP_DIR/terminal-velocity" "$SHARE_DIR"

    # Path to launcher script/binary
    TARGET_BIN="$SHARE_DIR/bin/$BINARY_NAME"
fi

# Create Symlink
echo "Linking to $INSTALL_DIR/$BINARY_NAME..."
ln -sf "$TARGET_BIN" "$INSTALL_DIR/$BINARY_NAME"

echo "Success! Run '$BINARY_NAME' to start."
