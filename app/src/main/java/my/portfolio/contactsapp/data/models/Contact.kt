package my.portfolio.contactsapp.data.models

import java.io.Serializable


data class Contact(
    val id: String?,
    val name: String = "",
    val imageUri: String?
) : Serializable {
    var numbers = ArrayList<String>()
    var emails = ArrayList<String>()
    var primaryNumber: String = numbers.firstOrNull() ?: ""
}