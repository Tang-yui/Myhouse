package com.example.teacherconsultapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * Simple persistence helper that stores call records as JSON.
 */
object CallRecordStore {
    private const val FILE_NAME = "call_records.json"
    private val gson = Gson()

    private fun file(context: Context) = File(context.filesDir, FILE_NAME)

    fun load(context: Context): MutableList<CallRecord> {
        val f = file(context)
        if (!f.exists()) return mutableListOf()
        return try {
            val type = object : TypeToken<List<CallRecord>>() {}.type
            gson.fromJson<List<CallRecord>>(f.readText(), type).toMutableList()
        } catch (_: Exception) {
            mutableListOf()
        }
    }

    fun add(context: Context, record: CallRecord) {
        val list = load(context)
        list.add(0, record)
        save(context, list)
    }

    fun save(context: Context, records: List<CallRecord>) {
        val f = file(context)
        f.writeText(gson.toJson(records))
    }
}
