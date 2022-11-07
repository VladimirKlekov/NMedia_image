package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentOpenImageBinding

class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentOpenImageBinding.inflate(inflater, container, false)
        val attachmentUrl = arguments?.getString("image")

        binding.apply {
            image.visibility = View.GONE
            attachmentUrl?.let {
                val url = "${BASE_URL}/media/${it}"

                Glide.with(image)
                    .load(url)
                    .placeholder(R.drawable.ic_error_100dp)
                    .error(R.drawable.ic_loading_100dp)
                    .timeout(10_000)
                    .into(image)
            }
            image.visibility = View.VISIBLE
        }

        binding.image.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}