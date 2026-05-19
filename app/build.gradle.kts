plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.example.taskflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.taskflow"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_17
        targetCompatibility =
            JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // =========================
    // CORE
    // =========================
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // =========================
    // ACTIVITY + FRAGMENT
    // =========================
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.fragment:fragment-ktx:1.8.2")

    // =========================
    // RECYCLERVIEW
    // =========================
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // =========================
    // CARDVIEW
    // =========================
    implementation("androidx.cardview:cardview:1.0.0")

    // =========================
    // LIFECYCLE / MVVM
    // =========================
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

    // =========================
    // COROUTINE
    // =========================
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // =========================
    // ROOM DATABASE
    // =========================
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // =========================
    // RETROFIT
    // =========================
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // =========================
    // OKHTTP LOGGING
    // =========================
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // =========================
    // NAVIGATION
    // =========================
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")

    // =========================
    // WORK MANAGER
    // =========================
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // =========================
    // CALENDAR
    // =========================
    //implementation("com.github.prolificinteractive:material-calendarview:2.0.1")

    // =========================
    // IMAGE LOADER (AVATAR)
    // =========================
    implementation("io.coil-kt:coil:2.7.0")

    // =========================
    // TEST
    // =========================
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(
        "androidx.test.ext:junit:1.2.1"
    )
    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.6.1"
    )
}