//import com.android.build.gradle.BaseExtension
//
//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//        maven("https://jitpack.io")
//    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:7.0.1")
//        classpath("com.github.Aliucord:gradle:main-SNAPSHOT")
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        maven("https://jitpack.io")
//    }
//}
//
//fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()
//
//subprojects {
//    apply(plugin = "com.android.library")
//    apply(plugin = "com.aliucord.gradle")
//
//    android {
//        compileSdkVersion(30)
//
//        defaultConfig {
//            minSdk = 24
//            targetSdk = 30
//        }
//
//        compileOptions {
//            sourceCompatibility = JavaVersion.VERSION_11
//            targetCompatibility = JavaVersion.VERSION_11
//        }
//    }
//
//    dependencies {
//        val discord by configurations
//        val implementation by configurations
//
//        discord("com.discord:discord:aliucord-SNAPSHOT")
//        implementation("com.github.Aliucord:Aliucord:main-SNAPSHOT")
//
//        implementation("androidx.appcompat:appcompat:1.3.1")
//        implementation("com.google.android.material:material:1.4.0")
//        implementation("androidx.constraintlayout:constraintlayout:2.1.0")
//    }
//}
//
//task<Delete>("clean") {
//    delete(rootProject.buildDir)
//}

import com.aliucord.gradle.AliucordExtension
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.github.Aliucord:gradle:main-SNAPSHOT")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

fun Project.android(configuration: BaseExtension.() -> Unit) =
        extensions.getByName<BaseExtension>("android").configuration()

fun Project.aliucord(configuration: AliucordExtension.() -> Unit) =
        extensions.getByName<AliucordExtension>("aliucord").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "com.aliucord.gradle")
    apply(plugin = "kotlin-android")

    aliucord {
        author("Alyxia", 465702500146610176L)
        updateUrl.set("https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json")
        buildUrl.set("https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/%s.zip")
    }

    android {
        compileSdkVersion(30)

        defaultConfig {
            minSdk = 24
            targetSdk = 30
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        val discord by configurations
        val compileOnly by configurations

        discord("com.discord:discord:aliucord-SNAPSHOT")
        compileOnly("com.github.Aliucord:Aliucord:main-SNAPSHOT")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}