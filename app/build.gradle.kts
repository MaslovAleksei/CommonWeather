plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\erect\\Documents\\upload-keystore.jks")
            storePassword = "3Kokosaetomi"
            keyAlias = "upload"
            keyPassword = "3Kokosaetomi"
        }
    }
    namespace = "com.margarin.commonweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.margarin.commonweather"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(project (":feature:search"))
    implementation(project (":feature:weather"))
    implementation(project (":network"))
    implementation(project (":database"))
    implementation(project (":core:core"))
    implementation(project (":core:theme"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:dagger:2.48.1")
    ksp("com.google.dagger:dagger-compiler:2.48.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.yandex.android:maps.mobile:4.4.0-lite")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


}