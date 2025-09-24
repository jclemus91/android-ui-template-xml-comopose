package com.jclemus.androidsamples.data

import com.jclemus.androidsamples.domain.Contact

class ContactRepository {

    private val contacts = listOf(
        Contact(
            id = 1,
            name = "Julio Lemus",
            email = "julio@gmail.com",
            phone = "3333333333",
            company = "Airbnb"
        ),
        Contact(
            id = 2,
            name = "Carla Souza",
            email = "carla@gmail.com",
            phone = "3366772288",
            company = "Globant"
        ),
        Contact(
            id = 3,
            name = "Homer Simpson",
            email = "baaaart@gmail.com",
            phone = "1212121212",
            company = "Nuclear"
        ),
        Contact(
            id = 4,
            name = "Moe",
            email = "moe@gmail.com",
            phone = "992290933949",
            company = "Moe's Tavern"
        )
    )

    fun getAll(): List<Contact> = contacts

    fun getFilteredContacts(query: String): List<Contact> {
        return contacts.filter { contact ->
            contact.name.contains(query, ignoreCase = true) ||
                    contact.email.contains(query, ignoreCase = true) ||
                    contact.company.contains(query, ignoreCase = true)
        }
    }

    fun getContactById(id: Long): Contact? = contacts.find { it.id == id }
}