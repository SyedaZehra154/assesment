package org.example.project.domain

class ElectricityCalculator(private val slabs: List<Slab>) {

    fun calculate(units: Int): Double {

        var remaining = units
        var total = 0.0

        for (slab in slabs) {

            if (remaining <= 0) break

            val slabUnits =
                if (slab.maxUnit == null)
                    remaining
                else
                    minOf(remaining, slab.maxUnit - slab.minUnit + 1)

            total += slabUnits * slab.rate
            remaining -= slabUnits
        }

        return total
    }
}