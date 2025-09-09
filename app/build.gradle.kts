plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("androidx.room")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

val productionBaseUrl = "\"https://dptinfo.iutmetz.univ-lorraine.fr/applis/edt/serveur/\""
val developmentBaseUrl = "\"https://dptinfo.iutmetz.univ-lorraine.fr/applis/edt/serveur/\""

android {
    namespace = "com.example.edt"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.edt"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        room {
            schemaDirectory("$projectDir/schemas")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("host")

    productFlavors {
        create("production") {
            dimension = "host"
            buildConfigField("String", "BASE_URL", productionBaseUrl)
        }

        create("development") {
            dimension = "host"
            buildConfigField("String", "BASE_URL", developmentBaseUrl)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kapt {
        correctErrorTypes = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    val roomVersion="2.6.1"
    val daggerVersion="2.57.1"
    val timberVersion="5.0.1"
    val retrofitVersion="2.11.0"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")

    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    implementation("com.jakewharton.timber:timber:${timberVersion}")

    implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-scalars:${retrofitVersion}")
    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.0.0-alpha.10"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}

kapt {
    correctErrorTypes = true
}