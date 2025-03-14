# Module 5: Performance Optimization

## Introduction 

Proyek ini berfokus pada optimasi performa aplikasi Spring Boot dengan menggunakan performance testing melalui JMeter dan profiling dengan IntelliJ Profiler. Tujuannya adalah untuk mengidentifikasi dan menyelesaikan bottleneck dalam aplikasi, sehingga meningkatkan waktu CPU dan performa secara keseluruhan.

## Tutorial and Exercise

### Performance Testing 

#### JMeter Apache GUI

##### /all-student-name

| Listener Name | Result Image | 
| ------------- | -----------  |
| View Results in Table | ![test_plan2_before.png](gambar2/test_plan2_before.png) | 


##### /highest-gpa

| Listener Name | Result Image | 
| ------------- | -----------  |
| View Results in Table | ![test_plan3_before.png](gambar2/test_plan3_before.png) | 


#### Command Line

##### /all-student-name

Log File Results

![terminal testplan2.png](gambar2/terminal%20testplan2.png)

##### /highest-gpa

Log File Results

![terminal testplan3.png](gambar2/terminal%20testplan3.png)

### Profiling 

#### Optimizations Implemented 

1. **Optimization of `findStudentWithHighestGpa()` Method**
   - Issue: Metode ini mengambil semua data student dan memproses mereka di Java, yang tidak efisien untuk dataset besar.
   - Solution: Menggunakan SQL query langsung untuk mendapatkan student dengan GPA tertinggi.
   ```java
   @Query(value = "SELECT * FROM students WHERE gpa = (SELECT MAX(gpa) FROM students) LIMIT 1", nativeQuery = true)
   Optional<Student> findStudentWithHighestGpa();
   ```
   - Result: Waktu eksekusi berkurang signifikan karena filtering dilakukan di level database.

2. **Optimization of `joinStudentNames()` Method**
   - Issue: Metode ini menggunakan string concatenation yang tidak efisien dengan operator `+=`.
   - Solution: Menggunakan SQL query dengan fungsi STRING_AGG untuk menggabungkan nama student.
   ```java
   @Query("SELECT STRING_AGG(s.name, ', ') FROM Student s")
   String joinStudentNames();
   ```
   - Result: Waktu eksekusi berkurang drastis karena operasi string dilakukan di level database.

3. **Optimization of `getAllStudentsWithCourses()` Method**
   - Issue: Metode ini mengambil semua student dan kemudian melakukan query terpisah untuk setiap student.
   - Solution: Menggunakan JOIN FETCH untuk mengambil data student dan course sekaligus.
   ```java
   @Query("SELECT sc FROM StudentCourse sc JOIN FETCH sc.student JOIN FETCH sc.course")
   List<StudentCourse> findAllStudentsWithCourses();
   ```
   - Result: Mengurangi jumlah query ke database dan meningkatkan performa secara keseluruhan.

#### JMeter Apache Optimization Comparison

##### /all-student-name

| Listener Name | Result Image Before | Result Image After |
| ------------- | ------------------  | ------------------ |
| View Results Tree | ![test_plan2_before.png](gambar2/test_plan2_before.png) | ![test_plan2_after.png](gambar2/test_plan2_after.png) |

##### /highest-gpa

| Listener Name | Result Image Before | Result Image After |
| ------------- | ------------------  | ------------------ |
| View Results Tree | ![test_plan3_before.png](gambar2/test_plan3_before.png) | ![test_plan3_after.png](gambar2/test_plan3_after.png) |


Dengan membandingkan hasil sebelum dan sesudah optimasi pada endpoint **/all-student-name** dan **/highest-gpa**, terlihat adanya peningkatan performa yang signifikan. Melalui profiling dan performance testing, aplikasi mengalami peningkatan performa yang cukup besar. Dengan mengoptimalkan operasi string dan query database, waktu eksekusi berkurang drastis seperti yang terlihat pada Summary Report, sehingga memastikan efisiensi dan skalabilitas yang lebih baik.

### Reflection

1. **Apa perbedaan antara pendekatan performance testing dengan JMeter dan profiling dengan IntelliJ Profiler dalam konteks optimasi performa aplikasi?**
   - JMeter: Berfokus pada pengujian beban (load testing) dengan mensimulasikan banyak pengguna yang mengakses aplikasi secara bersamaan untuk mengukur respons sistem secara keseluruhan.
   - IntelliJ Profiler: Berfokus pada analisis kode tingkat rendah untuk mengidentifikasi metode atau bagian kode yang menjadi bottleneck dengan mengukur waktu CPU, penggunaan memori, dan alokasi objek.
   - Perbedaan Utama: JMeter melihat performa dari perspektif pengguna akhir (end-to-end), sementara IntelliJ Profiler melihat performa dari perspektif internal kode aplikasi.

2. **Bagaimana proses profiling membantu Anda dalam mengidentifikasi dan memahami titik lemah dalam aplikasi Anda?**
   - Profiling membantu mengidentifikasi metode yang memakan waktu CPU paling banyak, seperti yang terlihat pada metode `joinStudentNames()` dan `findStudentWithHighestGpa()`.
   - Dengan flame graph, saya dapat melihat secara visual bagian kode mana yang menghabiskan waktu eksekusi terbanyak.
   - Profiling juga membantu mengidentifikasi operasi yang tidak efisien seperti penggunaan string concatenation dan pengambilan data yang tidak perlu dari database.
   - Tanpa profiling, bottleneck ini sulit diidentifikasi karena tidak terlihat langsung dari kode.

3. **Apakah menurut Anda IntelliJ Profiler efektif dalam membantu menganalisis dan mengidentifikasi bottleneck dalam kode aplikasi Anda?**
   - Ya, IntelliJ Profiler sangat efektif karena:
     - Memberikan visualisasi yang jelas tentang penggunaan CPU per metode
     - Memungkinkan drill-down ke metode spesifik untuk melihat detail eksekusi
     - Mudah digunakan dan terintegrasi dengan IDE yang sudah saya gunakan
     - Memungkinkan perbandingan hasil profiling sebelum dan sesudah optimasi
     - Memberikan informasi yang cukup detail untuk mengidentifikasi masalah performa tanpa terlalu kompleks

4. **Apa tantangan utama yang Anda hadapi saat melakukan performance testing dan profiling, dan bagaimana Anda mengatasi tantangan tersebut?**
   - Tantangan: Hasil JMeter terkadang tidak konsisten dan berbeda dengan hasil di Postman.
   - Solusi: Melakukan beberapa kali pengujian dan mengambil rata-rata hasil untuk mendapatkan gambaran yang lebih akurat.
   
   - Tantangan: Mengidentifikasi apakah bottleneck ada di kode Java atau di query database.
   - Solusi: Menggunakan kombinasi profiling CPU dan monitoring query database untuk melihat keduanya.
   
   - Tantangan: Memastikan optimasi tidak mengubah fungsionalitas aplikasi.
   - Solusi: Membuat test case untuk memvalidasi hasil sebelum dan sesudah optimasi.

5. **Apa manfaat utama yang Anda dapatkan dari menggunakan IntelliJ Profiler untuk profiling kode aplikasi Anda?**
   - Kemudahan penggunaan: Terintegrasi dengan IDE yang sudah saya gunakan sehari-hari.
   - Visualisasi yang jelas: Flame graph dan hot spots memudahkan identifikasi bottleneck.
   - Detail yang cukup: Memberikan informasi CPU time per metode yang sangat berguna.
   - Real-time monitoring: Dapat melihat perubahan performa saat aplikasi berjalan.
   - Perbandingan hasil: Dapat membandingkan hasil profiling sebelum dan sesudah optimasi.
   - Tidak memerlukan konfigurasi tambahan: Dapat langsung digunakan tanpa setup yang rumit.

6. **Bagaimana Anda menangani situasi di mana hasil dari profiling dengan IntelliJ Profiler tidak sepenuhnya konsisten dengan temuan dari performance testing menggunakan JMeter?**
   - Menganalisis perbedaan antara CPU time vs response time: IntelliJ Profiler mengukur CPU time, sementara JMeter mengukur response time yang mencakup network latency dan I/O.
   - Mempertimbangkan faktor eksternal: Database, network, dan resource sistem lainnya yang mungkin mempengaruhi hasil JMeter tapi tidak terlihat di profiler.
   - Melakukan pengujian berulang: Menjalankan beberapa kali test untuk melihat pola dan tren.
   - Menggunakan pendekatan holistik: Tidak hanya bergantung pada satu alat, tapi menggunakan kombinasi profiling, monitoring database, dan performance testing.
   - Fokus pada perbaikan relatif: Meskipun angka absolut berbeda, perbaikan relatif (persentase) seharusnya terlihat di kedua alat.

7. **Strategi apa yang Anda terapkan dalam mengoptimalkan kode aplikasi setelah menganalisis hasil dari performance testing dan profiling? Bagaimana Anda memastikan perubahan yang Anda buat tidak mempengaruhi fungsionalitas aplikasi?**
   - Strategi optimasi:
     - Mengoptimalkan satu bottleneck pada satu waktu dan mengukur dampaknya
     - Memindahkan logika dari Java ke SQL untuk operasi yang melibatkan data dalam jumlah besar
     - Menggunakan teknik caching untuk data yang sering diakses
     - Mengganti operasi string yang tidak efisien dengan metode yang lebih optimal
     - Mengurangi jumlah query database dengan menggunakan JOIN dan fetch yang tepat
   
   - Memastikan fungsionalitas tetap terjaga:
     - Membuat unit test sebelum melakukan optimasi
     - Membandingkan hasil sebelum dan sesudah optimasi
     - Melakukan pengujian fungsional manual untuk endpoint yang dioptimasi
     - Menerapkan perubahan secara bertahap dan melakukan validasi di setiap tahap
     - Menggunakan version control untuk dapat kembali ke versi sebelumnya jika terjadi masalah