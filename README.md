# cdc-docker-provider Steps

Provider Spring Boot app that verifies consumer contracts from shared GitHub contract files using `consumer-driven-contract-testing`.

## What This Pipeline Does

1. Fetches latest consumer contract files from shared GitHub `contracts` branch (or specific SHA from dispatch payload).
2. Runs provider verification tests from local pact files via `PACT_FOLDER`.
3. Publishes provider verification status JSON back to shared contracts branch.
4. Enables `can-i-deploy` equivalent for consumer and provider deployment gates.
4. Builds/pushes Docker image to Docker Hub.
5. Deploys to Kubernetes (if kube secret is configured).

## Decoupled Dependency Setup

This repo no longer uses `includeBuild`.

It consumes framework artifacts via:
- `mavenLocal()` (local dev)
- GitHub Packages (`https://maven.pkg.github.com/<owner>/<repo>`)

Main dependency:
- `com.fedex.cdc:cdc-pact-spring-boot-starter:${cdcFrameworkVersion}`

## Required GitHub Variables

- `CDC_FRAMEWORK_VERSION` (example: `1.0.0-SNAPSHOT`)
- `CDC_PACKAGES_OWNER` (example: `pravi`)
- `CDC_PACKAGES_REPO` (example: `consumer-driven-contract-testing`)

## Required GitHub Secrets

- `GH_PACKAGES_TOKEN` (PAT with `read:packages`)
- `GH_REPO_DISPATCH_TOKEN` (classic PAT with `repo` scope to read/write shared contracts branch)
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `KUBE_CONFIG_DATA` (base64 kubeconfig, optional for K8s deploy)

## Optional GitHub Variables

- `SHARED_CONTRACT_REPO` (default: `pravi976/cdc-docker-consumer`)
- `SHARED_CONTRACT_BRANCH` (default: `contracts`)

## Local Verification

Shared-file-based verification:

```powershell
cd C:\Users\pravi\spring-services\cdc-docker-provider
$env:PACT_FOLDER="C:\path\to\contracts\pacts\supply-orders-consumer\<consumer-sha>"
gradle clean test --tests "com.fedex.cdc.demo.provider.InventoryProviderLocalPactVerificationTest" --stacktrace
```

Build app jar:

```powershell
gradle clean bootJar -x test --stacktrace
```

Build image:

```powershell
docker build -t <dockerhub-user>/cdc-docker-provider:local .
```

Run container:

```powershell
docker run --rm -p 8082:8082 <dockerhub-user>/cdc-docker-provider:local
```

## GitHub Actions

Workflow:

```text
.github/workflows/provider-cdc.yml
```

Push to `main` (or run manually from Actions tab) to trigger verify + gate + deploy flow.

## Kubernetes

Manifests:

```text
k8s/deployment.yaml
k8s/service.yaml
```

The workflow replaces `__IMAGE__` with `DOCKERHUB_USERNAME/cdc-docker-provider:${GITHUB_SHA}` before applying.
