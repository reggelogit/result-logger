package com.resultlogger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 100
    private val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val FILE_NAME = "results.txt"

    private lateinit var btnTen: Button
    private lateinit var btnOne: Button
    private lateinit var tvLog: TextView
    private lateinit var tvFilePath: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTen = findViewById(R.id.btnTen)
        btnOne = findViewById(R.id.btnOne)
        tvLog = findViewById(R.id.tvLog)
        tvFilePath = findViewById(R.id.tvFilePath)

        val outputFile = getOutputFile()
        tvFilePath.text = "📄 Fájl: ${outputFile.absolutePath}"

        // Load existing content on startup
        refreshLog()

        btnTen.setOnClickListener {
            writeToFile(10)
        }

        btnOne.setOnClickListener {
            writeToFile(1)
        }

        // Request storage permission for older Android versions
        requestPermissionsIfNeeded()
    }

    private fun getOutputFile(): File {
        // Use Documents directory - works on Android 9+ without permissions
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!documentsDir.exists()) {
            documentsDir.mkdirs()
        }
        return File(documentsDir, FILE_NAME)
    }

    private fun writeToFile(value: Int) {
        val file = getOutputFile()

        // Check permission for older Android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tárhely engedély szükséges!", Toast.LENGTH_SHORT).show()
                requestPermissionsIfNeeded()
                return
            }
        }

        val utcDate = getUtcTimestamp()
        val logLine = "$utcDate | $value\n"

        try {
            FileWriter(file, true).use { writer ->
                writer.write(logLine)
            }

            Toast.makeText(this, "✅ Mentve: $value", Toast.LENGTH_SHORT).show()
            refreshLog()

        } catch (e: IOException) {
            Toast.makeText(this, "❌ Hiba: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getUtcTimestamp(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    private fun refreshLog() {
        val file = getOutputFile()
        if (file.exists()) {
            try {
                val content = file.readText()
                val lines = content.trim().lines()
                // Show last 15 lines
                val display = if (lines.size > 15) {
                    "... (${lines.size - 15} korábbi sor)\n" + lines.takeLast(15).joinToString("\n")
                } else {
                    content
                }
                tvLog.text = if (content.isBlank()) "(üres fájl)" else display
            } catch (e: Exception) {
                tvLog.text = "(nem olvasható)"
            }
        } else {
            tvLog.text = "(fájl még nem létezik)"
        }
    }

    private fun requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "✅ Engedély megadva!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "❌ Engedély megtagadva – írás nem lehetséges", Toast.LENGTH_LONG).show()
            }
        }
    }
}
