// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion = "1.3.21"

    repositories {
        maven(url = "https://jitpack.io")
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.2.0")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.0.0.RC6-3")
}

detekt {
    version = "1.0.0.RC7"
    profile("main") {
        input = "$projectDir/app/src/main/java"
        config = "$projectDir/default-detekt-config.yml"
        filters = ".*test.*,.*/resources/.*,.*/tmp/.*"
    }
}

allprojects {
    repositories {
        maven(url = "https://jitpack.io")
        google()
        jcenter()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
