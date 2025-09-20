package com.jclemus.ui_app.data

import com.jclemus.ui_app.domain.Home
import com.jclemus.ui_app.domain.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SectionsRepository {

    private val sections = mutableListOf<Section>().apply {
        addAll(
        List(10) { sectionIndex ->
            Section(
                id = sectionIndex.toLong(),
                title = "Recommended for you $sectionIndex",
                homes = List(10) { homeIndex ->
                    Home(
                        id = homeIndex.toLong(),
                        imageUrl = "",
                        title = "Home $homeIndex",
                        price = "$ 1,348 MXN",
                        description = "for 2 nights",
                        rating = 4.99f
                    )

                }
            )
        }
        )
    }

    fun getSections(q: String): Flow<List<Section>> {
        return if (q.isBlank()) {
            flowOf(sections)
        } else {
            flow {
                val filtered: List<Section> = ArrayList(sections)
                    .map { section ->
                        val newHomes: List<Home> = section.homes.filter {  home ->
                            home.title.contains(q)
                        }
                        section.copy(homes = newHomes)

                    }

                emit(filtered)
            }
        }
    }

    fun getHome(id: Long): Home? {
        return sections.flatMap {
            it.homes
        }.firstOrNull { it.id == id }
    }

}

