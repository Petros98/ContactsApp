package my.portfolio.contactsapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import my.portfolio.contactsapp.adapters.ContactsAdapter
import my.portfolio.contactsapp.databinding.FragmentContactsBinding


class ContactsFragment : Fragment() {


    private lateinit var binding: FragmentContactsBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ContactsViewModel
    private lateinit var adapter: ContactsAdapter

    private val requestCode = 66

    private val myPermissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater)

        factory = ViewModelFactory(requireActivity())
        viewModel = ViewModelProvider(this, factory).get(ContactsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ContactsAdapter()
        binding.rvContacts.adapter = adapter

        val isGranted =
            checkCallingOrSelfPermission(requireContext(), myPermissions[0]) == PERMISSION_GRANTED
                    &&
                    checkCallingOrSelfPermission(
                        requireContext(),
                        myPermissions[1]
                    ) == PERMISSION_GRANTED


        if (!isGranted)
            requestPermissions(myPermissions, requestCode)
        else
            lifecycleScope.launchWhenCreated {
                viewModel.fetchContacts()
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launchWhenCreated {
                viewModel.fetchContacts()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}