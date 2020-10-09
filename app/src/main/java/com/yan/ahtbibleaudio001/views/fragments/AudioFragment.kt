package com.yan.ahtbibleaudio001.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.remote.APIServiceAudio
import com.yan.ahtbibleaudio001.remote.AudioClient
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.repository.audio.AudioPagedListRepository
import com.yan.ahtbibleaudio001.viewmodels.AudioActivityViewModel
import com.yan.ahtbibleaudio001.views.adapters.AudioPagedListAdapter
import kotlinx.android.synthetic.main.fragment_audio.view.*

/**
 * A simple [Fragment] subclass.
 */
class AudioFragment : Fragment() {

    private lateinit var viewModel: AudioActivityViewModel
    lateinit var audioRepository: AudioPagedListRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_audio, container, false)
        val fragmentName = arguments?.getString("fragmentName")
        Toast.makeText(requireActivity(),"${fragmentName}",Toast.LENGTH_SHORT).show()
        val apiService: APIServiceAudio = AudioClient.getClient()

        audioRepository = AudioPagedListRepository(apiService)
        viewModel = getViewModel()

        val audioAdapter = AudioPagedListAdapter(requireActivity())

        val gridLayoutManager = GridLayoutManager(requireActivity(), 1)

        // we want the progressbar to show the full width
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = audioAdapter.getItemViewType(position)
                return if (viewType == audioAdapter.AUDIO_VIEW_TYPE) 1    // Audio_VIEW_TYPE will occupy 1 out of 3 span
                else 1                                             // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        rootView.rv_audio_list.layoutManager = gridLayoutManager
        rootView.rv_audio_list.setHasFixedSize(true)
        rootView.rv_audio_list.adapter = audioAdapter

        viewModel.audioPagedList.observe(requireActivity(), Observer {
            audioAdapter.submitList(it)
        })

        viewModel.networkState.observe(requireActivity(), Observer {
            rootView.progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            rootView.txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                audioAdapter.setNetworkState(it)
            }
        })

        return rootView
    }

    private fun getViewModel(): AudioActivityViewModel {
        return ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AudioActivityViewModel(audioRepository) as T
            }
        })[AudioActivityViewModel::class.java]
    }

}
