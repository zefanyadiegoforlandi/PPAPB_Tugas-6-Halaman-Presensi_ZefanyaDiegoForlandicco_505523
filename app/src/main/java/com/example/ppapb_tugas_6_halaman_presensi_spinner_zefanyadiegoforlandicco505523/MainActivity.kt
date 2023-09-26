package com.example.ppapb_tugas_6_halaman_presensi_spinner_zefanyadiegoforlandicco505523

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ppapb_tugas_6_halaman_presensi_spinner_zefanyadiegoforlandicco505523.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    // Mendeklarasikan variabel binding yang akan digunakan untuk mengikat tampilan XML dengan kode Kotlin.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menginisialisasi binding dengan meng-inflate layout XML yang sesuai.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Daftar opsi presensi yang akan ditampilkan dalam spinner.
        val presensiOptions = listOf("Hadir Tepat Waktu", "Sakit", "Terlambat", "Izin")

        // Membuat adapter untuk spinner yang akan digunakan untuk mengisi opsi presensi.
        val spinnerAdapter = ArrayAdapter(this@MainActivity, R.layout.spinner, presensiOptions)

        // Mendapatkan instance dari objek kalender untuk mengelola tanggal dan waktu.
        val calendar = Calendar.getInstance()

        // Format tanggal yang akan ditampilkan awalnya adalah tanggal saat ini.
        var selectedDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(calendar.time)

        // Format waktu yang akan ditampilkan awalnya adalah waktu saat ini.
        var selectedTime = decideTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

        // Mengatur adapter spinner yang telah dibuat ke dalam spinner di layout XML.
        with(binding) {
            newSpinnerId.adapter = spinnerAdapter

            // Mendengarkan perubahan seleksi pada spinner.
            newSpinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    // Mendapatkan opsi presensi yang dipilih oleh pengguna.
                    val selectedPresensi = newSpinnerId.selectedItem.toString()

                    // Menampilkan atau menyembunyikan kolom keterangan berdasarkan opsi presensi yang dipilih.
                    if (selectedPresensi == presensiOptions[1] || selectedPresensi == presensiOptions[2] || selectedPresensi == presensiOptions[3]) {
                        newKeteranganId.visibility = View.VISIBLE
                    } else {
                        newKeteranganId.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tidak melakukan apa-apa jika tidak ada opsi yang dipilih.
                }
            }

            // Mendengarkan perubahan tanggal pada DatePicker.
            newDatePickerId.setOnDateChangeListener { _, year, month, dayOfMonth ->
                // Mengatur tanggal yang dipilih ke dalam objek kalender.
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Mengubah format tanggal yang dipilih menjadi format yang sesuai dan menyimpannya dalam variabel.
                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)
            }

            // Mendengarkan perubahan waktu pada TimePicker.
            newTimePickerId.setOnTimeChangedListener { _, hourOfDay, minute ->
                // Mengubah format waktu yang dipilih menjadi format yang sesuai dan menyimpannya dalam variabel.
                selectedTime = decideTime(hourOfDay, minute)
            }

            // Mendengarkan aksi saat tombol "Submit" ditekan.
            newSubmitId.setOnClickListener {
                val selectedPresensi = newSpinnerId.selectedItem.toString()

                // Memeriksa apakah opsi presensi yang dipilih memerlukan keterangan.
                if (selectedPresensi == presensiOptions[1] || selectedPresensi == presensiOptions[2] || selectedPresensi == presensiOptions[3]) {
                    if (newKeteranganId.text.toString().isEmpty()) {
                        // Menampilkan pesan Snackbar jika kolom keterangan kosong.
                        Snackbar.make(root, "Mohon isi kolom keterangan", Snackbar.LENGTH_SHORT).setAnchorView(newSubmitId).show()
                    } else {
                        // Menampilkan pesan Snackbar jika presensi berhasil dengan tanggal dan waktu yang dipilih.
                        Snackbar.make(root, "Presensi berhasil pada $selectedDate pukul $selectedTime", Snackbar.LENGTH_SHORT).setAnchorView(newSubmitId).show()
                    }
                } else {
                    // Menampilkan pesan Snackbar jika presensi berhasil tanpa keterangan dengan tanggal dan waktu yang dipilih.
                    Snackbar.make(root, "Presensi berhasil pada $selectedDate pukul $selectedTime", Snackbar.LENGTH_SHORT).setAnchorView(newSubmitId).show()
                }
            }
        }
    }

    // Fungsi untuk menentukan format waktu yang sesuai berdasarkan jam dan menit yang dipilih.
    private fun decideTime(hourOfDay: Int, minute: Int): String {
        var selectedTime = ""
        var minutes = minute.toString()

        // Menambahkan "0" di depan menit jika menit kurang dari 10 untuk format waktu yang benar.
        if (minute < 10) {
            minutes = "0$minute"
        }

        // Menentukan apakah waktu adalah pagi atau sore/malam dan memformatnya dengan benar.
        if (hourOfDay < 12) {
            selectedTime = "$hourOfDay:$minutes AM"
        } else if (hourOfDay > 12) {
            selectedTime = "${hourOfDay % 12}:$minutes PM"
        } else {
            if (minute > 0) {
                selectedTime = "${hourOfDay % 12}:$minutes PM"
            } else if (minute == 0) {
                selectedTime = "$hourOfDay:$minutes AM"
            }
        }
        return selectedTime
    }
}
