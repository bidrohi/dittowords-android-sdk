plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

group = "com.bidyut.tech.ditto"
version = libs.versions.dittowords.sdk.get()

android {
    namespace = "com.bidyut.tech.ditto.okhttp"
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
    implementation(project(":dittoWordsSdk:core"))

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization.json)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            artifactId = "ditto-okhttp"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
