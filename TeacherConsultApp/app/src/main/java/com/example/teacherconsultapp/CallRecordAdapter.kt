package com.example.teacherconsultapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File

class CallRecordAdapter(
    private var records: List<CallRecord>,
    private val onClick: (CallRecord) -> Unit
) : RecyclerView.Adapter<CallRecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record)
        holder.itemView.setOnClickListener { onClick(record) }
    }

    override fun getItemCount(): Int = records.size

    fun update(newRecords: List<CallRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val infoView: TextView = itemView.findViewById(R.id.record_info)
        private val summaryView: TextView = itemView.findViewById(R.id.record_summary)

        fun bind(record: CallRecord) {
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                .format(Date(record.startTime))
            infoView.text = "${record.name} (${record.phoneNumber})\n$time"
            val file = record.logPath?.let { File(it) }
            val summaryLine = file?.takeIf { it.exists() }?.readLines()?.firstOrNull() ?: ""
            summaryView.text = summaryLine
        }
    }
}
