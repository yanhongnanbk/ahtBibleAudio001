package com.yan.ahtbibleaudio001.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.lifecycle.Observer
import com.yan.ahtbibleaudio001.MediaItemAdapter
import com.yan.ahtbibleaudio001.viewmodels.MainActivityViewModel
import com.yan.ahtbibleaudio001.databinding.FragmentMediaitemListBinding
import com.yan.ahtbibleaudio001.viewmodels.MediaItemFragmentViewModel
import com.yan.ahtbibleaudio001.zutil001.utils.InjectorUtils

/**
 * A fragment representing a list of MediaItems.
 */
class MediaItemFragment : Fragment() {
    private val mainActivityViewModel by activityViewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(requireContext())
    }
    private val mediaItemFragmentViewModel by viewModels<MediaItemFragmentViewModel> {
        InjectorUtils.provideMediaItemFragmentViewModel(requireContext(), mediaId)
    }

    private lateinit var mediaId: String
    private lateinit var binding: FragmentMediaitemListBinding

    private val listAdapter = MediaItemAdapter { clickedItem ->
        mainActivityViewModel.mediaItemClicked(clickedItem)


    }

    companion object {
        fun newInstance(mediaId: String): MediaItemFragment {

            return MediaItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDIA_ID_ARG, mediaId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaitemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Always true, but lets lint know that as well.
        mediaId = arguments?.getString(MEDIA_ID_ARG) ?: return
        Log.d("MEDIAID", mediaId)

        mediaItemFragmentViewModel.mediaItems.observe(viewLifecycleOwner,
            Observer { list ->
                binding.loadingSpinner.visibility =
                    if (list?.isNotEmpty() == true) View.GONE else View.VISIBLE
                if (list?.isNotEmpty() == true) {

                    list.forEach {
                        Log.d("SUBTITLE", it.subtitle)
                        Log.d("MEDIAID", it.mediaId)
                    }
                }


                listAdapter.submitList(list)

            })
        mediaItemFragmentViewModel.networkError.observe(viewLifecycleOwner,
            Observer { error ->
                binding.networkError.visibility = if (error) View.VISIBLE else View.GONE
            })

        // Set the adapter
        binding.list.adapter = listAdapter

    }
}

private const val MEDIA_ID_ARG = "com.yan.ahtbibleaudio001.views.fragments.MediaItemFragment.MEDIA_ID"
