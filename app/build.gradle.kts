plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.project.level.dependency)
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.prathameshkumbhar.iplay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prathameshkumbhar.iplay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //other dependencies
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.testing)
    implementation(libs.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation.compose)
    implementation(libs.runtime.livedata)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.constraintlayout.compose)
    implementation(libs.coil.compose )


}


/*

 import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.project.level.dependency)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    id 'dagger.hilt.android.plugin'
    id 'com.google.devtools.ksp'
}

android {
    namespace 'com.gd.salsforce'
    compileSdk 34

    defaultConfig {
        applicationId "com.gd.salsforce"
        minSdk 21
        targetSdk 34
        versionCode 21
        versionName "1.0.21"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary true
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.0'
    }
    packagingOptions {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }

    /*keystore pass gdff123
    * alias key0
    * password gdff123*/
}

dependencies {
    implementation libs.androidx.core.ktx
    implementation libs.lifecycle.runtime.ktx
    implementation libs.activity.compose
    implementation libs.lifecycle.runtime.compose
    implementation libs.compose.ui
    implementation libs.compose.ui.tooling.preview
    implementation libs.navigation.compose
    implementation libs.viewmodel.compose
    implementation libs.material3
    implementation libs.material.compose
    implementation libs.material.icons.extended
    implementation libs.coil.compose
    implementation libs.constraintlayout
    implementation libs.constraintlayout.compose
    implementation libs.play.services.location
    implementation libs.maps.compose
    implementation libs.maps.compose.utils
    implementation libs.maps.compose.widgets
    implementation libs.swiperefreshlayout
    implementation libs.appcompat
    implementation libs.firebase.crashlytics
    implementation libs.firebase.analytics
    implementation libs.firebase.auth
    testImplementation libs.junit
    androidTestImplementation libs.test.ext.junit
    androidTestImplementation libs.espresso.core
    androidTestImplementation libs.ui.test.junit4
    debugImplementation libs.ui.tooling
    debugImplementation libs.ui.test.manifest
    implementation libs.runtime.livedata
    implementation libs.lifecycle.livedata.ktx
    implementation libs.retrofit
    implementation libs.converter.gson
    implementation libs.okhttp
    implementation libs.logging.interceptor
    implementation libs.otto
    implementation libs.adapter.rxjava2
    implementation libs.hilt.android
    ksp libs.hilt.compiler
    implementation libs.hilt.navigation.compose
    implementation libs.datastore.preferences
    implementation libs.compressor
    implementation libs.glide
    ksp libs.glide.compiler
    implementation libs.glide.compose
    implementation libs.play.services.maps
    implementation libs.maps.compose.ktx
    implementation libs.maps.utils.ktx
    implementation libs.camera.camera2
    implementation libs.camera.lifecycle
    implementation libs.camera.view
    implementation libs.material
    implementation libs.datetime
}

tasks.withType(KotlinCompile).configureEach {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

*/