package my.portfolio.contactsapp.data.models

import java.io.Serializable


data class Contact(
    val id: String?,
    val name: String = "",
    val imageUri: String?
) : Serializable {
    var primaryNumber: String = ""
    var numbers = ArrayList<String>()
    var emails = ArrayList<String>()
}