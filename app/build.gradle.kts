plugins {
    id("com.android.application")
}

android {
    namespace = "com.motorvitals"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.motorvitals"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures{
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
//    implementation("androidx.core:core:2.2.0")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.core:core-ktx:1.12.0") // Versione più recente di core-ktx


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}