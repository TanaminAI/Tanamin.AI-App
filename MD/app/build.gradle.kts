plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.dicoding.tanaminai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.tanaminai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_SOIL", "\"https://tanamin-ai-backend-bkq3xprrxa-et.a.run.app\"")
        buildConfigField("String", "BASE_AUTH", "\"https://soilapp-api-bkq3xprrxa-uc.a.run.app/\"")
        buildConfigField("String", "BASE_WEATHER", "\"https://api.openweathermap.org/\"")
        buildConfigField("String", "KEY_WEATHER", "\"196abc4b6c8ee31eeb8c0eefd8330183\"")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Splash Screen
    implementation (libs.androidx.core.splashscreen)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // tensorflow lite
    implementation("com.google.android.gms:play-services-tflite-java:16.1.0")
    implementation("com.google.android.gms:play-services-tflite-gpu:16.2.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // ktx
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")

    // location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // view pager
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // database
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}