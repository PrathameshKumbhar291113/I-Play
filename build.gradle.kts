buildscript {
    dependencies {
        classpath(libs.symbol.processing.gradle.plugin)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.project.level.dependency) apply false
}