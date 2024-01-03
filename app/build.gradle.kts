plugins {

    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.personalrecordv4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.personalrecordv4"
        minSdk = 27
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")
    //Firebase SDK
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    //Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    //Firestore
    implementation("com.google.firebase:firebase-firestore")
    //Cloud Storage
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.firebase:firebase-storage:20.2.2")
    implementation ("com.github.bumptech.glide:glide:4.13.0")

    implementation ("androidx.media3:media3-exoplayer:1.1.1")
    implementation ("androidx.media3:media3-exoplayer-dash:1.1.1")
    implementation ("androidx.media3:media3-ui:1.1.1")

    //Camera
    val cameraxVersion = "1.3.0-rc01"
    implementation ("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-extensions:$cameraxVersion")
    implementation("androidx.work:work-runtime:2.9.0")

}