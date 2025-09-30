package com.jclemus.androidsamples.ui.data

import com.jclemus.androidsamples.ui.data.PropertyFilter

data class PropertyFilter(
    val name: String,
    val value: String,
    val subFilters: List<PropertyFilter> = emptyList()
)

class AirbnbFilterSystem {

    private val filterHierarchy = PropertyFilter(
        name = "Root",
        value = "",
        subFilters = listOf(
            PropertyFilter("Property Type", "", subFilters = listOf(
                PropertyFilter("Entire place", "entire", subFilters = listOf(
                    PropertyFilter("House", "house"),
                    PropertyFilter("Apartment", "apartment"),
                    PropertyFilter("Condo", "condo")
                )),
                PropertyFilter("Private room", "private", subFilters = listOf(
                    PropertyFilter("Bedroom", "bedroom"),
                    PropertyFilter("Living room", "living")
                )),
                PropertyFilter("Shared room", "shared")
            )),
            PropertyFilter("Amenities", "", subFilters = listOf(
                PropertyFilter("Essentials", "", subFilters = listOf(
                    PropertyFilter("Wifi", "wifi"),
                    PropertyFilter("Kitchen", "kitchen"),
                    PropertyFilter("Air conditioning", "ac")
                )),
                PropertyFilter("Features", "", subFilters = listOf(
                    PropertyFilter("Pool", "pool"),
                    PropertyFilter("Gym", "gym"),
                    PropertyFilter("Parking", "parking")
                ))
            ))
        )
    )

    fun findFilterBruteForce(searchIterm: String): List<List<String>> {
        val allPaths = mutableListOf<List<String>>()

        val foundFilters = findInLevel(filterHierarchy.subFilters, 0, searchIterm)
        foundFilters.forEach { filter ->
            val path = findPathToFilter(filter.name)
            if (path.)
        }

    }

    private fun findInLevel(filters: List<PropertyFilter>, level: Int, searchTerm: String): List<PropertyFilter> {
        val found = mutableListOf<PropertyFilter>()
        for (filter in filters) {
            if (filter.name.contains(searchTerm, ignoreCase = true)) {
                found.add(filter)
            }

            found.addAll(findInLevel(filter.subFilters, level + 1, searchTerm))
        }

        return found
    }


    private fun findPathToFilter(name: String) {

    }

}