package org.example.project.domain

class MeterRepository {

    private val history = mutableMapOf<String, MutableList<MeterReading>>()

    fun getPreviousReading(service: String): MeterReading? {
        return history[service]?.lastOrNull()
    }

    fun getLastThree(service: String): List<MeterReading> {
        return history[service]?.takeLast(3) ?: emptyList()
    }

    fun saveReading(reading: MeterReading) {

        val list = history.getOrPut(reading.serviceNumber) {
            mutableListOf()
        }

        list.add(reading)
    }
}