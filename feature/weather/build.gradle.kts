plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.margarin.weather"
    compileSdk = 34

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        buildConfig = true
    }
}

dependencies {

    implementation(project (":network"))
    implementation(project (":database"))
    implementation(project (":core:core"))
    implementation(project (":core:theme"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:dagger:2.48.1")
    ksp("com.google.dagger:dagger-compiler:2.48.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")


    implementation("com.arkivanov.mvikotlin:mvikotlin:3.3.0")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.3.0")
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:3.3.0")
    implementation("com.arkivanov.decompose:decompose:2.2.2")
    implementation("com.arkivanov.decompose:extensions-compose-jetpack:2.2.2")

    testImplementation ("org.mockito:mockito-core:5.9.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:5.2.1")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0-RC2")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}