import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    `maven-publish`
    signing
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "io.github.limuyang2.xxhash.lib"
        compileSdk = 36
        minSdk = 21

        withHostTest {}

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-rules.pro")
            }
        }

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("stdlib"))
        }

        androidMain.dependencies {
            implementation(project(":lib_android_native"))
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.junit)
            implementation(libs.androidx.espresso.core)
        }
    }
}

//---------- maven upload info -----------------------------------

val versionName = "1.0.1"

var signingKeyId = "" //签名的密钥后8位
var signingPassword = "" //签名设置的密码
var secretKeyRingFile = "" //生成的secring.gpg文件目录

val localProperties: File = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signing.keyId")
    signingPassword = properties.getProperty("signing.password")
    secretKeyRingFile = properties.getProperty("signing.secretKeyRingFile")
} else {
    println("No props file, loading env vars")
}

group = "io.github.limuyang2"
version = versionName

publishing {
    publications.withType<MavenPublication>().configureEach {
        groupId = "io.github.limuyang2"
        version = versionName

        artifactId = when (name) {
            "kotlinMultiplatform" -> "xxhash"
            "android" -> "xxhash-android"
            else -> artifactId
        }

        pom {
            name.value("xxhash")
            description.value("A xxhash Kotlin Multiplatform library.")
            url.value("https://github.com/limuyang2/fast-xxhash-android")

            licenses {
                license {
                    name.value("The MIT License")
                    url.value("https://github.com/limuyang2/fast-xxhash-android/blob/main/LICENSE")
                }
            }

            developers {
                developer {
                    id.value("limuyang2")
                    name.value("limuyang")
                    email.value("limuyang2@hotmail.com")
                }
            }

            scm {
                connection.value("scm:git@github.com:limuyang2/fast-xxhash-android.git")
                developerConnection.value("scm:git@github.com:limuyang2/fast-xxhash-android.git")
                url.value("https://github.com/limuyang2/fast-xxhash-android")
            }
        }
    }

    repositories {
        maven {
            setUrl("$rootDir/RepoDir")
        }
    }
}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {
        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}
