package com.yan.ahtbibleaudio001

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yan.ahtbibleaudio001.MediaItemData.Companion.PLAYBACK_RES_CHANGED
import com.yan.ahtbibleaudio001.views.fragments.MediaItemFragment
import com.yan.ahtbibleaudio001.databinding.FragmentMediaitemBinding

/**
 * [RecyclerView.Adapter] of [MediaItemData]s used by the [MediaItemFragment].
 */
class MediaItemAdapter(
    private val itemClickedListener: (MediaItemData) -> Unit
) : ListAdapter<MediaItemData, MediaViewHolder>(MediaItemData.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentMediaitemBinding.inflate(inflater, parent, false)
        return MediaViewHolder(binding, itemClickedListener)
    }

    override fun onBindViewHolder(
        holder: MediaViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        val mediaItem = getItem(position)
        var fullRefresh = payloads.isEmpty()

        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                when (payload) {
                    PLAYBACK_RES_CHANGED -> {
                        holder.playbackState.setImageResource(mediaItem.playbackRes)
                    }
                    // If the payload wasn't understood, refresh the full item (to be safe).
                    else -> fullRefresh = true
                }
            }
        }

        // Normally we only fully refresh the list item if it's being initially bound, but
        // we might also do it if there was a payload that wasn't understood, just to ensure
        // there isn't a stale item.
        if (fullRefresh) {
            holder.item = mediaItem
            holder.titleView.text = mediaItem.title
            holder.subtitleView.text = mediaItem.subtitle
            holder.playbackState.setImageResource(mediaItem.playbackRes)

            Glide.with(holder.albumArt)
                .load(mediaItem.albumArtUri)
                .into(holder.albumArt)
        }
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }
}

class MediaViewHolder(
    binding: FragmentMediaitemBinding,
    itemClickedListener: (MediaItemData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    val titleView: TextView = binding.title
    val subtitleView: TextView = binding.subtitle
    val albumArt: ImageView = binding.albumArt
    val playbackState: ImageView = binding.itemState

    var item: MediaItemData? = null

    init {
        binding.root.setOnClickListener {
            item?.let { itemClickedListener(it) }
        }
    }
}
