package com.jclemus.androidsamples.ui.data

import java.util.ArrayDeque
import java.util.Queue

data class AirbnbProperty(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val pricePerNight: Int,
    val rating: Double,
    val nearbyProperties: List<String> = emptyList()
)

class AirbnbPropertySearchEngine {

    val properties = mapOf(
        "prop1" to AirbnbProperty("prop1", "Downtown Loft", 40.7589, -73.9851, 120, 4.8, listOf("prop2", "prop3")),
        "prop2" to AirbnbProperty(
            "prop2",
            "Cozy Studio",
            40.7614,
            -73.9776,
            95,
            4.6,
            listOf("prop1", "prop4", "prop5")
        ),
        "prop3" to AirbnbProperty("prop3", "Modern Apt", 40.7505, -73.9934, 150, 4.9, listOf("prop1", "prop6")),
        "prop4" to AirbnbProperty(
            "prop4",
            "Historic Brownstone",
            40.7505,
            -73.9857,
            200,
            4.7,
            listOf("prop2", "prop7")
        ),
        "prop5" to AirbnbProperty("prop5", "Penthouse Suite", 40.7484, -73.9857, 350, 4.9, listOf("prop2", "prop8")),
        "prop6" to AirbnbProperty("prop6", "Artist Loft", 40.7463, -73.9897, 110, 4.5, listOf("prop3")),
        "prop7" to AirbnbProperty("prop7", "Garden Apartment", 40.7444, -73.9903, 130, 4.6, listOf("prop4")),
        "prop8" to AirbnbProperty("prop8", "Luxury Condo", 40.7434, -73.9903, 280, 4.8, listOf("prop5"))
    )

    fun findNearbyPropertiesBruteForce(
        startPropertyId: String,
        maxDistance: Int
    ): Map<Int, List<AirbnbProperty>> {
        val startProperty = properties[startPropertyId] ?: return emptyMap()
        val results = mutableMapOf<Int, MutableList<AirbnbProperty>>()

        exploreProperty(startPropertyId, 0, mutableSetOf(), maxDistance, results)

        return results
    }

    private fun exploreProperty(
        currentId: String,
        distance: Int,
        visited: MutableSet<String>,
        maxDistance: Int,
        results: MutableMap<Int, MutableList<AirbnbProperty>>
    ) {
        if (distance > maxDistance || currentId in visited) return

        val current = properties[currentId] ?: return
        if (!results.containsKey(distance)) {
          results.put(distance, mutableListOf())
        }
        results[distance]?.add(current)

        visited.add(currentId)

        current.nearbyProperties.forEach { propertyId ->
            exploreProperty(propertyId, distance + 1, visited, maxDistance, results)
        }
    }


     fun findNearbyPropertiesOptimized(
        startPropertyId: String,
        maxDistance: Int
    ) : List<Pair<AirbnbProperty, Int>> {

        val queue : Queue<Pair<String, Int>> = ArrayDeque()
        val visited = mutableSetOf<String>()
        val results = mutableListOf<Pair<AirbnbProperty, Int>>()

        queue.offer(startPropertyId to 0)
        visited.add(startPropertyId)

        while (queue.isNotEmpty()) {
            val (currentId, distance) = queue.poll()

            if (distance > maxDistance) continue

            val currentProperty = properties[currentId] ?: continue
            results.add(currentProperty to distance)

            currentProperty.nearbyProperties.forEach { propertyId ->
                if (propertyId !in visited && distance + 1 <= maxDistance) {
                    visited.add(propertyId)
                    queue.add(propertyId to distance + 1)
                }
            }
        }

        return results.sortedBy { it.second }
    }

    fun findSimilarPropertiesInNeighborhood(
        target: AirbnbProperty,
        priceRange: IntRange,
        minRating: Double
    ) : List<AirbnbProperty> {

        val queue: Queue<String> = ArrayDeque()
        val visited = mutableSetOf<String>()
        val result = mutableListOf<AirbnbProperty>()

        queue.offer(target.id)
        visited.add(target.id)


        while (queue.isNotEmpty()) {
            val currentId = queue.poll() ?: continue
            val current = properties[currentId] ?: continue

            if (current.pricePerNight in priceRange && current.rating >= minRating && currentId != target.id) {
                result.add(current)
            }

            current.nearbyProperties.forEach { nearbyPropertyId ->
                if (nearbyPropertyId !in visited) {
                    visited.add(nearbyPropertyId)
                    queue.offer(nearbyPropertyId)
                }
            }
        }

        return result.sortedByDescending { it.rating }
    }
}