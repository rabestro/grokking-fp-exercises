#!/bin/bash
set -euo pipefail

# Check if Homebrew is installed
if ! command -v brew >/dev/null 2>&1; then
    echo "Error: Homebrew is not installed" >&2
    exit 1
fi

# Install git
if ! command -v git >/dev/null 2>&1; then
    echo "Installing Git..."
    if ! brew install git; then
        echo "Error: Failed to install Git" >&2
        exit 1
    fi
else
    echo "Git is already installed"
fi

# Get the full name of the current user
FULL_NAME=$(id -F)

# Get the username of the current user
USERNAME=$(whoami)

# Default email domain
DEFAULT_DOMAIN="evolution.com"

# Allow override through environment variable
DOMAIN="${GIT_EMAIL_DOMAIN:-$DEFAULT_DOMAIN}"

# Construct the email address
if [ -n "${GIT_EMAIL:-}" ]; then
    EMAIL="$GIT_EMAIL"
else
    EMAIL="$USERNAME@$DOMAIN"
    echo "Using default email: $EMAIL"
    echo "To use a different email, set GIT_EMAIL environment variable"
fi

# Validate email format
if ! echo "$EMAIL" | grep -qE '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'; then
    echo "Error: Invalid email format: $EMAIL" >&2
    exit 1
fi

# Configure Git with the full name
git config --global user.name "$FULL_NAME"

# Configure Git with the email
git config --global user.email "$EMAIL"

# Output the configured values for verification
echo "Git configuration updated:"
echo "Name: $(git config --global user.name)"
echo "Email: $(git config --global user.email)"
