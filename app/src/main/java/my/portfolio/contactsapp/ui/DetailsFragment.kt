package my.portfolio.contactsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import my.portfolio.contactsapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater)
        toolbar = binding.toolbar
        toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val contact = DetailsFragmentArgs.fromBundle(requireArguments()).contact
        viewModel.setContact(contact)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnCall.setOnClickListener {
            viewModel.call(requireContext())
        }
        binding.btnMessage.setOnClickListener {
            viewModel.sendSMS(requireContext())
        }

    }


}