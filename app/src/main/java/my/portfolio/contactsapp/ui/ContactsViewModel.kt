package my.portfolio.contactsapp.ui

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.portfolio.contactsapp.data.ContactsRepository
import my.portfolio.contactsapp.data.models.Contact

class ContactsViewModel : ViewModel() {

    private val contactsRepository: ContactsRepository by lazy { ContactsRepository() }

    private val _contacts: MutableLiveData<ArrayList<Contact>> = MutableLiveData()
    val contacts: LiveData<ArrayList<Contact>>
        get() = _contacts

    private val _navigateToSelectedProperty = MutableLiveData<Contact>()
    val navigateToSelectedProperty: LiveData<Contact>
        get() = _navigateToSelectedProperty

    fun fetchContacts(activity: Activity) {
        _contacts.postValue(contactsRepository.fetchContacts(activity))
    }

    fun deleteContact(activity: Activity, contact: Contact): Boolean {
        val res = contactsRepository.deleteContact(activity, contact)
        if (res) {
            _contacts.value?.remove(contact)
        }
        return res
    }

    fun displayContactDetails(contact: Contact) {
        _navigateToSelectedProperty.value = contact
    }

    fun displayDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun addContact(activity: Activity, name: String, number: String) =
        contactsRepository.addContact(activity, name, number)

}