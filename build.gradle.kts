allprojects {
    buildscript {
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
        }
    }
}

tasks.register<Delete>("clean") {
    description = "Delete build directory on build"
    group = JavaBasePlugin.BUILD_TASK_NAME
    delete(rootProject.layout.buildDirectory)
}