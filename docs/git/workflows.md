## Continuous Integration (CI)

Workflow configuration is based on [KaMPKit’s workflows](https://github.com/touchlab/KaMPKit/tree/main/.github/workflows) and ensures that every push and pull request is properly built and tested.

## Workflow Configuration 

[📄 File](/.github/workflows/FinBlue-Android.yml)

### Workflow Name
```yaml
name: FinBlue-Android
```
The workflow is named **FinBlue-Android**. This name is displayed in the **GitHub Actions UI**.

### Trigger Conditions
```yaml
on:
  workflow_dispatch:
  push:
    branches:
      - main
  pull_request:
    paths-ignore:
      - "**.md"
      - "*.png"
      - docs
      - iosApp
```
- **workflow_dispatch** → Allows manual triggeringfrom GitHub UI.
- **push** → Runs the workflow when code is pushed to main.
- **pull_request** → Runs on pull requests, but ignores changes in:
	- Markdown (.md)
	- Images (.png)
	- Documentation folder (docs/)
	- iOS project (iosApp/)

### Build Job
```yaml
jobs:
  build:
    runs-on: ubuntu-latest
```
Defines a **single job** named build and the OS/environment for the CI job

### Checkout Repository
```yaml    
steps:
  - name: Checkout repository
    uses: actions/checkout@v4
```
Fetches the latest repository code so the workflow can access it.

### Set Up Java
```yaml
  - name: Setup Java
    uses: actions/setup-java@v4
    with:
      distribution: corretto
      java-version: 21
```
Installs Amazon Corretto JDK 21* which is used to build the project.

### Set Up Gradle
```yaml
  - name: Setup Gradle
    uses: gradle/actions/setup-gradle@v4
```
Speeds up builds by caching Gradle dependencies.

### Create local.properties File
```yaml
  - name: Create local.properties
    run: |
      echo "API_KEY=${{ secrets.API_KEY }}" > local.properties
    shell: bash
```
[BuildKonfig](https://github.com/yshrsmz/BuildKonfig) expects an **API key** in local.properties.
Reads GitHub Secret → ${{ secrets.API_KEY }}.
Without this step, the build will fail!

### Build the Project
```yaml
   - name: Build Project
     run: ./gradlew build
```
Runs Gradle build to compile the project and ensure everything is working.
