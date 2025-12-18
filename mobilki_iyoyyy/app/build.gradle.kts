plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.mobilki_iyoyyy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobilki_iyoyyy"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

//dependencies {
//    // AndroidX и Material
//
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("io.appwrite:sdk-for-android:8.1.0")
//    implementation(libs.volley) // <-- здесь добавляем Appwrite SDK
//    testImplementation("junit:junit:4.13.2")
//
//
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.activity:activity-ktx:1.7.2")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.recyclerview:recyclerview:1.3.0")
//
//
//    // Тесты
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//
//    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
//
//    implementation("com.google.android.material:material:1.12.0")
//
//    implementation("com.android.volley:volley:1.2.1")
//
//    implementation("androidx.core:core:1.12.0")
//
//
//}
dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    implementation("com.google.android.material:material:1.11.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("com.android.volley:volley:1.2.1")
    implementation("io.appwrite:sdk-for-android:8.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
    implementation("com.google.android.gms:play-services-auth:20.6.0")


}
