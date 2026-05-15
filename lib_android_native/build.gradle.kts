plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "io.github.limuyang2.xxhash.lib.internal"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
            version = "3.22.1"
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

group = "io.github.limuyang2"
version = "1.0.1"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "io.github.limuyang2"
                artifactId = "xxhash-android-native"
                version = "1.0.1"

                pom {
                    name.value("xxhash-android-native")
                    description.value("Android native JNI artifacts for xxhash.")
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
        }

        repositories {
            maven {
                setUrl("$rootDir/RepoDir")
            }
        }
    }
}
