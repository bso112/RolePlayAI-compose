@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.bso112.roleplayai.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.bso112.roleplayai.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.activity.ktx)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.ktor)
    implementation(libs.lifecycle.compose)
    implementation(libs.gson)
    implementation(libs.imagechopper)
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.paging:paging-compose:3.2.0-beta01")
    implementation("io.coil-kt:coil:2.3.0")
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
}