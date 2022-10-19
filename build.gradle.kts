buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("io.github.gradle-nexus:publish-plugin:1.1.0")
    }
}
plugins{
    id ("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}
apply(from = "${rootDir}/scripts/publish-root.gradle")
apply(from = "${rootDir}/extras.gradle")