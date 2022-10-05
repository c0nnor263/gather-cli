buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
        classpath("io.github.gradle-nexus:publish-plugin:1.1.0")
    }
}
plugins{
    id ("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}
apply(from = "${rootDir}/scripts/publish-root.gradle")
apply(from = "${rootDir}/extras.gradle")