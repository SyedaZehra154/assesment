package org.example.project.domain

import kotlinx.serialization.Serializable

@Serializable
data class MeterReading(
    val serviceNumber: String,
    val reading: Int,
    val cost: Double
)