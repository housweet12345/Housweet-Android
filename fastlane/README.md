fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android test

```sh
[bundle exec] fastlane android test
```

Runs all the tests

### android beta

```sh
[bundle exec] fastlane android beta
```

Crashlytics Beta

### android validateReleaseToStore

```sh
[bundle exec] fastlane android validateReleaseToStore
```

Validate Play Console preconditions (no upload)

### android publishClosedDraft

```sh
[bundle exec] fastlane android publishClosedDraft
```

Upload to Closed track as draft

### android publishProductionDraft

```sh
[bundle exec] fastlane android publishProductionDraft
```

Upload to Production as draft

### android deploy

```sh
[bundle exec] fastlane android deploy
```

Deploy (legacy)

### android publishReleaseToStore

```sh
[bundle exec] fastlane android publishReleaseToStore
```



----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
