# Pipelines

This directory contains source-oriented pipeline files and shared scripts.

Codemagic executes only the root `codemagic.yaml` file.

## Files

- `android-tests.yaml`: workflow-specific source for android tests pipeline
- `android-build-deploy.yaml`: workflow-specific source for android build and deploy pipeline
- `scripts/`: extracted shell scripts used by workflows

## Current status

Root `codemagic.yaml` is the runtime configuration and already references scripts from `pipelines/scripts`.
The YAML files in this folder are for maintainability and future generation/merge automation.
