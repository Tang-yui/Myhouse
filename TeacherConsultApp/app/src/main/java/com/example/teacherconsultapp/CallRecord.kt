package com.example.teacherconsultapp

/**
 * Represents a single recorded call and its related summary log.
 */
data class CallRecord(
    val name: String,
    val phoneNumber: String,
    val startTime: Long,
    val recordingPath: String,
    val logPath: String?
)
