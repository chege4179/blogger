plugins {
    id("com.diffplug.spotless") version "5.3.0"
    id ("com.android.application") version "8.2.0" apply false
    id( "com.android.library") version "8.1.1" apply false
    kotlin("android") version "1.9.20" apply false
    kotlin("jvm") version "1.9.20" apply false
    kotlin("plugin.parcelize") version "1.9.20" apply false
    kotlin("plugin.serialization") version "1.9.20" apply false
    id ("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    id("com.android.test") version "8.2.0" apply false
    id("androidx.baselineprofile") version "1.2.1" apply false
}



// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies{
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
    }
}


apply(plugin = "com.diffplug.spotless")
spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile(
            rootProject.file("${project.rootDir}/spotless/LICENSE.txt"),
            "^(package|object|import|interface)"
        )
    }
}


