package ru.netology.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.music.R
import ru.netology.music.databinding.CardHeaderAlbumBinding
import ru.netology.music.databinding.CardTrackBinding
import ru.netology.music.model.AlbumItem
import ru.netology.music.model.FeedItem
import ru.netology.music.model.TrackItem

class AlbumAdapter : ListAdapter<FeedItem, RecyclerView.ViewHolder>(FeedItemCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlbumItem -> R.layout.card_header_album
            is TrackItem -> R.layout.card_track
            null -> error("unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.card_header_album -> {
                val binding = CardHeaderAlbumBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AlbumVH(binding)
            }

            R.layout.card_track -> {
                val binding =
                    CardTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TrackVH(binding)
            }

            else -> error("unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is AlbumItem -> (holder as AlbumVH).bind(item)
            is TrackItem -> (holder as TrackVH).bind(item)
            else -> error("unknown item type")
        }
    }


}

class AlbumVH(
    private val binding: CardHeaderAlbumBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(albumItem: AlbumItem) {
        with(binding) {
            nameAlbum.text = albumItem.title
            singer.text = albumItem.artist
            year.text = albumItem.published
            genre.text = albumItem.genre
        }
    }

}

class TrackVH(
    private val binding: CardTrackBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(trackItem: TrackItem) {
        binding.trackName.text = trackItem.file
    }

}

class FeedItemCallBack : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

}
