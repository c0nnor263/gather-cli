import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")

}
apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")

group = property("PUBLISH_GROUP_ID")!!
version = property("PUBLISH_VERSION")!!

val key1: String = gradleLocalProperties(rootDir).getProperty("OBFUSTRING_KEY")

android {
    namespace = "io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")


    }

    buildTypes {
        debug{
            buildConfigField ("String", "fob1", key1)
        }
        release {

            buildConfigField ("String", "fob1", key1)
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

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation("com.google.android.material:material:1.6.1")
    testImplementation("junit:junit:4.13.2")


    // Lifecycle + ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Volley
    implementation("com.android.volley:volley:1.2.1")


    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.5.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-installations:17.1.0")
    implementation("com.google.firebase:firebase-database-ktx")

    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")


    implementation ("com.google.code.gson:gson:2.9.1")
    // Obfustring
    implementation ("io.github.c0nnor263:obfustring-core:10.05")
}