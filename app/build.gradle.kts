plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

val ktlint: Configuration by configurations.creating
val kotlinVersion = "1.3.21"
val androidSupportVersion = "28.0.0"
val daggerVersion = "2.13"
val archRoomVersion = "1.1.1"
val lifecycleVersion = "2.2.0-alpha02"
val coroutineVersion = "1.1.1"

val androidCompileSdk = 28
val androidBuildTools = "28.0.0"
val androidMinSdk = 21
val androidTargetSdk = 28
val androidVersionName = "1.1"
val androidVersionCode = 3
val MovieAPIKey = "\"3d6c79a2c15d8b742f15246311632b08\""

android {
    compileSdkVersion(androidCompileSdk)
    defaultConfig {
        applicationId = "com.movie.app"
        minSdkVersion(androidMinSdk)
        targetSdkVersion(androidTargetSdk)
        versionCode = androidVersionCode
        versionName = androidVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    lintOptions {
        isAbortOnError = false
    }
    buildTypes.forEach {
        it.buildConfigField("String", "MOVIE_API_KEY", MovieAPIKey)
    }
}

/*
tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(FAILED, PASSED, SKIPPED, STANDARD_OUT)
        showStandardStreams = true
    }
}
*/

/***
 * ktlint
 */
tasks.register<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.github.shyiko.ktlint.Main"
    args("--android", "src/**/*.kt")
}

tasks.named("check") {
    dependsOn(ktlint)
}
tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.github.shyiko.ktlint.Main"
    args("--android", "-F", "src/**/*.kt")
}

dependencies {
    testImplementation("io.mockk:mockk:1.8.13")
    androidTestImplementation("io.mockk:mockk-android:1.8.13")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")

   // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("com.github.bumptech.glide:glide:4.9.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")
    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.46")

    //Room
    implementation("androidx.room:room-runtime:2.1.0")
    kapt("androidx.room:room-compiler:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.0.1")
    implementation("androidx.room:room-coroutines:2.1.0-alpha04")

    implementation("com.jakewharton.timber:timber:4.7.0")

    implementation("com.github.santalu:emptyview:1.3.4")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("com.github.PierfrancescoSoffritti:AndroidYouTubePlayer:6.0.0")
    ktlint("com.github.shyiko:ktlint:0.29.0")
    implementation("com.mikepenz:materialdrawer:6.0.8")

    // Koin for Android
    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-androidx-scope:2.0.1")
    implementation("org.koin:koin-androidx-viewmodel:2.0.1")

    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
}
