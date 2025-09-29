allprojects {
    buildscript {
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
        }
        dependencies {
            val nav_version = "2.9.5"
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        }
    }
}

tasks.register<Delete>("clean") {
    description = "Delete build directory on build"
    group = JavaBasePlugin.BUILD_TASK_NAME
    delete(rootProject.layout.buildDirectory)
}