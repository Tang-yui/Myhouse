package com.example.teacherconsultapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.lifecycleScope
import com.example.teacherconsultapp.CallRecordStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialerActivity : AppCompatActivity() {

    private lateinit var numberInput: EditText
    private lateinit var searchInput: EditText
    private lateinit var adapter: ContactAdapter
    private lateinit var callButton: Button
    private lateinit var historyButton: Button
    private val recordingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val recording = intent?.getBooleanExtra(CallMonitorService.EXTRA_IS_RECORDING, false) ?: false
            updateRecordingUI(recording)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer)

        numberInput = findViewById(R.id.dialer_number_input)
        searchInput = findViewById(R.id.contact_search_edittext)
        val recyclerView: RecyclerView = findViewById(R.id.contact_list_recyclerview)
        callButton = findViewById(R.id.call_button)
        historyButton = findViewById(R.id.history_button)

        adapter = ContactAdapter(emptyList()) { contact ->
            numberInput.setText(contact.phoneNumber)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener { text ->
            loadContacts(text.toString())
        }

        callButton.setOnClickListener { initiateCall() }
        historyButton.setOnClickListener { showHistory() }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(recordingReceiver, IntentFilter(CallMonitorService.ACTION_RECORDING_STATUS))

        loadContacts("")
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(recordingReceiver)
        super.onDestroy()
    }

    private fun initiateCall() {
        val phoneNumber = numberInput.text.toString()
        if (phoneNumber.isBlank()) {
            Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = android.content.Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        } else {
            Toast.makeText(this, "전화 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContacts(query: String) {
        lifecycleScope.launch {
            val contacts = withContext(Dispatchers.IO) { queryContacts(query) }
            adapter.update(contacts)
        }
    }

    private fun queryContacts(query: String): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val selection = if (query.isNotBlank())
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY} LIKE ?" else null
        val selectionArgs = if (query.isNotBlank()) arrayOf("%$query%") else null

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
            val numberIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIdx) ?: ""
                val number = it.getString(numberIdx) ?: ""
                contactList.add(Contact(name, number))
            }
        }
        return contactList
    }

    private fun updateRecordingUI(recording: Boolean) {
        callButton.text = if (recording) {
            "녹음 중..."
        } else {
            "전화 걸기"
        }
    }

    private fun showHistory() {
        val phone = numberInput.text.toString()
        if (phone.isBlank()) {
            Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }
        val records = CallRecordStore.load(this).filter { it.phoneNumber == phone }
        if (records.isEmpty()) {
            Toast.makeText(this, "기록이 없습니다", Toast.LENGTH_SHORT).show()
            return
        }
        val items = records.map {
            val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.US)
                .format(java.util.Date(it.startTime))
            val firstLine = it.logPath?.let { path ->
                val f = java.io.File(path)
                if (f.exists()) f.readLines().firstOrNull() else ""
            } ?: ""
            "$date\n$firstLine"
        }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("이전 기록")
            .setItems(items, null)
            .setPositiveButton("닫기", null)
            .show()
    }
}
