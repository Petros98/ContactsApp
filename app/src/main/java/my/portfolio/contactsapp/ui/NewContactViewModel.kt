package my.portfolio.contactsapp.ui

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.portfolio.contactsapp.data.ContactsRepository

class NewContactViewModel : ViewModel() {

    private val contactsRepository: ContactsRepository by lazy { ContactsRepository() }

    private val _result: MutableLiveData<Boolean> = MutableLiveData()
    val result: LiveData<Boolean>
        get() = _result

    val name: ObservableField<String> = ObservableField()
//    val name:
////        get() = _nameLiveData<String>

    val number: ObservableField<String> = ObservableField()
//    val number: LiveData<String>
//        get() = _number

    fun saveContact(name: String, number: String, activity: Activity) {
        _result.value = contactsRepository.addContact(activity, name, number)
    }

}