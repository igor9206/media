package ru.netology.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.music.dto.Album
import ru.netology.music.model.AlbumItem
import ru.netology.music.model.FeedItem
import ru.netology.music.model.TrackItem
import java.io.IOException

private const val albumUrl =
    "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

class AlbumViewModel : ViewModel() {
    private val gson = Gson()

    private var feedItems: List<FeedItem> = listOf()
    private val _data: MutableLiveData<List<FeedItem>> = MutableLiveData(feedItems)
    val data: LiveData<List<FeedItem>> = _data

    init {
        load()
    }

    private fun load() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(albumUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val album = gson.fromJson(response.body!!.string(), Album::class.java)
                    _data.postValue(parseAlbum(album))
                }
            }
        })

    }

    private fun parseAlbum(album: Album): List<FeedItem> {
        return listOf<FeedItem>(
            AlbumItem(
                album.id, album.artist, album.genre, album.published, album.subtitle, album.title
            )
        ) + album.tracks.map {
            TrackItem(it.id, it.file)
        }
    }

    fun setPlayPauseTrack(currentTrack: TrackItem?) {
        if (currentTrack != null) {
            feedItems = feedItems.map {
                when (it) {
                    is AlbumItem -> {
                        it.copy(play = currentTrack.play)
                    }

                    is TrackItem -> {
                        if (it.id == currentTrack.id) it.copy(play = currentTrack.play) else it.copy(
                            play = false
                        )
                    }
                }
            }
            _data.value = feedItems
        }
    }


}