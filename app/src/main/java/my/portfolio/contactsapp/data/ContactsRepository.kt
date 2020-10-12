package my.portfolio.contactsapp.data

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import my.portfolio.contactsapp.data.models.Contact


class ContactsRepository {

    fun fetchContacts(activity: Activity): ArrayList<Contact> {
        val contactsList = getPhoneContacts(activity)
        val contactNumbers = getContactNumbers(activity)
        val contactEmail = getContactEmails(activity)


        contactsList.forEach {
            contactNumbers[it.id]?.let { numbers ->
                it.numbers = numbers
            }
            contactEmail[it.id]?.let { emails ->
                it.emails = emails
            }
        }
        return contactsList
    }

    private fun getPhoneContacts(activity: Activity): ArrayList<Contact> {
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
            val imageUriIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                val imageUri = contactsCursor.getString(imageUriIndex)
                if (name != null) {
                    contactsList.add(
                        Contact(
                            id,
                            name,
                            imageUri
                        )
                    )
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(activity: Activity): Map<String, ArrayList<String>> {
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

    private fun getContactEmails(activity: Activity): Map<String, ArrayList<String>> {
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

    fun addContact(activity: Activity, name: String, number: String): Boolean {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        intent.apply {
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
            putExtra(ContactsContract.Intents.Insert.NAME, name)
            putExtra(
                ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK
            )
            activity.startActivityForResult(this, 1)

        }
        return true

    }

    fun deleteContact(activity: Activity, contact: Contact): Boolean {
        val contactUri: Uri =
            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contact.numbers[0]))
        val cur: Cursor? = activity.contentResolver
            .query(contactUri, null, null, null, null)
            ?.apply {
                try {
                    if (moveToFirst()) {
                        do {
                            if (getString(getColumnIndex(PhoneLookup.DISPLAY_NAME))
                                    .equals(contact.name, ignoreCase = true)
                            ) {
                                val lookupKey =
                                    getString(getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                                val uri: Uri = Uri.withAppendedPath(
                                    ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                    lookupKey
                                )
                                activity.contentResolver.delete(uri, null, null)
                                return true
                            }
                        } while (moveToNext())
                    }
                } catch (e: Exception) {
                    println(e.stackTrace)
                } finally {
                    close()
                }
            }
        cur?.close()

        return false
    }

    fun editContact() {}
}