plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services) // Plugin de Google Services
}

android {
    namespace = "com.example.socialab2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.socialab2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Implementación de dependencias comunes
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Dependencias de Firebase (mantén solo una versión por biblioteca)
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-functions:20.1.1")
    implementation("com.google.firebase:firebase-database:20.0.5")

    // Dependencias de Google Play Services
    implementation("com.google.android.gms:play-services-auth:20.2.0")

    // Dependencias de pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
