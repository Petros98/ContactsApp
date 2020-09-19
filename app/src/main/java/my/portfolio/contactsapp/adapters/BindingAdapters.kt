package my.portfolio.contactsapp.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun addListToRV(recyclerView: RecyclerView, contacts: List<Contact>) {
    val adapter = recyclerView.adapter as ContactsAdapter
    adapter.submitList(contacts)
}