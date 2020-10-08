package my.portfolio.contactsapp.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import my.portfolio.contactsapp.R
import my.portfolio.contactsapp.data.adapters.ContactsAdapter
import my.portfolio.contactsapp.data.adapters.OnItemClickListener
import my.portfolio.contactsapp.data.models.Contact
import my.portfolio.contactsapp.databinding.FragmentContactsBinding


class ContactsFragment : Fragment(), OnItemClickListener {


    private lateinit var binding: FragmentContactsBinding
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    private val requestCode = 66

    private val myPermissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.CALL_PHONE
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater)
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    ContactsFragmentDirections.actionContactsFragmentToDetailsFragment(
                        it
                    )
                )
                viewModel.displayDetailsComplete()
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = ContactsAdapter(this)
        binding.rvContacts.adapter = adapter

        val isGranted =
            checkCallingOrSelfPermission(requireContext(), myPermissions[0]) == PERMISSION_GRANTED
                    &&
                    checkCallingOrSelfPermission(
                        requireContext(),
                        myPermissions[1]
                    ) == PERMISSION_GRANTED
                    &&
                    checkCallingOrSelfPermission(
                        requireContext(),
                        myPermissions[2]
                    ) == PERMISSION_GRANTED


        if (!isGranted)
            requestPermissions(myPermissions, requestCode)
        else
            lifecycleScope.launch {
                viewModel.fetchContacts(requireActivity())
            }

        binding.search.doAfterTextChanged {
            it?.toString()?.let {
                val filtered = viewModel.contacts.value?.filter { contact ->
                    contact.name.contains(it, true)
                }
                adapter.submitList(filtered)
            }

        }

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_newContactFragment)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch {
                viewModel.fetchContacts(requireActivity())
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onClick(contact: Contact) = viewModel.displayContactDetails(contact)
}