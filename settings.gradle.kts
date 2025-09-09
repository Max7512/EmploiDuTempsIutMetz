pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

    plugins {
        val kotlinVersion = "2.0.21"

        id("androidx.room") version "2.7.2"
        id("com.google.dagger.hilt.android") version "2.57.1"
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Emploi du temps"
include(":app")
 