// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("http://oss.jfrog.org/artifactory/oss-snapshot-local/")
    }
    dependencies {
        classpath(Dependencies.Plugin.androidGradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath(Dependencies.Kotlin.extensions)
        classpath(Dependencies.Helper.ktLint)
        classpath(Dependencies.Plugin.hiltGradlePlugin)
        classpath(Dependencies.Plugin.navGradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}