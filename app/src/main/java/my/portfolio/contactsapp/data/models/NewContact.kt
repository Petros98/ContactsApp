package my.portfolio.contactsapp.data.models

import java.io.Serializable

data class NewContact(
    val name: String,
    val number: String
) : Serializable