#!/bin/bash

# Install git
brew install git

# Get the full name of the current user
FULL_NAME=$(id -F)

# Get the username of the current user
USERNAME=$(whoami)

# Construct the email address
EMAIL="$USERNAME@evolution.com"

# Configure Git with the full name
git config --global user.name "$FULL_NAME"

# Configure Git with the email
git config --global user.email "$EMAIL"

# Output the configured values for verification
echo "Git configuration updated:"
echo "Name: $(git config --global user.name)"
echo "Email: $(git config --global user.email)"
