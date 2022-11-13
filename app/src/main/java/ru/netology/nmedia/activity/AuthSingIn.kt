package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAuthSingInBinding
import ru.netology.nmedia.viewmodel.AuthSingInViewModel

class AuthSingIn : Fragment(
) {

    private val viewModel: AuthSingInViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthSingInBinding.inflate(inflater, container, false)

        binding.enterButton.setOnClickListener {
            viewModel.login(
                binding.loginTextInputLayout.editText?.text.toString().trim(),
                binding.passwordTextInputLayout.editText?.text.toString().trim())
            findNavController().navigate(R.id.action_authSingIn_to_feedFragment)

        }

        return binding.root


        }


            }



//override fun onActivityCreated(savedInstanceState: Bundle?) {
//    super.onActivityCreated(savedInstanceState)
//    viewModel = ViewModelProvider(this).get(AuthSingInViewModel::class.java)
//
//}


//}