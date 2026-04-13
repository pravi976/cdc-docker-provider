# cdc-docker-provider

Provider Spring Boot app that verifies consumer contracts from Pact Broker using `consumer-driven-contract-testing`.

## What This Pipeline Does

1. Runs provider verification tests against Pact Broker.
2. Publishes provider verification results.
3. Runs `can-i-deploy` for provider version.
4. Builds/pushes Docker image to Docker Hub.
5. Deploys to Kubernetes (if kube secret is configured).
6. Records deployment in Pact Broker.

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
- `PACT_BROKER_BASE_URL`
- `PACT_BROKER_USERNAME`
- `PACT_BROKER_PASSWORD`
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `KUBE_CONFIG_DATA` (base64 kubeconfig, optional for K8s deploy)

## Local Verification

Broker-based verification:

```powershell
cd C:\Users\pravi\spring-services\cdc-docker-provider
$env:PACT_BROKER_BASE_URL="http://localhost:9292"
$env:PACT_BROKER_USERNAME="..."
$env:PACT_BROKER_PASSWORD="..."
$env:GITHUB_SHA="local-dev-1"
$env:GITHUB_REF_NAME="main"
gradle clean test --tests "com.fedex.cdc.demo.provider.InventoryProviderPactVerificationTest" --stacktrace
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
