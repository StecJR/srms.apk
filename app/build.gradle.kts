plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.stec.srms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stec.srms"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
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

        packagingOptions {
            exclude("license/*")
            exclude("META-INF/*")
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
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips64")
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