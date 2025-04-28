# MyMoney Notes

MyMoney Notes adalah aplikasi Android sederhana berbasis Kotlin + Jetpack Compose yang berfungsi untuk mencatat pemasukan dan pengeluaran pengguna, dilengkapi dengan tampilan grafik menggunakan MPAndroidChart.

## 📱 Fitur Utama
- Menampilkan saldo total pengguna.
- Menampilkan perbandingan income dan expense dalam bentuk bar chart.
- Menambahkan transaksi baru (Income/Expense) dengan kategori dan jumlah.
- Menampilkan daftar riwayat transaksi dengan kategori, tanggal, jumlah, dan tipe.
- Menggunakan Material Design Components untuk tampilan modern.
- Menggunakan RecyclerView untuk daftar transaksi.

## 🛠️ Teknologi & Library
- **Kotlin** untuk bahasa pemrograman utama.
- **Jetpack Compose** untuk UI deklaratif modern.
- **MPAndroidChart** untuk grafik (bar chart).
- **Material 3** dan **Material Components** untuk desain antarmuka.
- **AndroidX Lifecycle**, **Activity Compose**, dan **Core KTX** untuk manajemen lifecycle dan kemudahan Kotlin Extensions.

## 🏗️ Step-by-Step Pembuatan

1. **Setup Project**  
   - Buat project Android baru di Android Studio dengan opsi *Empty Compose Activity*.
   - Set `minSdk` ke **26**, `targetSdk` ke **35**, gunakan bahasa **Kotlin**, dan aktifkan **Jetpack Compose**.

2. **Konfigurasi Gradle**  
   - Tambahkan plugin `kotlin-android`, `kotlin-compose`, dan `android-application`.
   - Tambahkan dependencies utama di `build.gradle` (lihat bagian 📦 Dependencies di bawah).

3. **Buat Model Data**  
   - Buat class data `Transaction.kt` untuk mewakili transaksi dengan properti: kategori, tanggal, jumlah, dan tipe transaksi (INCOME atau EXPENSE).

4. **Buat UI Layout**  
   - Desain tampilan utama menggunakan Jetpack Compose (`MainActivity.kt`) yang berisi:
     - Saldo total
     - Grafik income vs expense
     - Form input untuk menambah transaksi
     - Daftar transaksi

5. **Integrasi Chart**  
   - Gunakan **MPAndroidChart** untuk membuat bar chart yang menampilkan total pemasukan dan pengeluaran.

6. **Buat Adapter RecyclerView** (Jika menggunakan RecyclerView untuk transaksi)
   - Buat `TransactionAdapter.kt` untuk menghubungkan data transaksi ke item tampilan (`item_transaction.xml`).

7. **Buat Item Layout**  
   - Buat file `item_transaction.xml` berisi tampilan satu transaksi di dalam CardView.

8. **Kelola State Transaksi**  
   - Gunakan State di Compose (seperti `mutableStateListOf`) untuk menyimpan dan memperbarui daftar transaksi secara dinamis.

9. **Testing dan Debugging**  
   - Jalankan aplikasi, tambahkan transaksi dummy, pastikan saldo, grafik, dan daftar transaksi tampil sesuai.

10. **Optimasi dan Styling**  
    - Gunakan tema Material 3 untuk tampilan lebih modern.
    - Sesuaikan warna, font, margin, dan padding agar aplikasi lebih nyaman digunakan.

---

## ⚙️ Konfigurasi Build
```groovy
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mymoneynotes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mymoneynotes"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
```

## 📦 Dependencies
- **UI dan Jetpack Compose**
  - `androidx.core:core-ktx`
  - `androidx.lifecycle:lifecycle-runtime-ktx`
  - `androidx.activity:activity-compose`
  - `androidx.compose:*` (BOM, UI, Graphics, Tooling Preview, Test)
  - `androidx.material3:material3`
  - `com.google.android.material:material`
  - `androidx.appcompat:appcompat`
  - `androidx.recyclerview:recyclerview`

- **Chart Library**
  - `com.github.PhilJay:MPAndroidChart`

- **Testing**
  - `junit:junit`
  - `androidx.test.ext:junit`
  - `androidx.test.espresso:espresso-core`
  - `androidx.compose.ui:ui-test-junit4`
  - `androidx.compose.ui:ui-tooling`
  - `androidx.compose.ui:ui-test-manifest`

## 🚀 Cara Menjalankan
1. Clone repository ini.
2. Buka project menggunakan **Android Studio** (versi terbaru disarankan).
3. Pastikan sudah menggunakan **JDK 11** dan **Gradle Plugin** terbaru.
4. Sync Gradle, lalu Build dan Run project di emulator atau perangkat fisik minimal Android 8.0 (API 26).

## 📷 Screenshot

<img alt="Thumbnail MyMoney Notes" src="https://i.imgur.com/uvLv1W5.png" width="250" />

---
