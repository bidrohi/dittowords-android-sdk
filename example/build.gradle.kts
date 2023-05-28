plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bidyut.tech.ditto.example"
    compileSdk = 33
    buildToolsVersion = "33.0.2"
    defaultConfig {
        applicationId = "com.bidyut.tech.ditto.example"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.get()
    }
    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    flavorDimensions += "network"
    productFlavors {
        create("ktor") {
            dimension = "network"
            versionNameSuffix = "-ktor"
        }
        create("okhttp") {
            dimension = "network"
            versionNameSuffix = "-okhttp"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":dittoWordsSdk:core"))

    "ktorImplementation"(project(":dittoWordsSdk:ktor"))
    "ktorImplementation"(libs.ktor.client.core)
    "ktorImplementation"(libs.ktor.client.android)
    "ktorImplementation"(libs.ktor.client.logging)
    "ktorImplementation"(libs.ktor.client.encoding)
    "ktorImplementation"(libs.ktor.client.contentNavigation)
    "ktorImplementation"(libs.ktor.serialization.json)

    "okhttpImplementation"(project(":dittoWordsSdk:okhttp"))
    "okhttpImplementation"(platform(libs.okhttp.bom))
    "okhttpImplementation"(libs.okhttp.core)
    "okhttpImplementation"(libs.okhttp.logging)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.graphics)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.toolingPreview)
    implementation(libs.compose.material3)

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation)
}
