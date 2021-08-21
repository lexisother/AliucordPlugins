import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha08")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    if (name != "Aliucord" && name != "DiscordStubs") {
        apply(plugin = "com.android.library")
        
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

        dependencies {
            val implementation by configurations

            implementation(project(":Aliucord"))
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}