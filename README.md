# DevOps Candidate Exercise: Java + Spring Boot Profiles, Docker, Jenkins, Cloud Run

This repository contains a simple Spring Boot "Hello World" app that uses both Java `-Dspring.profiles.active` and Spring Boot profile properties. Your task is to demonstrate DevOps skills by experimenting with profiles, containerizing the app, and building a CI/CD pipeline that deploys to Google Cloud Run.

Please read the full activity, complete the tasks, and submit your implementation via a PR or a branch with a short write-up.

## Objectives
- Work with Java and Spring Boot profiles.
- Write a production-grade multi-stage `Dockerfile` with best practices.
- Create a `Jenkinsfile` pipeline using a Kubernetes agent with two containers (Java/Maven, `docker` and `gcloud`).
- Run unit tests, build and push the container image, and deploy to Cloud Run.
- Use a dummy service account secret for the `gcloud` container to authenticate.

## Repo Overview
- App entry: `src/main/java/com/example/helloworld/HelloWorldApplication.java`
- Profiles config: `src/main/java/com/example/helloworld/config/ProfileConfig.java`
- Service: `src/main/java/com/example/helloworld/service/MessageService.java`
- Properties: `src/main/resources/application.properties`, `application-dev.properties`, `application-prod.properties`
- Tests: `src/test/java/com/example/helloworld/...`

## Assumptions
- Jenkins running in a Kubernetes cluster is available and can provision pods.
- A Jenkins credential called `gcloud-sa` exists and contains `key.json` from a gcp service account with permissions to push to Artifact Registry and deploy Cloud Run.
- Artifact Registry repository exists to receive container images: `us-central1-docker.pkg.dev/my-project/my-repo`.
- Artifact Registry repository and Cloud Run are on the same project and region
- Cloud Run API is enabled in the target project.
- Jenkins is already configured with a multibranch job listening to `main`, so checkout of repository is not necessary.

## Candidate Tasks

1. Dockerfile (multi-stage, best practices)
	 - Create a `Dockerfile` at repo root that:
		 - Uses a builder stage (Maven + JDK) to compile and run tests.
		 - Uses a slim runtime stage (JRE) and runs as non-root.
		 - Supports passing `SPRING_PROFILES_ACTIVE` at runtime via env or JVM arg.
		 - Includes healthcheck and sensible defaults.
		 - Uses `.dockerignore` to keep the image clean.

1. Jenkinsfile (Kubernetes agent, sidecars)
	 - Create a `Jenkinsfile` that defines a declarative pipeline using a Kubernetes agent with three containers:
		 - `maven`: for testing (Java/JDK + Maven)
         - `docker`: for building docker image
		 - `gcloud`: for authentication and deploying to Cloud Run
	 - Pipeline stages:
		 - Unit Tests (Maven)
		 - Build & Push Image (use `docker` and remember to authenticate so you can perform push command)
		 - Deploy to Cloud Run
	 - Use `gcloud-sa` Jenkins credential for interaction with gcp 
	 - Use environment to define project id, region, service name, and image tag.
     - Cloud Run deployment runs only on merge (not PR). Remember is a multibranch job.

1. Cloud Run deployment
	 - Deploy the container to Cloud Run with traffic set to 100%.
	 - Configure environment variables or args to set the active profile at deploy time.

## Evaluation Criteria
- Profiles: Clear demonstration of switching and effects.
- Dockerfile: Multi-stage, minimal runtime, non-root, healthcheck, versioned base images.
- Jenkinsfile: Kubernetes agent with containers, environments, proper auth and deployment.
- Cloud Run: Successful deploy with env configuration.
