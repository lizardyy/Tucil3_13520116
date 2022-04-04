# Tugas Kecil 3 IF2211 Strategi Algoritma Semester II tahun 2021/2022

## Penyelesaian Persoalan 15-Puzzle dengan Algoritma Branch and Bound

Pada Tucil ini akan dibuat program untuk menyelesaikan persoalan 15-Puzzle dengan menggunakan Algoritma Branch and Bound seperti pada materi kuliah. Nilai bound tiap simpul adalah penjumlahan cost yang diperlukan untuk sampai suatu simpul x dari akar, dengan taksiran cost simpul x untuk sampai ke goal. Taksiran cost yang digunakan adalah jumlah ubin tidak kosong yang tidak berada pada tempat sesuai susunan akhir (goal state).Untuk semua instansiasi persoalan 15-puzzle, Program harus dapat menentukan apakah posisi awal suatu masukan dapat diselesaikan hingga mencapai susunan akhir, dengan mengimplementasikan fungsi Kurang(i) dan posisi ubin kosong di kondisi awal (X), seperti pada materi kuliah. Jika posisi awal tidak bisa mencapai susunan akhir, program akan menampilkan pesan tidak bisa diselesaikan,. Jika dapat diselesaikan, program dapat menampilkan urutan matriks rute (path) aksi yang dilakukan dari posisi awal ke susunan akhir.


## Disusun Oleh

```
Nama  : Mahesa Lizardy
NIM   : 13520116
Kelas : 2
```

## Cara Penggunaan

lakukan command berikut pada terminal, pertama masuk ke dalam folder bin

```
cd bin 
```
kemudian lakukan command berikut untuk menjalankan program
```
java Main
```

setelah itu akan ditampilkan gui fifteen puzzle, untuk menjalankan GUI dapat melakukan Command pada terminal, berikut daftar command yang tersedia
```
COMMAND LIST
==================================================
RANDOM      : Melakukan pengacakan pada puzzle
INPUTTEST   : Melakukan input puzzle dari file di dalam folder test
SOLUTION    : Menjalankan Solusi dari puzle tersebut
QUIT        : Keluar dari program
HELP        : Menampilkan daftar command yang ada
```

## Meng*compile* Ulang

lakukan command berikut pada terminal, pertama masuk ke dalam folder bin

```
cd src
```
kemudian lakukan command berikut untuk meng*compile* ulang
```
javac Main.java fifteenPuzzle.java State.java
```

jalankan **java Main** pada terminal direktori src, jika ingin menjalankan program hasil compile yang baru

# !!!!Tentang Kode!!!!

Pada program jika ingi melakukan command **INPUTTEST** perlu diperhatikan file test yang bisa dibaca hanya pada folder test. Kemudian, setiap pergerakan ubin dari puzzle pada gui diberikan delay 1 detik setiap move nya. Pastikan hal tersebut sudah selesai (command SOLUTION sudah berakhir), sebelum memberikan command lainnya
