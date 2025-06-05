package com.example.teacherconsultapp

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var summaryEditor: EditText
    private lateinit var progress: ProgressBar
    private lateinit var infoView: TextView
    private lateinit var saveButton: Button
    private var recordingPath: String? = null
    private var phone: String = ""
    private var startTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        summaryEditor = findViewById(R.id.summary_text_editor)
        progress = findViewById(R.id.summary_progress)
        infoView = findViewById(R.id.summary_info)
        saveButton = findViewById(R.id.save_button)

        recordingPath = intent.getStringExtra(EXTRA_RECORDING_PATH)
        infoView.text = recordingPath ?: ""
        phone = intent.getStringExtra(EXTRA_PHONE_NUMBER) ?: ""
        startTime = intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())

        recordingPath?.let { audioPath ->
            lifecycleScope.launch {
                progress.visibility = View.VISIBLE
                val text = withContext(Dispatchers.IO) { transcribeAudio(audioPath) }
                val summary = withContext(Dispatchers.Default) { simpleSummarize(text) }
                summaryEditor.setText(summary)
                progress.visibility = View.GONE
            }
        }

        saveButton.setOnClickListener {
            val text = summaryEditor.text.toString()
            val saved = saveLog(text)
            if (saved) {
                finish()
            }
        }
    }

    private fun transcribeAudio(path: String): String {
        // Placeholder for offline STT implementation using Vosk or SpeechRecognizer
        // Returning dummy text for now
        return "통화 내용 텍스트 예시"
    }

    private fun simpleSummarize(text: String): String {
        val sentences = text.split(".")
        return sentences.take(2).joinToString(".")
    }

    private fun saveLog(text: String): Boolean {
        return try {
            val dir = getExternalFilesDir(null) ?: filesDir
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "${timeStamp}_consult_log.txt"
            val file = File(dir, fileName)
            file.writeText(text)

            val name = lookupContactName(phone)
            val record = CallRecord(name, phone, startTime, recordingPath ?: "", file.absolutePath)
            CallRecordStore.add(this, record)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun lookupContactName(number: String): String {
        if (number.isBlank()) return ""
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY
        )
        val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER}=?"
        val cursor = contentResolver.query(uri, projection, selection, arrayOf(number), null)
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(0) ?: number
            }
        }
        return number
    }

    companion object {
        const val EXTRA_RECORDING_PATH = "recording_path"
        const val EXTRA_PHONE_NUMBER = "phone_number"
        const val EXTRA_START_TIME = "start_time"
    }
}
