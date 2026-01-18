plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "github.stecjr.srms"
    compileSdk = 35

    defaultConfig {
        applicationId = "github.stecjr.srms"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "2.0.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_SMTP_HOST", "\"${System.getenv("APP_SMTP_HOST") ?: ""}\"")
        buildConfigField(
            "String",
            "APP_SMTP_EMAIL_SENDER",
            "\"${System.getenv("APP_SMTP_EMAIL_SENDER") ?: ""}\""
        )
        buildConfigField(
            "String",
            "APP_SMTP_EMAIL_PASSWORD",
            "\"${System.getenv("APP_SMTP_EMAIL_PASSWORD") ?: ""}\""
        )

        packaging {
            resources {
                excludes.add("license/*")
                excludes.add("META-INF/*")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    applicationVariants.all {
        outputs.all {
            val appName = "srms"
            val versionName = android.defaultConfig.versionName
            val arch = filters.find { it.filterType == "ABI" }?.identifier ?: "universal"
            val newFileName = "${appName}_v${versionName}_${arch}.apk"
            (this as com.android.build.gradle.internal.api.ApkVariantOutputImpl).outputFileName =
                newFileName
        }
    }

    lint {
        abortOnError = false
    }

    tasks.register("checkSoAlignment") {
        group = "verification"
        description = "Run tools/check_so_align.py against release APKs and fail if any .so has incorrect p_align"
        dependsOn("assembleRelease")
        doLast {
            val python = "python"
            val checker = project.rootDir.resolve("tools/check_so_align.py")
            val apkDir = project.buildDir.resolve("outputs/apk/release")
            if (!checker.exists()) {
                throw GradleException("Checker script not found: $checker. Please ensure tools/check_so_align.py exists and pyelftools is installed.")
            }
            if (!apkDir.exists()) {
                logger.lifecycle("No release APKs found at $apkDir")
                return@doLast
            }
            val apks = apkDir.listFiles { f -> f.name.endsWith(".apk") } ?: arrayOf()
            if (apks.isEmpty()) {
                logger.lifecycle("No release APKs found to check in $apkDir")
                return@doLast
            }
            var failed = false
            for (apk in apks) {
                logger.lifecycle("Checking APK: ${apk}")
                val proc = ProcessBuilder(python, checker.absolutePath, "--apk", apk.absolutePath)
                    .inheritIO()
                    .start()
                val exit = proc.waitFor()
                if (exit != 0) {
                    failed = true
                }
            }
            if (failed) {
                throw GradleException("One or more APKs contain .so files with incorrect p_align. See logs above.")
            }
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.security.crypto)
    implementation(libs.javax.annotation.api)

    implementation(libs.openpdf)
    implementation(libs.androidawt)

    implementation(libs.android.mail)
    implementation(libs.android.activation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}