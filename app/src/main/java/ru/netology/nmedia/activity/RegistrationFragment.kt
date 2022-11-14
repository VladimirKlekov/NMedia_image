package ru.netology.nmedia.activity

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.viewmodel.AuthSingInViewModel
import java.io.File

class RegistrationFragment : Fragment() {
    private val viewModel: AuthSingInViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.loginEvent.collectLatest {
                findNavController().navigate(R.id.action_registrationFragment_to_feedFragment)
            }
        }
        lifecycleScope.launch {
            viewModel.loginException.collectLatest {
                Toast.makeText(
                    requireContext(),
                    R.string.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        lifecycleScope.launch {
            viewModel.authorizationFailed.collectLatest {
                Toast.makeText(
                    requireContext(),
                    R.string.wrong_login_or_password,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        lifecycleScope.launch {
            viewModel.lostConnection.collectLatest {
                Toast.makeText(
                    requireContext(),
                    R.string.lost_network_connection,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        fun onImage(image: String) {
            val bundle = Bundle().apply {
                putString("image", image)
            }
            findNavController().navigate(
                R.id.action_feedFragment_to_imageFragment, bundle
            )

        }


        binding.enterButton.setOnClickListener {

            if (binding.passwordTextInputLayout.editText?.text == binding.replayPasswordTextInputLayout.editText?.text)
                viewModel.registration(
                    binding.loginTextInputLayout.editText?.text.toString().trim(),
                    binding.passwordTextInputLayout.editText?.text.toString().trim(),
                    binding.nameTextInputLayout.editText?.text.toString().trim(),
                  )
            findNavController().navigate(R.id.action_registrationFragment_to_feedFragment)

        }

       return binding.root
    }
}

private fun AuthSingInViewModel.registration(login: String, password: String, name: String) {

}
