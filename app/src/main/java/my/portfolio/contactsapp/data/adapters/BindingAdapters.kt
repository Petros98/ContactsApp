package my.portfolio.contactsapp.data.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textview.MaterialTextView
import my.portfolio.contactsapp.R
import my.portfolio.contactsapp.data.models.Contact


@BindingAdapter("listData")
fun addListToRV(recyclerView: RecyclerView, contacts: List<Contact>?) {
    if (recyclerView.adapter is ContactsAdapter)
        (recyclerView.adapter as ContactsAdapter).submitList(contacts)
}

@BindingAdapter("imageUri")
fun bindImage(imageView: ImageView, imageUri: String?) {

    Glide.with(imageView.context)
        .load(imageUri?.toUri())
        .centerCrop()
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_person_24px)
        )
        .into(imageView)

}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindContacts")
fun bindContacts(textView: MaterialTextView, list: ArrayList<String?>) {
    var text = textView.text.toString()
    list.toSet().forEach { contact ->
        text.takeUnless { contact.isNullOrEmpty() }?.apply {
            text += "$contact \n"
        }
    }
    textView.text = text
}