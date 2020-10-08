package my.portfolio.contactsapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.portfolio.contactsapp.data.models.Contact

class DetailsViewModel : ViewModel() {

    private val _contact: MutableLiveData<Contact> = MutableLiveData()
    val contact: LiveData<Contact>
        get() = _contact

    fun setContact(contact: Contact) {
        _contact.value = contact
    }

    fun call(context: Context) {
        contact.value?.takeIf { it.numbers.isNotEmpty() }?.let {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + it.numbers[0]))
            context.startActivity(intent)
        }
    }

    @Suppress("SpellCheckingInspection")
    fun sendSMS(context: Context) {
        contact.value?.takeIf { it.numbers.isNotEmpty() }?.let { contact ->
            val uri = Uri.parse("smsto:" + contact.numbers[0])
            Intent(Intent.ACTION_SENDTO, uri).also {
                it.putExtra("sms_body", "your message...")
                context.startActivity(it)
            }
        }
    }

}