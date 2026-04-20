plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.agritechda3k"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.agritechda3k"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Thêm vào phần dependencies thư vien retrofit2
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0") // Dùng để đọc chuỗi String đơn giản

    // 2. Room Database - Lưu trữ dữ liệu dưới máy
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Hỗ trợ suspend functions cho DAO
    ksp("androidx.room:room-compiler:$room_version")      // Dùng KSP thay vì KAPT
    // 3. Lifecycle - Quản lý trạng thái UI (ViewModel, LiveData)
    val lifecycle_version = "2.10.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Khuyên dùng thêm: Để quản lý vòng đời trong Activity/Fragment dễ hơn
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // 1. Coroutines - Xử lý bất đồng bộ
    val coroutines_version = "1.10.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // Thêm 2 dòng này để dùng được 'by viewModels()'
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.activity:activity-ktx:1.8.2")
    // Thư viện Glide để tải ảnh từ URL
    implementation(libs.glide)
    //toolbar
    implementation("com.google.android.material:material:1.11.0")
    // Các thư viện cũ hơn nếu có
    implementation("androidx.appcompat:appcompat:1.6.1")
}