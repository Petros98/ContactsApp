package my.portfolio.contactsapp.ui

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import my.portfolio.contactsapp.R
import my.portfolio.contactsapp.databinding.FragmentNewContactBinding

class NewContactFragment : Fragment() {

    private lateinit var binding: FragmentNewContactBinding
    private val viewModel: NewContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewContactBinding.inflate(inflater).apply {
            number.inputType = InputType.TYPE_CLASS_PHONE
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.saveContact.setOnClickListener {
            viewModel.takeUnless {
                it.name.get().isNullOrEmpty() && it.number.get().isNullOrEmpty()
            }
                ?.run {
                    this.name.get()?.let { name ->
                        this.number.get()?.let { number ->
                            saveContact(name, number, requireActivity())
                        }
                    }
                }
        }

        viewModel.result.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.contactSaved),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failedSaving),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

//        viewModel.number.observe(viewLifecycleOwner, {
//            Log.i("NUMBER", it)
//        })
//        viewModel.name.observe(viewLifecycleOwner, {
//            Log.i("NUMBER", it)
//        })
    }

}