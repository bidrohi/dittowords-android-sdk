plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
    id("maven-publish")
}

group = "com.bidyut.tech.ditto"
version = libs.versions.dittowords.sdk.get()

android {
    namespace = "com.bidyut.tech.ditto"
    compileSdk = 33
    buildToolsVersion = "33.0.2"
    defaultConfig {
        minSdk = 21
        aarMetadata {
            minCompileSdk = 33
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization.json)

    implementation(libs.sqldelight.runtime)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.sqldelight.android)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}

sqldelight {
    databases {
        create("DittoDatabase") {
            packageName.set("com.bidyut.tech.ditto.cache")
            sourceFolders.set(listOf("sqldelight/projects"))
        }
        create("WordsDatabase") {
            packageName.set("com.bidyut.tech.ditto.cache")
            sourceFolders.set(listOf("sqldelight/words"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            artifactId = "ditto-core"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
