pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "dittowords-android-sdk"
include(":dittoWordsSdk:core")
include(":dittoWordsSdk:ktor")
include(":dittoWordsSdk:okhttp")
include(":example")
