
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")

}

configurations.all {
    resolutionStrategy {
        preferProjectModules()
    }
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
        //noinspection GradlePluginVersion
        classpath("com.android.tools.build:gradle:2.3.0")
    }
}

android {
    namespace = "com.example.libraryofworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.libraryofworld"
        minSdk = 28
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

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-analytics:18.0.4")
//    implementation("com.android.car.ui:car-ui-lib:2.5.1")
//    implementation("com.android.support:support-annotations:28.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //image
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.squareup.picasso:picasso:2.8")

    //fragment
    val fragment_version = "1.6.2"

    // Java language implementation
    implementation("androidx.fragment:fragment:$fragment_version")
    // Kotlin
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    // Testing Fragments in Isolation
    debugImplementation("androidx.fragment:fragment-testing:$fragment_version")

    //navigation
    val nav_version = "2.7.6"
    implementation ("androidx.navigation:navigation-fragment:$nav_version")
    implementation ("androidx.navigation:navigation-ui:$nav_version")

    //RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.5")

    //rxjava retrofit adapter
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //OKHTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    // For control over item selection of both touch and mouse driven selection
    implementation( "androidx.recyclerview:recyclerview-selection:1.1.0" )


    //room dependencies
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    ksp("androidx.room:room-compiler:2.5.0")
    implementation("androidx.core:core:1.6.0")

    //PDF
    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
//    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")



    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")


}
