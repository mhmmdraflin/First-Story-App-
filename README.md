

---

# 📱 First Story App

**First Story App** adalah aplikasi Android yang memungkinkan pengguna untuk membagikan cerita dalam bentuk teks dan foto. Aplikasi ini menyediakan fitur login, register, menampilkan daftar cerita, serta menambah cerita baru. Dengan desain modern, custom view, serta animasi yang halus, aplikasi ini dirancang sebagai bagian dari pengembangan aplikasi Android menggunakan prinsip *Clean Architecture* dan *Jetpack Libraries*.

---

## ✨ Fitur Utama

### 🔐 Autentikasi Pengguna

* **Login** dan **Register** terintegrasi dengan **Story API**
* Validasi input yang informatif dan ramah pengguna
* Menampilkan pesan kesalahan bila login/register gagal

### 🎨 Custom View

* `EditText` khusus dengan border dan ikon sesuai kriteria
* Dapat digunakan ulang untuk berbagai form di aplikasi

### 💾 Penyimpanan Data Sesi

* Data token dan nama pengguna disimpan menggunakan **DataStore Preferences**
* Otentikasi otomatis saat membuka aplikasi jika sudah login

### 🚪 Logout

* Tombol **Logout** untuk menghapus data sesi pengguna
* Setelah logout, pengguna diarahkan kembali ke halaman login

### 🗂️ Daftar Cerita

* Menampilkan cerita pengguna dari **Story API**
* Gambar, nama pengguna, dan deskripsi ditampilkan dalam card
* Menggunakan `RecyclerView` dengan desain responsif

### 📄 Detail Cerita

* Saat salah satu item cerita diklik, akan muncul halaman **DetailStoryActivity**
* Menampilkan gambar, nama, waktu, dan deskripsi cerita secara lengkap

### ➕ Tambah Cerita

* Fitur untuk **mengunggah cerita baru** dengan foto dan deskripsi
* Bisa mengambil gambar dari kamera atau galeri
* Mendukung pengambilan lokasi untuk cerita

### 🎞️ Animasi

* Animasi transisi halaman menggunakan **Material SharedAxis**
* Efek fade dan slide di halaman login/register dan tambah cerita
* Efek loading ketika daftar cerita sedang dimuat

---

## 🧩 Teknologi yang Digunakan

| Komponen           | Teknologi/Library                 |
| ------------------ | --------------------------------- |
| Bahasa Pemrograman | Kotlin                            |
| Arsitektur         | MVVM                              |
| UI                 | Material Design, XML Layout       |
| Networking         | Retrofit, OkHttp                  |
| State & Lifecycle  | ViewModel, LiveData               |
| Persistensi Data   | DataStore Preferences             |
| Animasi            | Material Motion, View Animation   |
| Navigasi           | Intent, Shared Element Transition |

---

## 📷 Screenshot Aplikasi

| Login                                                    | Daftar Cerita                                                | Detail Cerita                                                    | Tambah Cerita                                              |
| -------------------------------------------------------- | ------------------------------------------------------------ | ---------------------------------------------------------------- | ---------------------------------------------------------- |
| ![Login](https://via.placeholder.com/200x400?text=Login) | ![List](https://via.placeholder.com/200x400?text=List+Story) | ![Detail](https://via.placeholder.com/200x400?text=Detail+Story) | ![Add](https://via.placeholder.com/200x400?text=Add+Story) |

---

## 🛠️ Cara Menjalankan Aplikasi

1. Clone repositori ini:

   ```bash
   git clone https://github.com/username/first-story-app.git
   ```
2. Buka di **Android Studio**.
3. Jalankan aplikasi menggunakan emulator atau perangkat fisik.
4. Pastikan koneksi internet aktif karena aplikasi membutuhkan akses API.

---

## 📌 Catatan Penting

* Aplikasi menggunakan endpoint API publik dari \[Dicoding API - Story App].
* Lokasi dan gambar disimpan dengan aman dan sesuai ketentuan privasi.
* Perlu mengaktifkan izin lokasi dan kamera untuk fitur tertentu.

---

## 👨‍💻 Developer

* **Nama**: Muhammad Rafli Nurfathan
* **Email**: [nurfathanrafli85@gmail.com](mailto:nurfathanrafli85@gmail.com)
* **LinkedIn**: [linkedin.com/in/mhmmdraflin](https://www.linkedin.com/in/mhmmdraflin)

---


