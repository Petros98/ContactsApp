package my.portfolio.contactsapp

import android.app.Activity
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel(private val activity: Activity) : ViewModel() {

    private val _contacts: MutableLiveData<ArrayList<Contact>> = MutableLiveData()
    val contacts: LiveData<ArrayList<Contact>>
        get() = _contacts

    fun fetchContacts() {
        val contactsList = getPhoneContacts()
        val contactNumbers = getContactNumbers()
        val contactEmail = getContactEmails()


        contactsList.forEach {
            contactNumbers[it.id]?.let { numbers ->
                it.numbers = numbers
            }
            contactEmail[it.id]?.let { emails ->
                it.emails = emails
            }
        }
        _contacts.postValue(contactsList)

    }

    private fun getPhoneContacts(): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val contactsCursor: Cursor? = activity.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                if (name != null) {
                    contactsList.add(Contact(id, name))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(): Map<String, ArrayList<String>> {
        val contactsNumberMap = mutableMapOf<String, ArrayList<String>>()
        val phoneCursor: Cursor? = activity.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex =
                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex =
                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            phoneCursor.close()
        }
        return contactsNumberMap
    }

    private fun getContactEmails(): Map<String, ArrayList<String>> {
        val contactsEmailMap = mutableMapOf<String, ArrayList<String>>()
        val emailCursor = activity.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (emailCursor != null && emailCursor.count > 0) {
            val contactIdIndex =
                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex =
                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (emailCursor.moveToNext()) {
                val contactId = emailCursor.getString(contactIdIndex)
                val email = emailCursor.getString(emailIndex)
                if (contactsEmailMap.containsKey(contactId)) {
                    contactsEmailMap[contactId]?.add(email)
                } else {
                    contactsEmailMap[contactId] = arrayListOf(email)
                }
            }
            emailCursor.close()
        }
        return contactsEmailMap
    }
}