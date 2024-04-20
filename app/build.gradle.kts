plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.geoskills2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.geoskills2"
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
    viewBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Navigation
    val nav_version = "2.7.7"

    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    // Loadind dots
    implementation ("com.github.razaghimahdi:Android-Loading-Dots:1.3.2")
    // Slide dots
    implementation ("com.tbuonomo:dotsindicator:5.0")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1")) //Bom
    implementation("com.google.firebase:firebase-analytics") // Analytics
    // Auth
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    //Firestore
    implementation("com.google.firebase:firebase-firestore")
    // Click Effect
    implementation ("com.github.muratozturk5:ClickShrinkEffectLibrary:1.2.0")
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    // Cookie Bar
    implementation ("org.aviran.cookiebar2:cookiebar2:1.1.5")
}