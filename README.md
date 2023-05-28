# Ditto Works SDK

This packages aims to provide a simple integration with [DittoWords](https://www.dittowords.com/) localizations allowing
one to fetch the strings for a project or components. The data fetched can be cached locally so that we don't need to
fetch strings everytime they are needed.

## TODOS

- [ ] Support filtering by status
- [ ] Support for components
- [ ] Support for variables

## Quick start

To integrate Ditto inside your app you simply have to include the SDK in your gradle configuration.
```kotlin
dependencies {
    // define the core library
    implementation("com.bidyut.tech.ditto:ditto-core:0.0.1")

    // define the preferred networking extension
    implementation("com.bidyut.tech.ditto:ditto-ktor:0.0.1")
    implementation("com.bidyut.tech.ditto:ditto-okhttp:0.0.1")
}
```

If you are using the version catalog you can configure it as the following
```toml
[versions]
dittowords-sdk = "0.0.1"

[libraries]
ditto-core = { group = "com.bidyut.tech.ditto", name = "ditto-core", version.ref = "dittowords-sdk" }
ditto-ktor = { group = "com.bidyut.tech.ditto", name = "ditto-ktor", version.ref = "dittowords-sdk" }
ditto-okhttp = { group = "com.bidyut.tech.ditto", name = "ditto-okhttp", version.ref = "dittowords-sdk" }
```

Instantiate the `DittoWords` instance based on the platform and network engine
```kotlin
val dittoWords = DittoWords {
    apiToken = "<YOUR_API_KEY>"
    dittoServiceFactory = DittoServiceKtorFactory(Android) {
        install(HttpCache)
    }
    databaseDriverFactory = DatabaseDriverFactory(context)
    cacheInterval = 1.toDuration(DurationUnit.HOURS)
}
```

## Fetching projects

Fetch all available projects in the account using

```kotlin
val dittoWords: DittoWords
// ...
dittoWords.getProjects()
```

## Fetching variants

Fetch all available variants in the account using

```kotlin
val dittoWords: DittoWords
// ...
dittoWords.getVariants()
```

## Fetching strings

Strings can be fetch all at once for a project and variant

```kotlin
val dittoWords: DittoWords
// ...
dittoWords.getStrings(
    "<PROJECT_ID>",
    "<VARIANT_ID>"
)
// or get the base variant
dittoWords.getStrings("<PROJECT_ID>")
```

Or we can request the string by their developer key

```kotlin
val dittoWords: DittoWords
// ...
dittoWords.getStringsByKey(
    "<DEVELOPER_KEY>",
    "<PROJECT_ID>",
    "<VARIANT_ID>"
)
// or get the base variant
dittoWords.getStringsByKey(
    "<DEVELOPER_KEY>",
    "<PROJECT_ID>"
)
```
