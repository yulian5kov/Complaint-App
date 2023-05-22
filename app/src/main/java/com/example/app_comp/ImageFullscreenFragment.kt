package com.example.app_comp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.app_comp.databinding.FragmentImageFullscreenBinding
import com.example.app_comp.databinding.FragmentPostComplaintBinding


class ImageFullscreenFragment : Fragment() {

    private lateinit var binding: FragmentImageFullscreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? UserActivity)?.setCurrentFragment(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as? UserActivity)?.setCurrentFragment(this)
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"

        fun newInstance(imageUri: Uri): ImageFullscreenFragment {
            val fragment = ImageFullscreenFragment()
            val args = Bundle()
            args.putParcelable(ARG_IMAGE_URI, imageUri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageFullscreenBinding.inflate(layoutInflater)

        val imageUri = arguments?.getParcelable<Uri>(ARG_IMAGE_URI)
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(binding.imageViewFullscreen)
        }

        return binding.root
    }

}