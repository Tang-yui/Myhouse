package com.example.teacherconsultapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.teacherconsultapp.CallRecordAdapter
import com.example.teacherconsultapp.CallRecordStore
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = mutableListOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE
    ).apply {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()

    private lateinit var recordAdapter: CallRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasAllPermissions()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSION_REQUEST_CODE)
        } else {
            startCallService()
        }

        val recycler = findViewById<RecyclerView>(R.id.recycler_recent_calls)
        recycler.layoutManager = LinearLayoutManager(this)
        recordAdapter = CallRecordAdapter(emptyList()) { record ->
            val intent = Intent(this, RecordDetailActivity::class.java).apply {
                putExtra(RecordDetailActivity.EXTRA_RECORD_PATH, record.recordingPath)
                putExtra(RecordDetailActivity.EXTRA_LOG_PATH, record.logPath)
                putExtra(RecordDetailActivity.EXTRA_START_TIME, record.startTime)
                putExtra(RecordDetailActivity.EXTRA_PHONE_NUMBER, record.phoneNumber)
            }
            startActivity(intent)
        }
        recycler.adapter = recordAdapter

        findViewById<FloatingActionButton>(R.id.fab_new_call).setOnClickListener {
            startActivity(Intent(this, DialerActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val records = CallRecordStore.load(this)
        recordAdapter.update(records)
    }

    private fun hasAllPermissions(): Boolean {
        return requiredPermissions.all { perm ->
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "앱 사용을 위해 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            } else {
                startCallService()
            }
        }
    }

    private fun startCallService() {
        ContextCompat.startForegroundService(this, Intent(this, CallMonitorService::class.java))
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
