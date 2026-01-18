# Continuous Integration

This project uses **GitHub Actions** for Continuous Integration.

## Workflow

The CI pipeline is defined in `.github/workflows/ci.yml`.

### Triggers

- **Push:** Automatically triggered on every push to the `main` branch.
- **Pull Request:** Automatically triggered on every pull request targeting the `main` branch.

### Jobs

The `build` job runs on `ubuntu-latest` and performs the following steps:

1.  **Checkout:** Clones the repository using `actions/checkout`.
2.  **Set up JDK 21:** Installs JDK 21 (Temurin distribution) using `actions/setup-java`.
3.  **Setup Gradle:** Configures Gradle and enables dependency caching using `gradle/actions/setup-gradle`.
4.  **Check Formatting:** Executes `./gradlew ktfmtCheck` to verify code formatting.
5.  **Build with Gradle:** Executes `./gradlew assemble` to ensure the project compiles correctly.
6.  **Run Tests:** Executes `./gradlew test` to run all unit tests.
7.  **Upload Test Report:** If tests fail, the HTML test report is uploaded as a GitHub Action artifact named `test-report`.

## Local Verification

Before pushing changes, you can simulate the CI pipeline locally by running:

```bash
./gradlew ktfmtCheck assemble test
```
